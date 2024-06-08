package backend.mqtt.adapter;

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
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;

import javax.net.ssl.SSLContext;

public class MqttUserFactory {
    @Value("${tempsync.mqtt.broker.url}")
    private String mqttBrokerUrl;

    @Value("${tempsync.mqtt.broker.port}")
    private int mqttBrokerPort;

    private final Logger logger = LoggerFactory.getLogger(MqttUserAdminAdapter.class);

    public MqttUserFactory(){
    }

    public MqttPahoClientFactory getUserFactory(String userName, String password, SSLContext sslContext){
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();

        MqttConnectOptions options = new MqttConnectOptions();

        String mqttBrokerURI = mqttBrokerUrl + ":" + mqttBrokerPort;
        options.setServerURIs(new String[] {mqttBrokerURI});

        options.setUserName(userName);
        options.setPassword(password.toCharArray());
        options.setCleanSession(true);
        options.setSocketFactory(sslContext.getSocketFactory());

        factory.setConnectionOptions(options);

        return factory;
    }
}
