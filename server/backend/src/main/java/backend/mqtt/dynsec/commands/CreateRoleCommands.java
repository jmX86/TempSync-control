package backend.mqtt.dynsec.commands;

import java.util.Arrays;
import java.util.List;

public class CreateRoleCommands {
    private List<DynsecCreateRole> commands;

    public CreateRoleCommands(DynsecCreateRole... roles){
        this.commands = Arrays.stream(roles).toList();
    }

    public List<DynsecCreateRole> getCommands() {
        return commands;
    }
}
