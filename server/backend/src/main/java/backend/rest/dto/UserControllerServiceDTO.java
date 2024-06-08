package backend.rest.dto;

import backend.domain.User;

public class UserControllerServiceDTO {
    private String userName;

    public UserControllerServiceDTO(String userName) {
        this.userName = userName;
    }

    public UserControllerServiceDTO(User user){
        this.userName = user.getUserName();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
