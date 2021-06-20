package study.datajpa.repository;

import org.springframework.stereotype.Repository;
import study.datajpa.entity.Member;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class MemberJpaRepository {
    @PersistenceContext
    private EntityManager em;

    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    public void delete(Member member) {
        em.remove(member);
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public Optional<Member> findById(Long id) {
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    public long count() {
        return em.createQuery("select count(m) from Member m", Long.class)
                .getSingleResult();
    }

    public Member find(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findByUserNameAndAgeGreaterThan(String userName, int age) {
        return em.createQuery("select m from Member m where m.userName = :userName and m.age > :age")
                .setParameter("userName", userName)
                .setParameter("age", age)
                .getResultList();
    }

    //NamedQuery 예제
    public List<Member> findByUserName(String userName) {
        return em.createNamedQuery("Member.findByUserName", Member.class)
                .setParameter("userName",userName)
                .getResultList();
    }

    //순수 JPA 페이징&정렬 기능
    public List<Member> findByPage(int age, int offset, int limit) {
        return em.createQuery("select m from Member m where m.age = :age order by m.userName desc")
                .setParameter("age", age)
                .setFirstResult(offset) // 어디서(몇번째) 부터 가져올 것인지?
                .setMaxResults(limit) // 갯수를 몇개까지 가져올 것인지?
                .getResultList();
    }

    public long totalCount(int age) {
        return em.createQuery("select count(m) from Member m where m.age = :age", Long.class)
                .setParameter("age", age)
                .getSingleResult();
    }

    //순수 jpa 벌크성 쿼리
    public int bulkAgePlus(int age) {
        return em.createQuery(
                "update Member m set m.age = m.age + 1" +
                        "where m.age >= :age")
                .setParameter("age", age)
                .executeUpdate();
    }
}
