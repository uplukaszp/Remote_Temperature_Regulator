package pl.uplukaszp.services.impl.mocks;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import pl.uplukaszp.domain.Device;
import pl.uplukaszp.domain.dto.DeviceDTO;
import pl.uplukaszp.repositories.DeviceRepository;
import pl.uplukaszp.repositories.LocalDeviceRepository;
import pl.uplukaszp.repositories.mocks.LocalDeviceRepositoryMockImpl;
import pl.uplukaszp.services.ParametersService;
import pl.uplukaszp.services.impl.DeviceServiceImpl;

@Service
@Profile("development")
public class DeviceServiceMockImpl extends DeviceServiceImpl {

	private LocalDeviceRepositoryMockImpl mockLocalDeviceRepository;
	public DeviceServiceMockImpl(DeviceRepository deviceRepository, LocalDeviceRepository localDeviceRepository,
			ParametersService parametersService,LocalDeviceRepositoryMockImpl mockLocalDeviceRepository) {
		super(deviceRepository, localDeviceRepository, parametersService);
		this.mockLocalDeviceRepository=mockLocalDeviceRepository;
	}
	@Override
	public Device addDevice(DeviceDTO device) {
		mockLocalDeviceRepository.addDev(device);
		Device d=super.addDevice(device);
		
		return d;
	}
}
