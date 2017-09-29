package filehub.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.session.SessionAutoConfiguration;

@SpringBootApplication(exclude = {SessionAutoConfiguration.class})
public class FileHubApplication {

	public static void main(String[] args) {
		//SpringApplication.run(FileHubApplication.class, new String[]{"--debug"});
		SpringApplication.run(FileHubApplication.class, args);
	}


}
