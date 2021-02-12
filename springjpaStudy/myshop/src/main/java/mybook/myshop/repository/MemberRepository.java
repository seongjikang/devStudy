package mybook.myshop.repository;

import lombok.RequiredArgsConstructor;
import mybook.myshop.domain.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

// Component Scan 으로 인해 자동으로 등록됨
//스프링 빈으로 등록
@Repository
@RequiredArgsConstructor
public class MemberRepository {

    //얘는 Spring 이 엔티티 매니저를 주입해줌
    // 예전에 팩토리 만들어서 엔티티 매니저 막 직접 꺼내서 하던 과정을 안해도됨
    //@PersistenceContext

   // Spring data jpa 쓰면 @PersistenceContext 말고, @Autowired 쓸수 있음
    //@Autowired

    // 그래서 이것도 마찬가지로... 생성자 injection 쓰면 참좋음
    private final EntityManager em;

    public void save(Member member) {
        em.persist(member);
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        // 엔티티 객체를 대상을 쿼리를 날림 ..!
        //sql 이랑 거의 비슷하지만 차이는 sql은 테이블을, jpql은 엔티티를 조회함
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}
