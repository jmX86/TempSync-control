package backend.mqtt.adapter;

import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ssl.NoSuchSslBundleException;
import org.springframework.boot.ssl.SslBundle;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.ClientManager;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.core.Mqttv3ClientManager;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

import javax.net.ssl.SSLContext;
import java.util.Objects;

@Configuration
public class MqttUserAdminAdapter {
    @Value("${tempsync.mqtt.broker.url}")
    private String mqttBrokerUrl;

    @Value("${tempsync.mqtt.broker.port}")
    private int mqttBrokerPort;

    @Value("${tempsync.mqtt.broker.dynsec.admin.username}")
    private String mqttBrokerDynsecAdminName;

    @Value("${tempsync.mqtt.broker.dynsec.admin.pass}")
    private String mqttBrokerDynsecAdminPass;

    private final Logger logger = LoggerFactory.getLogger(MqttUserAdminAdapter.class);

    private final SSLContext sslContext;

    @Autowired
    public MqttUserAdminAdapter(SslBundles sslBundles){
        SslBundle sslBundle = sslBundles.getBundle("mqtt-broker");
        this.sslContext = sslBundle.createSslContext();
    }

    private MqttPahoClientFactory getUserFactory(){
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();

        MqttConnectOptions options = new MqttConnectOptions();

        String mqttBrokerURI = mqttBrokerUrl + ":" + mqttBrokerPort;
        options.setServerURIs(new String[] {mqttBrokerURI});

        options.setUserName(mqttBrokerDynsecAdminName);
        options.setPassword(mqttBrokerDynsecAdminPass.toCharArray());
        options.setCleanSession(true);
        options.setSocketFactory(sslContext.getSocketFactory());
        options.setAutomaticReconnect(true);

        factory.setConnectionOptions(options);

        return factory;
    }

    @Bean
    public MessageChannel mqttInboundDynsecChannel(){
        return new DirectChannel();
    }

    @Bean
    public MessageProducer mqttInboundDynsecAdapter(){
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(
                "server-backend-mqtt-useradmin-id-1",
                getUserFactory(),
                "$CONTROL/dynamic-security/v1/response");

        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(2);
        adapter.setOutputChannel(mqttInboundDynsecChannel());

        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttInboundDynsecChannel")
    public MessageHandler mqttInboundDynsecHandler(){
        return new MessageHandler() {
            @Override
            public void handleMessage(Message<?> message) throws MessagingException {

                String topic = Objects.requireNonNull(message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC)).toString();
                logger.info("Topic: {}", topic);

                logger.info("Payload: {}", message.getPayload());
            }
        };
    }

    @Bean
    public MessageChannel mqttOutboundDynsecChannel(){
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundDynsecChannel")
    public MessageHandler mqttOutboundDynsec(){
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(
                "server-backend-mqtt-useradmin-id-2",
                getUserFactory()
        );

        messageHandler.setAsync(true);
        messageHandler.setDefaultTopic("$CONTROL/dynamic-security/#");
        messageHandler.setDefaultQos(1);

        return messageHandler;
    }
}
