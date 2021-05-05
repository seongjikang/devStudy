package mybook.myshop.repository;

import lombok.RequiredArgsConstructor;
import mybook.myshop.domain.Order;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {
    private final EntityManager em;

    public void save(Order order){
        em.persist(order);
    }

    public Order findOne(Long id) {
       return em.find(Order.class, id);
    }

    public List<Order> findAllByString(OrderSearch orderSearch) {

        // 여기서 paging 처리도 가능함
        // 여기서 동적 쿼리를 어떻게 ...?
        // mybatis는 동적 쿼리 작성시 xml 을 줌
//        return em.createQuery("select o from Order o join o.member m" +
//                " where o.status = :status " +
//                " and m.name like :name", Order.class)
//                .setParameter("status", orderSearch.getOrderStatus())
//                .setParameter("name", orderSearch.getMemberName())
//                .setMaxResults(1000)
//                .getResultList();

        // 1. 복잡하게 자바 로직으로 구현하기 (실무에서 이렇게 안씀)
        // 일단 버그를 찾기 힘들다
        String jpql = "select o from Order o join o.member m";
        boolean isFirstCondition = true;

        // 주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " o.status = :status";
        }

        // 회원 이름 검색
        if(StringUtils.hasText(orderSearch.getMemberName())) {
            if(isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " m.name like :name";
        }

        TypedQuery<Order> query = em.createQuery(jpql, Order.class)
                .setMaxResults(1000);

        if(orderSearch.getOrderStatus() != null) {
            query = query.setParameter("status", orderSearch.getOrderStatus());
        }
        if(StringUtils.hasText(orderSearch.getMemberName())) {
            query = query.setParameter("name", orderSearch.getMemberName());
        }

        return query.getResultList();
    }

    public List<Order> findAllByCriteria(OrderSearch orderSearch) {
        //2.  jpql 을 java 코드로 작성하느 표준방법
        // 이방법도 권장하는 방법아님
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> o = cq.from(Order.class);
        Join<Object, Object> m = o.join("member", JoinType.INNER);

        List<Predicate> criteria = new ArrayList<>();

        //주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            Predicate status = cb.equal(o.get("status"), orderSearch.getOrderStatus());
            criteria.add(status);
        }

        //회원이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            Predicate name =
                    cb.like(m.<String>get("name"), "%" + orderSearch.getMemberName() + "%");
            criteria.add(name);
        }

        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
        TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000);
        return query.getResultList();
    }

    //리얼 실무 코드는 QueryDSL .. !
    //QueryDSL 로 쓰면됨

    //fetch join
    // 이정도 ... Entity 조회하는 거 정도는 괜찮음 ...
    public List<Order> findAllWithMemberDelivery() {
        // 이것도 물론 단점 존재 ... entity를 쭉 가져와서 조회하니깐 문제 ..
        return em.createQuery(
                "select o from Order o" +
                        " join fetch o.member m" +
                        " join fetch o.delivery d", Order.class
        ).getResultList();
    }

    //distinct 없이 만약에 쿼리돌리면 ..
    //데이터 뻥티기의 ... 우려가 있음 .. (레퍼런스 조차 똑같은 ... 엔티티가 나오게됨 ...)
    // database와의 distinct 가 다른점은 ? db는 완전히 데이터가 일치해야 distinct가됨 ..\
    // 근데 jpa에서 distinct는 좀 다름 .. 이게 있으면 order (root인거지 이게...)를 가지고 올때 order가 같은 id 값이면 중복을 제거해줌
    // 물론 db 의 distinct도 날려줌
    // 일대다 페치 조인에서는... 페이징을 해버리면 ... 메모리에서 다해버리는 위험함 ...
    // db 상으로는 불가능한데 ... jpa가 메모리에서 해버리니깐 ... 경고로그를 남기면서 모든 데이터를 데이터베이스에서 읽어오고 페이징을 메모리에서 해버림 ... 대안을 찾아서 해야함..
    public List<Order> findAllWithItem() {
        return em.createQuery(
                "select distinct o from Order o" +
                        " join fetch o.member m" +
                        " join fetch o.delivery d" +
                        " join fetch o.orderItems oi" +
                        " join fetch oi.item i", Order.class)
                .setFirstResult(1)
                .setMaxResults(100)
                .getResultList();
    }

    //Dto를 바로 리턴 ..!
    //네트웍 용량을 최적화 한다지만 ... 그렇게 효과는 없음
    // 이 경우는 api 스펙이 바뀌면 뜯어 고칠 우려도 있음 ..
    //별도의 패키지 만드는 방법을 이용해보자
//    public List<OrderSimpleQueryDto> findOrderDtos() {
//        return em.createQuery(
//                "select new mybook.myshop.repository.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
//                        " from Order o" +
//                        " join o.member m" +
//                        " join o.delivery d", OrderSimpleQueryDto.class
//        ).getResultList();
//    }

    // v3 vs v4 ??
    // 둘간의 tradeoff 가 있음 ..!
    // 성능은 v4
    // 재사용성은 v3
}
