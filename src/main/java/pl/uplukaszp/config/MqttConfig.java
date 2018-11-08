package pl.uplukaszp.config;

import java.io.File;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqttConfig {

	@Value("${mqtt.server.uri}")
	private String uri;

	@Bean(name = "mqttClient")
	public MqttClient getMqttClient() throws MqttException {
		String dir = "";
		dir += File.separator + "lockFile";
		File f = new File(dir);
		if (!f.exists())
			f.mkdirs();
		MqttDefaultFilePersistence mqttPersistence = new MqttDefaultFilePersistence(dir);
		MqttClient client = new MqttClient(uri, MqttClient.generateClientId());
		client.connect();
		return client;
	}
}
