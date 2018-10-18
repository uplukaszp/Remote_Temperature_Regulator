package pl.uplukaszp.services.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import pl.uplukaszp.domain.Device;
import pl.uplukaszp.domain.DeviceParameters;
import pl.uplukaszp.domain.dto.DeviceDTO;
import pl.uplukaszp.domain.projections.DeviceOnlyWithIdProjection;
import pl.uplukaszp.repositories.DeviceRepository;
import pl.uplukaszp.repositories.LocalDeviceRepository;
import pl.uplukaszp.services.DeviceService;
import pl.uplukaszp.services.ParametersService;

@Service
@Profile("!development")
public class DeviceServiceImpl implements DeviceService {
	DeviceRepository deviceRepository;
	LocalDeviceRepository localDeviceRepository;
	ParametersService parametersService;

	public DeviceServiceImpl(DeviceRepository deviceRepository, LocalDeviceRepository localDeviceRepository,
			ParametersService parametersService) {
		this.deviceRepository = deviceRepository;
		this.localDeviceRepository = localDeviceRepository;
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
		return localDeviceRepository.findNewDevices();
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
}
