package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import study.datajpa.entity.Item;

@SpringBootTest
public class ItemRepositoryTest {

	@Autowired ItemRepository itemRepository;

	@Test
	public void save() {
		//Item item = new Item(); // 새것이라 판단해서 persist 되는 경우
		Item item = new Item("A"); // 새것이 아니라 판단해서 merge가 되는 경우, 데이터의 변경은 변경 감지 기능을 쓰는게 옳다
		//여기서 우리가 ... merge이 기능을 안쓰려면 ...? //  Persistable을 쓰면됨 Item 객체에다가
		itemRepository.save(item);
	}
}
