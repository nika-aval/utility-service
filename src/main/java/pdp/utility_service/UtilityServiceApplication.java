package pdp.utility_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableCaching
@SpringBootApplication
public class UtilityServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UtilityServiceApplication.class, args);
	}

}
