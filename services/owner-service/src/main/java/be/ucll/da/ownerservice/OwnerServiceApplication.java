package be.ucll.da.ownerservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class OwnerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OwnerServiceApplication.class, args);
	}

}
