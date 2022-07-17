package org.endofusion.endoserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.io.File;
import static org.endofusion.endoserver.constant.FileConstant.USER_FOLDER;

@SpringBootApplication
public class EndoServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(EndoServerApplication.class, args);
		new File(USER_FOLDER).mkdirs();
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
