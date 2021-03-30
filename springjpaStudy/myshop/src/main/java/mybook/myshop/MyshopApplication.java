package mybook.myshop;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

// 이 패키지랑 이 패키지 하위에 있는 빈들을 Component Scan해서 전부 Spring bean으로 자동 등록
@SpringBootApplication
public class MyshopApplication {

	public static void main(String[] args) {
//		Test test = new Test();
//		test.setData("hi");
//		System.out.println(test.getData());
		SpringApplication.run(MyshopApplication.class, args);
	}

	//라이브러리 등록 ..
	// 권장사항 아님 ...
	// 성능상 굉장한 문제가 됨 ..
	// 그리고 .. 엔티티를 직접 노출하는 문제도 있다
	@Bean
	Hibernate5Module hibernate5Module() {
		Hibernate5Module hibernate5Module = new Hibernate5Module();

		// 아래는 .. 문제가 있다 .. 엔티티를 직접 노출하는 ...
		//hibernate5Module.configure(Hibernate5Module.Feature.FORCE_LAZY_LOADING, true);
		return hibernate5Module;
	}

}
