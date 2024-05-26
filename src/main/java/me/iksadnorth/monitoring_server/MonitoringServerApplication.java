package me.iksadnorth.monitoring_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class MonitoringServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MonitoringServerApplication.class, args);
	}

}
