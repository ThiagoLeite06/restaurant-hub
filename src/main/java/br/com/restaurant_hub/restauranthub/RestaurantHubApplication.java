package br.com.restaurant_hub.restauranthub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "br.com.restaurant_hub.restauranthub")
public class RestaurantHubApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestaurantHubApplication.class, args);
	}

}
