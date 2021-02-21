package mybook.myshop.service;

import mybook.myshop.domain.item.Book;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ItemUpdateTest {
    @Autowired
    EntityManager em;

    @Test
    public void updateTest() throws Exception {
        Book book = em.find(Book.class, 1L);

        //TX
        book.setName("babo");

        //변경 감지 == dirty checking
        // 영속성 컨텍스트가 관리하는 애들은 flush 일어날때 변경이 db에서 되도록 JPA 가 알아서 해줌

        //TX Commit

    }
}
