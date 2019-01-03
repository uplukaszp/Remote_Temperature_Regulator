package pl.uplukaszp.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;

@Data
@Entity
public class Device {
	@Id
	@Column(nullable = false, unique = true)
	private String id;
	@Column(nullable = false, unique = true)
	private String name;
	@OneToMany
	@JoinColumn(name = "DEVICE_ID")
	@JsonManagedReference
	private List<Task> tasks;
	@Transient
	private DeviceParameters info;
}
