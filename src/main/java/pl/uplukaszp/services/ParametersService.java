package pl.uplukaszp.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import pl.uplukaszp.domain.Device;
import pl.uplukaszp.domain.DeviceParameters;

@Service
public interface ParametersService {

	public Optional<DeviceParameters> turnOn(String id);

	public Optional<DeviceParameters> turnOff(String id);

	public Optional<DeviceParameters> getParameters(String id);

	public Map<String, DeviceParameters> getParameters(List<Device> devices);

	public Map<String, DeviceParameters> getParameters();

	Optional<DeviceParameters> setTemperature(String id, Float temp);

}
