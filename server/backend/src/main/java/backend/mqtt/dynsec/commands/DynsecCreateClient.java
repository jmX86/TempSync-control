package backend.mqtt.dynsec.commands;

import java.util.Arrays;
import java.util.List;

public class DynsecCreateClient {
    private String command = "createClient";
    private String username;
    private String password;
    private List<LocalRole> roles;

    public DynsecCreateClient(String username, String password, DynsecCreateRole... roles) {
        this.username = username;
        this.password = password;
        this.roles = Arrays.stream(roles).map(LocalRole::new).toList();
    }

    public String getCommand() {
        return command;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public List<LocalRole> getRoles() {
        return roles;
    }

    public class LocalRole{
        private String rolename;
        private int priority = -1;

        public LocalRole(DynsecCreateRole role) {
            this.rolename = role.getRolename();
        }

        public String getRolename() {
            return rolename;
        }

        public int getPriority() {
            return priority;
        }
    }
}
