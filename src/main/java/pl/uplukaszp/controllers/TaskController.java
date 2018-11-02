package pl.uplukaszp.controllers;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pl.uplukaszp.domain.Device;
import pl.uplukaszp.domain.Task;
import pl.uplukaszp.domain.dto.TaskDTO;
import pl.uplukaszp.services.DeviceService;
import pl.uplukaszp.services.TaskService;

@RestController
@RequestMapping("/device/{deviceId}/control/schedule/")
public class TaskController {

	private TaskService taskService;

	public TaskController(TaskService taskService, DeviceService deviceService) {
		this.taskService = taskService;
	}

	@PostMapping
	public ResponseEntity<Task> schedule(@PathVariable String deviceId, @RequestBody @Valid TaskDTO task) {
		Optional<Task> newTask = taskService.createTask(task, deviceId);
		if (newTask.isPresent())
			return new ResponseEntity<>(newTask.get(), HttpStatus.CREATED);
		else
			return ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{taskId}")
	public ResponseEntity<Object> deleteSchedule(@PathVariable Long taskId) {

		if (taskService.remove(taskId))
			return ResponseEntity.noContent().build();
		else
			return ResponseEntity.notFound().build();

	}

	@GetMapping
	public ResponseEntity<List<Task>> getTasks(@PathVariable String deviceId) {
		return new ResponseEntity<>(taskService.getTasks(deviceId), HttpStatus.OK);
	}

	@GetMapping("/{taskId}")
	public ResponseEntity<Task> getTaskById(@PathVariable Long taskId) {
		Optional<Task> task = taskService.getTask(taskId);
		if (task.isPresent())
			return new ResponseEntity<>(task.get(), HttpStatus.OK);
		else
			return ResponseEntity.notFound().build();
	}
}
