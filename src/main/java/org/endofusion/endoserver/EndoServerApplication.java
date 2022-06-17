package org.endofusion.endoserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
public class EndoServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(EndoServerApplication.class, args);
	}
}
