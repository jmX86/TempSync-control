package backend.mqtt.dynsec.commands;

public enum DynsecACLtype {
    PUBLISH_SEND("publishClientSend"),
    PUBLISH_RECEIVE("publishClientReceive"),
    SUBSCRIBE("subscribe"),
    SUBSCRIBE_PATTERN("subscribePattern"),
    SUBSCRIBE_LITERAL("subscribeLiteral"),
    UNSUBSCRIBE("unsubscribe"),
    UNSUBSCRIBE_PATTERN("unsubscribePattern"),
    UNSUBSCRIBE_LITERAL("unsubscribeLiteral")
    ;

    public final String aclValue;

    DynsecACLtype(String aclTypeValue) {
        this.aclValue = aclTypeValue;
    }
}
