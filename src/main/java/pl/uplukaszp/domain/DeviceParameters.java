package pl.uplukaszp.domain;

import lombok.Data;

@Data
public class DeviceParameters {

	private DeviceState state;
	private Float currentTemperature;
	private Float savedTemperature;
}
