package pl.uplukaszp.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.uplukaszp.domain.Device;

public interface DeviceRepository extends JpaRepository<Device, String>{

	Optional<Device> findByName(String name);
	
}
