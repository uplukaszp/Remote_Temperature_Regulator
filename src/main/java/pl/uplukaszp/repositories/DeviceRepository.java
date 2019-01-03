package pl.uplukaszp.repositories;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import pl.uplukaszp.domain.Device;

public interface DeviceRepository extends JpaRepository<Device, String> {

	Optional<Device> findByName(String name);

	@Query("select d.id from Device d")
	Set<String> findAllIds();
}
