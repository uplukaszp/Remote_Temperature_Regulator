package pl.uplukaszp.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
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

	MqttClient client;
	MqttClient client2;
	Set<DeviceOnlyWithIdProjection> ids = new HashSet<>();
	Map<String, DeviceParameters> params = new ConcurrentHashMap<>();
	StringBuilder lock = new StringBuilder();
	long t = 0;

	public MqttDataProvider(MqttClient mqttClient, MqttClient mqttSecond) {
		client = mqttClient;
		client2 = mqttSecond;

		try {
			client.subscribeWithResponse("/devices/find/ids", recieveIds());
			client.subscribeWithResponse("/devices/+/info", recieveInfos());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<DeviceOnlyWithIdProjection> getIds() {
		return new ArrayList<>(ids);
	}

	public Map<String, DeviceParameters> getParams(List<Device> devices) {
		Map<String, DeviceParameters> map = new HashMap<>();
		for (Device device : devices) {
			DeviceParameters d = params.get(device.getId());
			if (d == null) {
				d = new DeviceParameters();
				d.setCurrentTemperature(0f);
				d.setSavedTemperature(0f);
				d.setState(DeviceState.offline);
			}
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
			System.out.println("mode update: " + mapper.writeValueAsString(mode));
			client2.publish("/devices/" + id + "/mode", message);
		} catch (MqttException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return waitForParams(id);
	}

	@Scheduled(fixedRate = 5000)
	private void refreshIds() {
		try {

			client2.publish("/devices/find", new MqttMessage());
		} catch (MqttException e) {
			e.printStackTrace();
		}

	}

	@Scheduled(fixedRate = 5000)
	private void refreshParams() {
		try {

			client2.publish("/devices/info", new MqttMessage("ALL".getBytes()));
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
			System.err.println("PARAM RECV" + id);
			params.put(id, param);
			System.err.println(System.currentTimeMillis() - t);
			synchronized (lock) {
				if (lock.toString().equals(id)) {
					lock.notify();
					System.err.println("UNLOCKED");
				}
			}
		};
	}

	private Optional<DeviceParameters> waitForParams(String id) {
		try {
			synchronized (lock) {
				params.remove(id);
				t = System.currentTimeMillis();
				System.out.println("WAIT FOR PARAMS: " + id);
				lock.setLength(0);
				lock.append(id);
				lock.wait(5000);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Optional<DeviceParameters> p = Optional.ofNullable(params.get(id));
		if (!p.isPresent()) {
			DeviceParameters d = new DeviceParameters();
			d.setCurrentTemperature(0f);
			d.setSavedTemperature(0f);
			d.setState(DeviceState.offline);
			p=Optional.of(d);
		}
		System.out.println("Returning: " + p.get());
		return p;
	}
}
