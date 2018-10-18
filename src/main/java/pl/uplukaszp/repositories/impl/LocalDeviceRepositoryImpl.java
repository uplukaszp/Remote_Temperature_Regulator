package pl.uplukaszp.repositories.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import pl.uplukaszp.domain.Device;
import pl.uplukaszp.domain.DeviceParameters;
import pl.uplukaszp.domain.projections.DeviceOnlyWithIdProjection;
import pl.uplukaszp.repositories.LocalDeviceRepository;

@Service
@Profile("!development")
public class LocalDeviceRepositoryImpl implements LocalDeviceRepository {

	@Override
	public List<DeviceOnlyWithIdProjection> findNewDevices() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, DeviceParameters> getParameters(List<Device> devices) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<DeviceParameters> getParameters(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<DeviceParameters> turnOn(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<DeviceParameters> turnOff(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<DeviceParameters> setTemperature(String id, Float temp) {
		// TODO Auto-generated method stub
		return null;
	}

}
