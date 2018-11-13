package pl.uplukaszp.repositories.impl;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import pl.uplukaszp.components.MqttDataProvider;
import pl.uplukaszp.domain.Device;
import pl.uplukaszp.domain.DeviceParameters;
import pl.uplukaszp.domain.DeviceState;
import pl.uplukaszp.domain.projections.DeviceOnlyWithIdProjection;
import pl.uplukaszp.repositories.MqttRepository;

@Service
@Profile("!development")
public class MqttRepositoryImpl implements MqttRepository {

	@Autowired
	MqttDataProvider dataProvider;

	@Override
	public List<DeviceOnlyWithIdProjection> findNewDevices() {
		return dataProvider.getIds();
	}

	@Override
	public Map<String, DeviceParameters> getParameters(List<Device> devices) {
		return dataProvider.getParams(devices);
	}

	@Override
	public Optional<DeviceParameters> getParameters(String id) {
		return dataProvider.getParams(id);
	}

	@Override
	public Optional<DeviceParameters> turnOn(String id) {
		return dataProvider.setMode(DeviceState.on,id);
	}

	@Override
	public Optional<DeviceParameters> turnOff(String id) {
		return dataProvider.setMode(DeviceState.off,id);

	}

	@Override
	public Optional<DeviceParameters> setTemperature(String id, Float temp) {
		return dataProvider.setMode(DeviceState.auto,id,temp);

	}

}
