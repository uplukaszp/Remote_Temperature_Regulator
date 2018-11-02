package pl.uplukaszp.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import pl.uplukaszp.domain.Task;

public interface TaskRepository extends CrudRepository<Task, Long> {
	@Override
	List<Task> findAll();

	List<Task> findByDeviceId(String deviceId);
}
