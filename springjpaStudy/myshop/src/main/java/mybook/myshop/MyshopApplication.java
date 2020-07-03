package mybook.myshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MyshopApplication {

	public static void main(String[] args) {
		Test test = new Test();
		test.setData("hi");
		System.out.println(test.getData());
		SpringApplication.run(MyshopApplication.class, args);
	}

}
