package mybook.myshop.repository;

import lombok.RequiredArgsConstructor;
import mybook.myshop.domain.item.Item;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {
    private final EntityManager em;

    public void save(Item item) {
        // jpa 저장하기 전까지 id 값이 없으니.. (새로 생성한 객체라는 말 !)
        // 그래서 null 체크해서 신규로 등록하는 것
        if(item.getId() == null) {
            em.persist(item);
        } else { // 여긴 db에 이미 있을 경우에 ..! 나중에 추후에 제대로 설명
            em.merge(item);
        }
    }

    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }
}
