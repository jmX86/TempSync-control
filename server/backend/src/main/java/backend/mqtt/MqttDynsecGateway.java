package backend.mqtt;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;

@MessagingGateway(defaultRequestChannel = "mqttOutboundDynsecChannel")
public interface MqttDynsecGateway {

    void sendDynsecCommand(String data, @Header(MqttHeaders.TOPIC) String topic);
}
