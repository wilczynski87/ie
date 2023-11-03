package dlarodziny.wolontariusze.ie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class IeApplication {

	public static void main(String[] args) {
		SpringApplication.run(IeApplication.class, args);
		log.info("\n\napp started\n");
	}

}
