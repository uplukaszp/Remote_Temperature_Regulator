package pl.uplukaszp.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import pl.uplukaszp.domain.Task;
import pl.uplukaszp.domain.dto.TaskDTO;

@Service
public interface TaskService {

	boolean remove(Long taskId);

	Optional<Task> createTask(TaskDTO newTask, String deviceId);

	Optional<Task> getTask(Long taskId);

	List<Task> getTasks(String deviceId);

}
