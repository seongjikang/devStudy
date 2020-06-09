package test.myteam;

import org.hibernate.Hibernate;
import test.myteam.domain.Member;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

//프록시 객체는 처음사용할때 한번만 초기화
//프록시 객체를 초기활 할때 프록시 객체가 실제 엔티티로 바뀌는게 아니다. 초기화 되면 프록시 객체를 통해 실제 엔티티에 접근하는 개념임.
//프록시 객체는 엔티티 상속받음 , 타입 비교할 때, == 비교 는 안된다 ! instance of 사용하자  (습관화 해놓자)!
//영속성 컨텍스트에 찾는 엔티티가 이미 있으면 em.getReference()를 호출해도 실제 엔티티가 반환됨 !
// 먼저 getReference 하고 find 해도 find할때 프록시가 반환됨 .. ! 왜냐면 == 비교를 jpa에서 맞춰주기 위해서..!
// 준영속상태일 때 (em.close나 em.detach 나 em.clear 가 호출된 상태) 프록시 초기화 요청을 하면 .. 에러가 발생한다. (실무에서 진짜 많이 만날 에러임 ..!)
// 프록시는 많이쓰진 않지만 지연로딩을 하기 위해서 잘 알아야하는 개념임
public class ProxyMain {
    public static void main(String args[]) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("relatedmapping");
        EntityManager em = emf.createEntityManager();

        EntityTransaction et = em.getTransaction();
        et.begin();

        try {
            // 만약에 Member 를 가져올 때 ... Team을 항상 가져올 필요가 있을까??
            // 어느경우에는 팀도 같이, 어느경우에는 멤버만 ... 비지니스 로직에 따라 다르게 가져와야 한다.
            // 이걸 해결하려면 프록시를 명확히 이해해야함.
            //em.find ? em.getReference?
            Member member = new Member();
            member.setUserName("itbanker");

            em.persist(member);

            em.flush();
            em.clear();

//            Member findMember = em.find(Member.class, member.getId());

            //데이터 베이스 조회를 미루는 가짜 엔티티 객체(껍데기)를 조회한다.
            //실제 엔티티를 상속받은 프록시 객체가 만들어짐
            //이론 상으로는 사용할때 실제 객체인지 프록시 객체인지 신경쓰지 않고 사용하면 됨
            Member findMember = em.getReference(Member.class, member.getId());

            System.out.println(findMember.getId());
            //프록시 인스턴스의 초기화 여부 확인
            System.out.println(emf.getPersistenceUnitUtil().isLoaded(findMember));

            //프록시 강제 초기화 하려면 ..
            //Hibernate.initialize(findMember);

            //getReference로 Member를 가져오면 ,
            //이게 db에 있는데, 실제로 이걸 사용하려고 하면 jpa가 쿼리를 날려서 가져오게함.
            System.out.println(findMember.getUserName());
            System.out.println(emf.getPersistenceUnitUtil().isLoaded(findMember));


            et.commit();
        } catch (Exception e) {

        } finally {
            em.close();
        }
        emf.close();
    }
}
