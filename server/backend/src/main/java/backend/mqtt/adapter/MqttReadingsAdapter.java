package backend.mqtt.adapter;

import backend.service.ReadingService;
import jakarta.validation.constraints.NotNull;
import org.apache.tomcat.jni.SSL;
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
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import javax.net.ssl.SSLContext;
import java.util.Objects;
import java.util.StringJoiner;

@Configuration
public class MqttReadingsAdapter {
    @Value("${tempsync.mqtt.broker.url}")
    private String mqttBrokerUrl;

    @Value("${tempsync.mqtt.broker.port}")
    private int mqttBrokerPort;

    @Value("${tempsync.mqtt.broker.dynsec.readings.username}")
    private String mqttBrokerReadingsName;

    @Value("${tempsync.mqtt.broker.dynsec.readings.pass}")
    private String mqttBrokerReadingsPass;

    private final Logger logger = LoggerFactory.getLogger(MqttReadingsAdapter.class);

    @Autowired
    private ReadingService readingService;

    private final SSLContext sslContext;

    @Autowired
    public MqttReadingsAdapter(SslBundles sslBundles) throws NoSuchSslBundleException {
        SslBundle sslBundle = sslBundles.getBundle("mqtt-broker");
        this.sslContext = sslBundle.createSslContext();
    }

    private MqttPahoClientFactory getUserFactory(){
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();

        MqttConnectOptions options = new MqttConnectOptions();

        String mqttBrokerURI = mqttBrokerUrl + ":" + mqttBrokerPort;
        options.setServerURIs(new String[] {mqttBrokerURI});

        options.setUserName(mqttBrokerReadingsName);
        options.setPassword(mqttBrokerReadingsPass.toCharArray());
        options.setCleanSession(true);
        options.setSocketFactory(sslContext.getSocketFactory());
        options.setAutomaticReconnect(true);

        factory.setConnectionOptions(options);

        return factory;
    }

    @Bean
    public MessageChannel mqttInboundReadingsChannel(){
        return new DirectChannel();
    }

    @Bean
    public MessageProducer mqttInboundReadingsAdapter(){
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(
                "server-backend-mqtt-readings-id-1",
                getUserFactory(),
                "+/up/#"
        );

        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(2);
        adapter.setOutputChannel(mqttInboundReadingsChannel());

        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttInboundReadingsChannel")
    public MessageHandler mqttInboundReadingsHandler(){
        return new MessageHandler() {
            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                String topic = Objects.requireNonNull(message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC)).toString();
                logger.info("Reading topic: {}", topic);

                String payload = message.getPayload().toString();
                if(payload.length() <= 128){
                    String[] splitTopic = topic.split("/");
                    logger.info("Payload: {}", payload);

                    if(splitTopic.length > 3){
                        // If topic has more than 3 parts it is a readings topic
                        String topicUser = splitTopic[0];
                        String topicDevice = splitTopic[2];
                        StringBuilder subtopicName = new StringBuilder();

                        for(int i = 3; i < splitTopic.length; i++){
                            subtopicName.append(splitTopic[i]);
                            if(i != splitTopic.length-1){
                                subtopicName.append('/');
                            }
                        }

                        try{
                            logger.info("Subtopic: {}", subtopicName);
                            readingService.saveNewReading(topicUser, topicDevice, subtopicName.toString(), payload);
                        }catch(Exception e){
                            logger.error(e.toString());
                        }
                    }else if(splitTopic.length == 3){
                        // If topic has exactly 3 parts it is a device topic broadcast
                        // TODO: Parse topic broadcast and add topics to device
                        int a = 0;
                    }
                }
            }
        };
    }
}
