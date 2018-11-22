package pl.uplukaszp.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import pl.uplukaszp.domain.Device;
import pl.uplukaszp.domain.dto.DeviceDTO;
import pl.uplukaszp.domain.projections.DeviceOnlyWithIdProjection;

@Service
public interface DeviceService {

	List<Device> getDevices();

	Optional<Device> getDevice(String id);

	Device addDevice(DeviceDTO device);

	boolean remove(String id);

	List<DeviceOnlyWithIdProjection> findNewLocalDevices();

	List<Device> getDevicesWithoutInfo();

	boolean deviceIsRegistred(String id);

	boolean existDeviceWithName(String name);
}
