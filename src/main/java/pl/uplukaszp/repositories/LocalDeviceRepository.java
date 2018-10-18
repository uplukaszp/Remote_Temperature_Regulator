package pl.uplukaszp.repositories;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import pl.uplukaszp.domain.Device;
import pl.uplukaszp.domain.DeviceParameters;
import pl.uplukaszp.domain.projections.DeviceOnlyWithIdProjection;

@Repository
public interface LocalDeviceRepository {

	List<DeviceOnlyWithIdProjection> findNewDevices();

	Map<String, DeviceParameters> getParameters(List<Device> devices);

	Optional<DeviceParameters> getParameters(String id);

	Optional<DeviceParameters> turnOn(String id);

	Optional<DeviceParameters> turnOff(String id);

	Optional<DeviceParameters> setTemperature(String id, Float temp);

}
