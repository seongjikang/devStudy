package test.datatype;

import test.datatype.domain.Address;
import test.datatype.domain.Member;
import test.datatype.domain.Period;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class DataType2Main {
    public static void main(String args[]) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("datatype");
        EntityManager em = emf.createEntityManager();

        EntityTransaction et = em.getTransaction();
        et.begin();

        try {
            Address address = new Address("masan", "street", "12345");

            //member1과 2가 같은 address 를사용중
            //Member member1 = new Member();
            //member1.setUserName("kang");
            //member1.setHomeAddress(address);
            //em.persist(member1);

            //Member member2 = new Member();
            //member2.setUserName("park");
            //member2.setHomeAddress(address);
            //em.persist(member2);

            //첫번째 멤버만 ... 바꾸고 싶은 의도 ... 하지만 ...
            //db에서는 둘다 busan으로 바뀐다 ..
            //side effect라고 하지? 이런건 진짜 잡기가 힘들다 ..
            // 그래서 임베디드 타입을 여러 엔티티에서 공유하면 위험하다.
            //member1.getHomeAddress().setCity("busan");

            // 그러니깐 아래처럼 복사해서 넣어주는 방법을 사용하자.
            // 이렇게하면 공유참조로 인해서 발생하는 부작용은 피할수가 있음 ..
            // 참조를 복사하다보니 .. 참조값을 직접 대입해버리는것을 막을수는 없다..
            Member member1 = new Member();
            member1.setUserName("kang");
            member1.setHomeAddress(address);
            em.persist(member1);

            Address copyAddress = new Address(address.getCity(), address.getStreet(), address.getZipcode());

            Member member2 = new Member();
            member2.setUserName("park");
            member2.setHomeAddress(copyAddress);
            // 아래처럼 직접 대입되는것을 피할수가 없음 .. 조심하자..
            //member2.setHomeAddress(member1.getHomeAddress());
            em.persist(member2);

            // setter 주석처리하고 나면 .. 아래 의 코드가 코딩 단계에서 걸려지니깐 면역력이 생긴다고 볼수 있지
            //member1.getHomeAddress().setCity("busan");

            //불변객체..
            // 객체타입을 수정할수 없게 만들어버리자. 부작용 원천 차단해버린다.
            // 값타입은 불변객체로 설계 ! (Integer, String 등..)
            // 불변객체는 생성시점 이후 절대 값을 변경할수 없는 객체임 .. !
            // 결국 생성자로만 값을 설정하게 하고 , Setter를 만들지 않거나 private(내부에서는 쓸수도 있으니깐) 로 만드는 전략을 사용하면된다.
            // 근데 이렇게하면 ... 아무것도 값을 바꿀수가 없게됨 ..

            // 근데도 바꾸고싶다면 이렇게 새롭게 만들어주자.
            Address newAddress = new Address("NewCity", address.getStreet(), address.getZipcode());
            member1.setHomeAddress(newAddress);


            et.commit();
        } catch (Exception e) {

        } finally {
            em.close();
        }
        emf.close();
    }
}
