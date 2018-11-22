package pl.uplukaszp.controllers;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.uplukaszp.domain.Device;
import pl.uplukaszp.domain.dto.DeviceDTO;
import pl.uplukaszp.domain.projections.DeviceOnlyWithIdProjection;
import pl.uplukaszp.services.DeviceService;

@RestController
@RequestMapping("/device")
public class DeviceController {

	private DeviceService deviceService;

	public DeviceController(DeviceService deviceService) {
		this.deviceService = deviceService;
	}

	@GetMapping
	public ResponseEntity<List<Device>> getDevices() {
		List<Device> body = deviceService.getDevices();
		return new ResponseEntity<List<Device>>(body, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Device> getDeviceById(@PathVariable String id) {
		Optional<Device> device = deviceService.getDevice(id);
		if (device.isPresent()) {
			return new ResponseEntity<Device>(device.get(), HttpStatus.OK);
		}
		return ResponseEntity.notFound().build();
	}

	@PostMapping()
	public ResponseEntity<Device> addDevice(@RequestBody @Valid DeviceDTO device, Errors errors) {
		if (errors.hasErrors()) {
			return ResponseEntity.badRequest().build();
		}
		if (deviceService.deviceIsRegistred(device.getId())||deviceService.existDeviceWithName(device.getName())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
		Device newDevice = deviceService.addDevice(device);
		return new ResponseEntity<Device>(newDevice, HttpStatus.CREATED);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Object> removeDevice(@PathVariable(required = true) String id) {
		if (deviceService.remove(id))
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		else
			return ResponseEntity.notFound().build();
	}

	@GetMapping("/find")
	public ResponseEntity<List<DeviceOnlyWithIdProjection>> findDevices() {
		List<DeviceOnlyWithIdProjection> newDevices = deviceService.findNewLocalDevices();
		return new ResponseEntity<List<DeviceOnlyWithIdProjection>>(newDevices, HttpStatus.OK);
	}
}
