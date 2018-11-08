package pl.uplukaszp.repositories.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttToken;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import pl.uplukaszp.domain.Device;
import pl.uplukaszp.domain.DeviceParameters;
import pl.uplukaszp.domain.DeviceState;
import pl.uplukaszp.domain.projections.DeviceOnlyWithIdProjection;

@Component
public class MqttDataProvider {
	ObjectMapper mapper = new ObjectMapper();
	@Autowired
	MqttClient client;
	List<DeviceOnlyWithIdProjection> ids = new ArrayList<>();
	Map<String, DeviceParameters> params = new HashMap<>();
	StringBuilder lock = new StringBuilder();

	public MqttDataProvider(MqttClient mqttClient) {
		client = mqttClient;

		try {
			client.subscribeWithResponse("/devices/find/ids", recieveIds());
			client.subscribeWithResponse("/devices/+/info", recieveInfos());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<DeviceOnlyWithIdProjection> getIds() {
		return ids;
	}

	public Map<String, DeviceParameters> getParams(List<Device> devices) {
		Map<String, DeviceParameters> map = new HashMap<>();
		for (Device device : devices) {
			map.put(device.getId(), params.get(device.getId()));
		}
		return map;
	}

	public Optional<DeviceParameters> getParams(String id) {

		return waitForParams(id);
	}

	public Optional<DeviceParameters> setMode(DeviceState state, String id) {
		return setMode(state, id, 0f);
	}

	public Optional<DeviceParameters> setMode(DeviceState state, String id, Float temp) {
		Map<String, String> mode = new HashMap<>();
		mode.put("mode", state.toString());
		mode.put("temperature", temp.toString());
		MqttMessage message = new MqttMessage();
		try {
			message.setPayload(mapper.writeValueAsBytes(mode));
			client.publish("/devices/" + id + "/mode", message);
		} catch (MqttException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return waitForParams(id);
	}

	@Scheduled(fixedRate = 5000)
	private void refreshIds() {
		ids.clear();
		try {

			client.publish("/devices/find", new MqttMessage());
		} catch (MqttException e) {
			e.printStackTrace();
		}

	}

	@Scheduled(fixedRate = 5000)
	private void refreshParams() {
		try {

			client.publish("/devices/info", new MqttMessage("ALL".getBytes()));
		} catch (MqttException e) {
			e.printStackTrace();
		}

	}

	private IMqttMessageListener recieveIds() {
		return (topic, message) -> {
			DeviceOnlyWithIdProjection dev = new DeviceOnlyWithIdProjection();
			dev.setId(new String(message.getPayload()));
			ids.add(dev);
		};
	}

	private IMqttMessageListener recieveInfos() {
		return (topic, message) -> {
			String id = topic.replace("/devices/", "").replace("/info", "");
			String messagePayload = new String(message.getPayload());
			DeviceParameters param = mapper.readValue(messagePayload, DeviceParameters.class);

			params.put(id, param);
			synchronized (lock) {
				if (lock.toString().equals(id)) {
					lock.notify();
				}
			}
		};
	}

	private Optional<DeviceParameters> waitForParams(String id) {
		try {
			synchronized (lock) {
				params.remove(id);
				lock.setLength(0);
				lock.append(id);
				lock.wait(5000);
			}
			return Optional.of(params.get(id));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return Optional.empty();
	}
}
