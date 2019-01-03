package pl.uplukaszp.services.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import pl.uplukaszp.domain.Device;
import pl.uplukaszp.domain.DeviceParameters;
import pl.uplukaszp.domain.dto.DeviceDTO;
import pl.uplukaszp.domain.projections.DeviceOnlyWithIdProjection;
import pl.uplukaszp.repositories.DeviceRepository;
import pl.uplukaszp.repositories.MqttRepository;
import pl.uplukaszp.services.DeviceService;
import pl.uplukaszp.services.ParametersService;

@Service
@Profile("!development")
public class DeviceServiceImpl implements DeviceService {
	DeviceRepository deviceRepository;
	MqttRepository mqttRepository;
	ParametersService parametersService;

	public DeviceServiceImpl(DeviceRepository deviceRepository, MqttRepository localDeviceRepository,
			ParametersService parametersService) {
		this.deviceRepository = deviceRepository;
		this.mqttRepository = localDeviceRepository;
		this.parametersService = parametersService;
	}

	@Override
	public List<Device> getDevices() {
		List<Device> devices = deviceRepository.findAll();
		Map<String, DeviceParameters> parameters = parametersService.getParameters(devices);
		for (Device device : devices) {
			device.setInfo(parameters.get(device.getId()));
		}
		return devices;
	}

	@Override
	public Optional<Device> getDevice(String id) {
		Optional<Device> device = deviceRepository.findById(id);
		device = addInfoToDevice(device);
		return device;
	}

	@Override
	public Device addDevice(DeviceDTO device) {
		Device newDevice = new Device();
		newDevice.setId(device.getId());
		newDevice.setName(device.getName());
		newDevice.setTasks(new ArrayList<>());
		newDevice = deviceRepository.save(newDevice);
		newDevice = addInfoToDevice(newDevice);
		return newDevice;
	}

	@Override
	public boolean remove(String id) {
		Optional<Device> device = deviceRepository.findById(id);
		if (device.isPresent()) {
			Device deviceToRemove = device.get();
			deviceRepository.delete(deviceToRemove);
			return true;
		}
		return false;
	}

	@Override
	public List<DeviceOnlyWithIdProjection> findNewLocalDevices() {
		List<DeviceOnlyWithIdProjection> localDevices = mqttRepository.findNewDevices();
		Set<String> registredDevices = deviceRepository.findAllIds();
		Iterator<DeviceOnlyWithIdProjection> it = localDevices.iterator();
		while (it.hasNext()) {
			DeviceOnlyWithIdProjection device = it.next();
			if (registredDevices.contains(device.getId())) {
				it.remove();
			}
		}
		return localDevices;
	}

	private Optional<Device> addInfoToDevice(Optional<Device> device) {
		if (device.isPresent()) {
			Device d = addInfoToDevice(device.get());
			return Optional.of(d);
		}
		return device;

	}

	private Device addInfoToDevice(Device device) {
		Optional<DeviceParameters> parameters = parametersService.getParameters(device.getId());
		if (parameters.isPresent()) {
			device.setInfo(parameters.get());
		}
		return device;
	}

	@Override
	public List<Device> getDevicesWithoutInfo() {
		return deviceRepository.findAll();
	}

	@Override
	public boolean deviceIsRegistred(String id) {
		return deviceRepository.findById(id).isPresent();
	}

	@Override
	public boolean existDeviceWithName(String name) {
		return deviceRepository.findByName(name).isPresent();
	}
}
