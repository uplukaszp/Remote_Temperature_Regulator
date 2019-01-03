package pl.uplukaszp;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RemoteTemperatureRegulatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(RemoteTemperatureRegulatorApplication.class, args);
	}
}
