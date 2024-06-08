package backend.service.impl;

import backend.BackendApplication;
import backend.dao.RoleRepository;
import backend.dao.UserRepository;
import backend.domain.Role;
import backend.domain.User;
import backend.mqtt.MqttDynsecGateway;
import backend.mqtt.dynsec.commands.*;
import backend.rest.dto.CreateUserDTO;
import backend.rest.dto.UserControllerServiceDTO;
import backend.service.UserService;
import backend.service.exceptions.DataNotFoundException;
import backend.service.exceptions.MqttServiceException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceJpa implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private MqttDynsecGateway mqttDynsecGateway;

    private final Logger logger = LoggerFactory.getLogger(UserServiceJpa.class);

    @Override
    public List<UserControllerServiceDTO> listAll() {
        List<User> userList = userRepository.findAll();

        return userList.stream()
                        .map(user -> new UserControllerServiceDTO(user.getUserName()))
                        .toList();
    }

    @Override
    public Optional<User> findUserByUsername(String userName) {
        return userRepository.findByUserName(userName);
    }

    @Override
    public Optional<User> findUserByEmail(String userEmail) {
        return userRepository.findByUserEmail(userEmail);
    }

    @Override
    public UserControllerServiceDTO registerNewUser(CreateUserDTO userDTO) {

        Assert.isTrue(userRepository.countAllByUserEmail(userDTO.getUserEmail()) == 0, "User with that email is already registered.");
        Assert.isTrue(userRepository.countAllByUserName(userDTO.getUserName()) == 0, "User with that username already registered.");

        Assert.hasText(userDTO.getUserEmail(), "No email provided!");
        Assert.hasText(userDTO.getUserName(), "No username provided!");

        Assert.isTrue(userDTO.getUserEmail().length() <= 128, "Email must be shorter than 128 characters.");
        Assert.isTrue(userDTO.getUserName().length() <= 64, "Username must be shorter than 64 characters.");
        Assert.isTrue(userDTO.getUserName().length() >= 6, "Username must be longer than 6 characters.");

        Assert.isTrue(userDTO.getUserName().matches(BackendApplication.USERNAME_FORMAT), "Username must contain at least 3 lowercase letters at the beginning and optionally numbers at the end.");
        Assert.isTrue(userDTO.getUserEmail().matches(BackendApplication.EMAIL_FORMAT), "Provided email is not in the right format.");

        Assert.hasText(userDTO.getUserPassword(), "Password must be provided.");
        Assert.isTrue(userDTO.getUserPassword().length() >= 8, "Password must be longer than 8 characters");
        Assert.isTrue(checkPassword(userDTO.getUserPassword()), "Password must contain at least one lower case and one upper case letter.");

        Role role = roleRepository.findRoleByRoleName("BASIC_USER").orElseThrow(() -> new DataNotFoundException("Role not found."));

        User newUser = new User(
                userDTO.getUserEmail(),
                userDTO.getUserName(),
                encoder.encode(userDTO.getUserPassword()),
                role);

        try{
            DynsecCreateRole dynsecRoleForUser = generateRoleForUser(userDTO.getUserName());

            CreateRoleCommands createRoleCommands = new CreateRoleCommands(dynsecRoleForUser);

            JSONObject jsonCreateRoleCommand = new JSONObject(createRoleCommands);
            logger.info("Creating dynsec role.");

            mqttDynsecGateway.sendDynsecCommand(
                    jsonCreateRoleCommand.toString(),
                    "$CONTROL/dynamic-security/v1"
            );

            CreateClientCommands createClientCommands = new CreateClientCommands(
                    generateNewMqttClient(
                            userDTO.getUserName(),
                            userDTO.getUserPassword(),
                            dynsecRoleForUser
                    )
            );

            JSONObject jsonCreateClientCommand = new JSONObject(createClientCommands);
            logger.info("Creating new client.");

            mqttDynsecGateway.sendDynsecCommand(
                    jsonCreateClientCommand.toString(),
                    "$CONTROL/dynamic-security/v1"
            );
        }catch(Exception e){
            logger.error(e.toString());
            throw new MqttServiceException("Exception while adding user.");
        }

        return new UserControllerServiceDTO(userRepository.save(newUser));
    }

    private Boolean checkPassword(String password){
        boolean hasUppercase = false, hasLowercase = false;

        for(char c : password.toCharArray()){
            if(Character.isUpperCase(c)){
                hasUppercase = true;
            }else if(Character.isLowerCase(c)){
                hasLowercase = true;
            }
        }

        return hasLowercase && hasUppercase;
    }

    private DynsecCreateRole generateRoleForUser(String username){
        DynsecACL dynsecACLPubSend = new DynsecACL(DynsecACLtype.PUBLISH_SEND, username + "/up/#", true);
        DynsecACL dynsecACLPubSub = new DynsecACL(DynsecACLtype.SUBSCRIBE_PATTERN, username + "/up/#", true);

        DynsecACL dynsecACLSub = new DynsecACL(DynsecACLtype.SUBSCRIBE_PATTERN, username + "/down/#", true);
        DynsecACL dynsecACLSubPub = new DynsecACL(DynsecACLtype.PUBLISH_SEND, username + "/down/#", true);

        DynsecACL dynsecACLUnsub = new DynsecACL(DynsecACLtype.UNSUBSCRIBE_PATTERN, username + "/down/#", true);
        DynsecACL dynsecACLPubUnsub = new DynsecACL(DynsecACLtype.UNSUBSCRIBE_PATTERN, username + "/up/#", true);

        return new DynsecCreateRole(username + "-role",
                dynsecACLPubSend, dynsecACLSub,
                dynsecACLPubSub, dynsecACLSubPub,
                dynsecACLUnsub, dynsecACLPubUnsub
        );
    }

    private DynsecCreateClient generateNewMqttClient(String username, String password, DynsecCreateRole dynsecCreateUserRole){

        return new DynsecCreateClient(username, password, dynsecCreateUserRole);
    }
}
