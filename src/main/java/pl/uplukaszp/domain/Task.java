package pl.uplukaszp.domain;

import java.time.DayOfWeek;
import java.time.LocalTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;
import lombok.ToString;

@Data
@Entity(name = "TASK")
@ToString(exclude = { "device" })
public class Task {
	@Id
	@GeneratedValue
	Long id;

	private DayOfWeek dayOfWeek;
	private LocalTime time;
	private DeviceState stateToSchedule;
	private Float temperatureToSchedule;
	private TaskType type;
	@ManyToOne(optional = false)
	@JsonBackReference
	private Device device;

}
