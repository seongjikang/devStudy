package test.myteam;

import test.myteam.domain.Child;
import test.myteam.domain.Parent;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class CascadeMain {
    public static void main(String args[]) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("relatedmapping");
        EntityManager em = emf.createEntityManager();

        EntityTransaction et = em.getTransaction();
        et.begin();

        try {
            Child child1 = new Child();
            Child child2 = new Child();

            Parent parent = new Parent();
            parent.addChild(child1);
            parent.addChild(child2);

            em.persist(parent);

            // 만약 아래의 child 들을 persist 해주고 싶지 않으면 .. Parent에 cascade 추가해주자.
            //em.persist(child1);
            //em.persist(child2);

            em.flush();
            em.clear();

            Parent findParent= em.find(Parent.class, parent.getId());

            // 만약 이렇게 연관관계가 끊긴 경우 .. !
            // orphanRemoval = true 가 설정되어있으니 ... 컬렉션에서 빠진 놈은 삭제가 된다 ! db에서 !!
            findParent.getChildList().remove(0);

            // 개념적으로 봤을때 부모 제거시 자식은  고아가 됨
            // 만약에 Parent 클래스에서 cascade = CascadeType.ALL 이 없으면 ... 자식도 삭제되어 버림 !
            //em.remove(findParent);

            et.commit();
        } catch (Exception e) {

        } finally {
            em.close();
        }
        emf.close();
    }
}
