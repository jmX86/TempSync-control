package backend.service;

import backend.domain.User;
import backend.rest.dto.CreateUserDTO;
import backend.rest.dto.UserControllerServiceDTO;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserControllerServiceDTO> listAll();

    Optional<User> findUserByUsername(String userName);
    Optional<User> findUserByEmail(String userEmail);

    UserControllerServiceDTO registerNewUser(CreateUserDTO userDTO);
}
