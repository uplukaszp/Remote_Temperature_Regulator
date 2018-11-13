package pl.uplukaszp.domain.dto;

import java.time.DayOfWeek;
import java.time.LocalTime;



import lombok.Data;
import pl.uplukaszp.domain.DeviceState;
import pl.uplukaszp.domain.TaskType;

@Data
public class TaskDTO {
	private DayOfWeek dayOfWeek;
	private LocalTime time;
	private DeviceState stateToSchedule;
	private TaskType type;
	private Float temperatureToSchedule;
}
