package pl.uplukaszp.repositories.mocks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.stereotype.Repository;

import pl.uplukaszp.domain.Device;
import pl.uplukaszp.domain.DeviceParameters;
import pl.uplukaszp.domain.DeviceState;
import pl.uplukaszp.domain.dto.DeviceDTO;
import pl.uplukaszp.domain.projections.DeviceOnlyWithIdProjection;
import pl.uplukaszp.repositories.LocalDeviceRepository;

@Repository
@Profile("development")
public class LocalDeviceRepositoryMockImpl implements LocalDeviceRepository {

	Map<String, Device> repository;
	ProjectionFactory factory;

	public LocalDeviceRepositoryMockImpl(ProjectionFactory factory) {

		this.factory = factory;
		repository = new HashMap<>();
	}

	@Override
	public List<DeviceOnlyWithIdProjection> findNewDevices() {
		List<DeviceOnlyWithIdProjection> devices = new ArrayList<>();
		DeviceOnlyWithIdProjection d1 = factory.createProjection(DeviceOnlyWithIdProjection.class);
		DeviceOnlyWithIdProjection d2 = factory.createProjection(DeviceOnlyWithIdProjection.class);
		d1.setId("client-" + (int) (Math.random() * 100.0));
		d2.setId("client-" + (int) (Math.random() * 100.0));
		devices.add(d1);
		devices.add(d2);

		return devices;
	}

	@Override
	public Map<String, DeviceParameters> getParameters(List<Device> devices) {
		Map<String, DeviceParameters> map = new HashMap<>();
		for (Device device : devices) {
			if (repository.containsKey(device.getId())) {
				map.put(device.getId(), repository.get(device.getId()).getInfo());
			}
		}
		return map;
	}

	@Override
	public Optional<DeviceParameters> getParameters(String id) {
		if (repository.containsKey(id))
			return Optional.of(repository.get(id).getInfo());
		else
			return Optional.empty();
	}

	
	@Override
	public Optional<DeviceParameters> setTemperature(String id, Float temp) {
		Device d = repository.get(id);
		DeviceParameters param = d.getInfo();
		param.setSavedTemperature(temp);
		param.setState(DeviceState.auto);
		d.setInfo(param);
		repository.put(id, d);
		return Optional.of(param);
	}

	@Override
	public Optional<DeviceParameters> turnOn(String id) {
		Device d = repository.get(id);
		DeviceParameters param = d.getInfo();
		param.setState(DeviceState.on);
		d.setInfo(param);
		repository.put(id, d);
		return Optional.of(param);
	}

	@Override
	public Optional<DeviceParameters> turnOff(String id) {
		Device d = repository.get(id);
		DeviceParameters param = d.getInfo();
		param.setState(DeviceState.off);
		d.setInfo(param);
		repository.put(id, d);
		return Optional.of(param);
	}

	public void addDev(DeviceDTO device) {
		Device d = new Device();
		d.setId(device.getId());
		d.setName(device.getName());
		DeviceParameters params = new DeviceParameters();
		params.setCurrentTemperature((float) (Math.random() * 100));
		params.setSavedTemperature(0f);
		params.setState(DeviceState.off);
		d.setInfo(params);
		repository.put(d.getId(), d);
	}
}
