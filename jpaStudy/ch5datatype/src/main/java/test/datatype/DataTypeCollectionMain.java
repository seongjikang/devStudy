package test.datatype;

import test.datatype.domain.Address;
import test.datatype.domain.AddressEntity;
import test.datatype.domain.Member;
import test.datatype.domain.Period;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;
import java.util.Set;

public class DataTypeCollectionMain {
    public static void main(String args[]) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("datatype");
        EntityManager em = emf.createEntityManager();

        EntityTransaction et = em.getTransaction();
        et.begin();

        try {

            Member member = new Member();
            member.setUserName("member1");
            member.setHomeAddress(new Address("city1", "street1", "1234"));

            member.getFavoriteFoods().add("라면");
            member.getFavoriteFoods().add("국수");
            member.getFavoriteFoods().add("파스타");

            //member.getAddressHistory().add(new Address("old1", "street1", "1234"));
            //member.getAddressHistory().add(new Address("old2", "street1", "1234"));
            member.getAddressHistory().add(new AddressEntity("old1", "street1", "1234"));
            member.getAddressHistory().add(new AddressEntity("old1", "street1", "1234"));

            // 값타입 컬렉션은 영속성 전이 + 고아객체 제거 기능을 필수로 가짐
            em.persist(member);

            em.flush();
            em.clear();

            System.out.println("----------------------------------------------");
            Member findMember = em.find(Member.class, member.getId());

            //기본적으로 값타입 컬렉션은 조회시 지연로딩 전략을 사용함.
            //List<Address> addressHistory = findMember.getAddressHistory();
//            for(Address address : addressHistory) {
//                System.out.println(address.getCity());
//            }

            Set<String> favoriteFoods = findMember.getFavoriteFoods();
            for(String food : favoriteFoods) {
                System.out.println(food);
            }

            //수정시 ... 주의할것.. 사이드 이팩트 발생할수도 있으니 ..
            //findMember.getHomeAddress().setCity("newCity"); --> 이렇게 하면 업데이트 문은 날라가겟지만 ... 어떤 사이드 이팩트가 발생할지 모른다.
            //아래처럼 하자. (완전히 교체해주기 !!)
            Address a = findMember.getHomeAddress();
            findMember.setHomeAddress(new Address("newCity", a.getStreet(), a.getZipcode()));

            // 국수를 막국수로 바꾸기 ~~!!
            findMember.getFavoriteFoods().remove("국수");
            findMember.getFavoriteFoods().add("막국수");

            //여기서 equals를 잘 정의해놓는게 참 중요해진다 !
            findMember.getAddressHistory().remove(new Address("old1", "street1", "1234"));
            findMember.getAddressHistory().remove(new Address("newCity1", "street1", "1234"));

            //값타입컬렉션에 변경사항이 발생하면 주인 엔티티와 연관된 모든데이터를 삭제하고 값타입 컬렉션에 있는 현재값을 모두 다시 저장함..
            //업데이트 해주려면 ..OrderColumn 을 쓰면 되....지 않음 ..!! 결론은 .. 이렇게 복잡하게 할거면 완전히 다르게 풀어줘야함..!
            // 실무에서는 결국 값타입 컬렉션대신 일대다 관계를 만들어주는게 더 낫다 ..
            et.commit();
        } catch (Exception e) {
            et.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
        emf.close();
    }
}
