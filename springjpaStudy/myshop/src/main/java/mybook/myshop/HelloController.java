package mybook.myshop;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

	@GetMapping("/hello")
	public String hello(Model model) {
		model.addAttribute("data","hello");
		// 여기 리턴은 관례상 뷰이름
		return "hello";
	}
}
