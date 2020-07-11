package mybook.myshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// 이 패키지 하위에 있는 빈들을 전부 등록
@SpringBootApplication
public class MyshopApplication {

	public static void main(String[] args) {
//		Test test = new Test();
//		test.setData("hi");
//		System.out.println(test.getData());
		SpringApplication.run(MyshopApplication.class, args);
	}

}
