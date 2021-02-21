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
            //이 merge가 뭐냐면 ..!
            // 여기 들어오는 파라미터에 들어온 item이
            // 다 바꿔치기 해버림..!
            em.merge(item);
            //merge 호출하면
            //파라미터로 넘어온 member를 영소성 컨텍스트의 1차 캐시에서 찾고 , 없으면 db에서 조회하고 1차 캐쉬에 저장
            // 조회한 영속 엔티티(merge 하고나면 리턴되는 애)\에 파라미터로 넘어온 엔티티 값을 채워서 넣음 (이렇게 엔티티 값이 변경되는거임)
            // 그러고 나서 영속상태면 그 엔티티 값을 반환함

            // 그런데 병합은 되게 ㅗㅈ심해서 사용해야함 ... 변경감지기능은 원하는 속성만 선택해서 변경할수있는데
            // 병합을 사용하면 모든데이터 속성이 다변경됨 ... 병합시 값이 없으면 .. null 로 업데이트해버릴 위험이 있으니 조심해서 써야함
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
