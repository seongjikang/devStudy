package study.datajpa; //이 패키지 하위는 전부다 끌어올수 있음

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication //@EnableJpaRepositories(basePackages = "study.datajpa.repository") // Spring Boot 에서는 필요없는 어노테이션
public class DataJpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataJpaApplication.class, args);
	}

}
