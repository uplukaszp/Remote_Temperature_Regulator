package pl.uplukaszp.components;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pl.uplukaszp.domain.Task;
import pl.uplukaszp.domain.TaskType;
import pl.uplukaszp.repositories.MqttRepository;
import pl.uplukaszp.services.TaskService;

@Component
@DisallowConcurrentExecution
public class TaskToExecute implements Job {

	@Autowired
	MqttRepository repository;
	@Autowired
	TaskService taskService;
	@Autowired
	Scheduler scheduler;
	@Autowired
	TaskPlanner planner;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		Long taskId = (Long) context.getJobDetail().getJobDataMap().get("task");
		Task t = taskService.getTask(taskId).get();
		System.out.println(t.toString());
		switch (t.getStateToSchedule()) {
		case auto:
		case on:
			repository.setTemperature(t.getDevice().getId(), t.getTemperatureToSchedule());
			break;
		case off:
			repository.turnOff(t.getDevice().getId());
		default:
			break;
		}
		if (t.getType().equals(TaskType.single)) {
			taskService.remove(t.getId());
		}
		try {
			scheduler.deleteJob(context.getJobDetail().getKey());
			
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		planner.planNextTasks();
	}

}
