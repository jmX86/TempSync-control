package backend.mqtt.dynsec.commands;

public class DynsecACL {
    private String aclType;
    private String topic;
    private final int priority = -1;
    private boolean allow;

    public DynsecACL(DynsecACLtype aclType, String topic, boolean allow){
        this.aclType = aclType.aclValue;
        this.topic = topic;
        this.allow = allow;
    }

    public String getAclType() {
        return aclType;
    }

    public String getTopic() {
        return topic;
    }

    public int getPriority() {
        return priority;
    }

    public boolean getAllow() {
        return allow;
    }
}
