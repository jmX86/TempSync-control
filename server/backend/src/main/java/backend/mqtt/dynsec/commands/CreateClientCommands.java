package backend.mqtt.dynsec.commands;

import java.util.Arrays;
import java.util.List;

public class CreateClientCommands {
    private List<DynsecCreateClient> commands;

    public CreateClientCommands(DynsecCreateClient... commands) {
        this.commands = Arrays.stream(commands).toList();
    }

    public List<DynsecCreateClient> getCommands() {
        return commands;
    }
}
