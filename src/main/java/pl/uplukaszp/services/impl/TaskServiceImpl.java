package pl.uplukaszp.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import pl.uplukaszp.domain.Device;
import pl.uplukaszp.domain.Task;
import pl.uplukaszp.domain.dto.TaskDTO;
import pl.uplukaszp.repositories.TaskRepository;
import pl.uplukaszp.services.DeviceService;
import pl.uplukaszp.services.TaskService;

@Service
public class TaskServiceImpl implements TaskService {

	private TaskRepository taskRepository;
	private DeviceService deviceService;

	public TaskServiceImpl(TaskRepository taskRepository, DeviceService deviceService) {
		this.taskRepository = taskRepository;
		this.deviceService = deviceService;
		
	}

	@Override
	public boolean remove(Long taskId) {
		Optional<Task> task = taskRepository.findById(taskId);
		if (task.isPresent()) {
			taskRepository.delete(task.get());
			return true;
		}
		return false;
	}

	@Override
	public Optional<Task> createTask(TaskDTO newTask,String deviceId) {
		Task task = new Task();
		Optional<Device> d = deviceService.getDevice(deviceId);
		if (d.isPresent()) {
			Device device = d.get();
			task.setDevice(device);
			task.setDayOfWeek(newTask.getDayOfWeek());
			task.setStateToSchedule(newTask.getStateToSchedule());
			task.setTime(newTask.getTime());
			task.setType(newTask.getType());
			task.setTemperatureToSchedule(newTask.getTemperatureToSchedule());
			task = taskRepository.save(task);
			return Optional.of(task);
		}
		return Optional.empty();
	}

	@Override
	public Optional<Task> getTask(Long taskId) {
		return taskRepository.findById(taskId);
	}

	@Override
	public List<Task> getTasks(String deviceId) {
		return taskRepository.findByDeviceId(deviceId);
	}

}
