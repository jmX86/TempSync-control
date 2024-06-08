package backend.mqtt.dynsec.commands;

import java.util.Arrays;
import java.util.List;

public class DynsecCreateRole {
    private String command = "createRole";
    private String rolename;
    private List<DynsecACL> acls;

    public DynsecCreateRole(String rolename, DynsecACL... acls){
        this.rolename = rolename;
        this.acls = Arrays.stream(acls).toList();
    }

    public String getCommand() {
        return command;
    }

    public String getRolename() {
        return rolename;
    }

    public List<DynsecACL> getAcls() {
        return acls;
    }
}
