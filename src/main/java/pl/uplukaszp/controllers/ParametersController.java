package pl.uplukaszp.controllers;

import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.uplukaszp.domain.DeviceParameters;
import pl.uplukaszp.domain.dto.TemperatureDTO;
import pl.uplukaszp.services.ParametersService;

@RestController
@RequestMapping("/device")
public class ParametersController {

	private ParametersService parametersService;

	public ParametersController(ParametersService parametersService) {
		this.parametersService = parametersService;
	}

	@PutMapping("/{id}/control/on")
	public ResponseEntity<DeviceParameters> turnOn(@PathVariable String id) {
		Optional<DeviceParameters> params = parametersService.turnOn(id);
		return createResponse(params);

	}

	@PutMapping("/{id}/control/off")
	public ResponseEntity<DeviceParameters> turnOff(@PathVariable String id) {
		Optional<DeviceParameters> params = parametersService.turnOff(id);
		return createResponse(params);
	}
	@PutMapping("/{id}/control/auto")
	public ResponseEntity<DeviceParameters> turnOnAuto(@PathVariable String id,@RequestBody @Valid TemperatureDTO temperature) {
		Optional<DeviceParameters> params = parametersService.setTemperature(id, temperature.getTemperature());
		return createResponse(params);
	}

	@GetMapping("/{id}/control/parameters")
	public ResponseEntity<DeviceParameters> getParametersById(@PathVariable String id) {
		Optional<DeviceParameters> params = parametersService.getParameters(id);
		return createResponse(params);
	}

	@GetMapping("/control/parameters")
	public ResponseEntity<Map<String, DeviceParameters>> getParameters() {
		Map<String, DeviceParameters> params = parametersService.getParameters();
		return new ResponseEntity<Map<String, DeviceParameters>>(params, HttpStatus.OK);
	}

	private ResponseEntity<DeviceParameters> createResponse(Optional<DeviceParameters> params) {
		if (params.isPresent())
			return new ResponseEntity<DeviceParameters>(params.get(), HttpStatus.OK);
		else
			return ResponseEntity.notFound().build();
	}

}
