package backend.rest;

import backend.domain.User;
import backend.mqtt.MqttDynsecGateway;
import backend.mqtt.dynsec.commands.*;
import backend.rest.dto.CreateUserDTO;
import backend.rest.dto.UserControllerServiceDTO;
import backend.service.UserService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Secured("ADMIN")
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private MqttDynsecGateway mqttDynsecGateway;

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("")
    public List<UserControllerServiceDTO> listUsers(){
        return userService.listAll();
    }

    @PostMapping("/register")
    public UserControllerServiceDTO registerNewUser(@RequestBody CreateUserDTO dto){
        return userService.registerNewUser(dto);
    }
}
