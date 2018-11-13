package pl.uplukaszp.components;

import java.text.ParseException;
import java.util.List;

import org.quartz.CronExpression;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.stereotype.Component;

import pl.uplukaszp.domain.Task;
import pl.uplukaszp.repositories.TaskRepository;

@Component
public class TaskPlanner {
	private TaskRepository taskRepository;
	private Scheduler scheduler;

	public TaskPlanner(TaskRepository taskRepository, Scheduler scheduler) {
		super();
		this.taskRepository = taskRepository;
		this.scheduler = scheduler;
	}
	
	public void planNextTasks() {
		List<Task> tasks = taskRepository.findNearestTasks();
		if (!tasks.isEmpty()) {
			try {
				JobDetail job = JobBuilder.newJob(TaskToExecute.class).withIdentity(tasks.get(0).getId().toString())
						.build();
				job.getJobDataMap().put("task", tasks.get(0).getId());
				CronExpression cronExpression = createCron(tasks.get(0));
				Trigger trigger = TriggerBuilder.newTrigger().withIdentity(tasks.get(0).getId().toString())
						.withSchedule(CronScheduleBuilder.cronSchedule(cronExpression)).build();
				scheduler.start();
				scheduler.scheduleJob(job, trigger);
			} catch (SchedulerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private CronExpression createCron(Task t) throws ParseException {
		StringBuilder expression = new StringBuilder();
		expression.append("0 ");
		expression.append(t.getTime().getMinute() + " ");
		expression.append(t.getTime().getHour() + " ");
		expression.append("? ");
		expression.append("* ");
		expression.append(t.getDayOfWeek().name().substring(0, 3));
		expression.append(" *");
		System.out.println(expression.toString());
		CronExpression exp = new CronExpression(expression.toString());
		return exp;
	}

}
