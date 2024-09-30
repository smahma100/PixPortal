package com.saberdot.photoapp.api.pixportalconfigserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class PixPortalConfigServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(PixPortalConfigServerApplication.class, args);
	}

}
