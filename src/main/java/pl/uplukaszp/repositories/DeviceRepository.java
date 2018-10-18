package pl.uplukaszp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.uplukaszp.domain.Device;

public interface DeviceRepository extends JpaRepository<Device, String>{
	
}
