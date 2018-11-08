package pl.uplukaszp.services.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import pl.uplukaszp.domain.Device;
import pl.uplukaszp.domain.DeviceParameters;
import pl.uplukaszp.repositories.MqttRepository;
import pl.uplukaszp.services.DeviceService;
import pl.uplukaszp.services.ParametersService;

@Service
public class ParametersServiceImpl implements ParametersService {

	private DeviceService deviceService;
	private MqttRepository localDeviceRepository;

	public ParametersServiceImpl(@Lazy DeviceService deviceService, MqttRepository localDeviceRepository) {
		super();
		this.deviceService = deviceService;
		this.localDeviceRepository = localDeviceRepository;
	}

	@Override
	public Optional<DeviceParameters> turnOn(String id) {
		if (deviceService.deviceIsRegistred(id))
			return localDeviceRepository.turnOn(id);
		else
			return Optional.empty();
	}

	@Override
	public Optional<DeviceParameters> turnOff(String id) {
		if (deviceService.deviceIsRegistred(id))
			return localDeviceRepository.turnOff(id);
		else
			return Optional.empty();
	}

	@Override
	public Optional<DeviceParameters> setTemperature(String id, Float temp) {
		if (deviceService.deviceIsRegistred(id))
			return localDeviceRepository.setTemperature(id, temp);
		else
			return Optional.empty();

	}

	@Override
	public Optional<DeviceParameters> getParameters(String id) {
		if (deviceService.deviceIsRegistred(id))
			return localDeviceRepository.getParameters(id);
		else
			return Optional.empty();
	}

	@Override
	public Map<String, DeviceParameters> getParameters(List<Device> devices) {
		return localDeviceRepository.getParameters(devices);
	}

	@Override
	public Map<String, DeviceParameters> getParameters() {
		return getParameters(deviceService.getDevicesWithoutInfo());
	}

}
