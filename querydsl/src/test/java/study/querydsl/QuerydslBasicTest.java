package study.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.entity.Member;
import study.querydsl.entity.QMember;
import study.querydsl.entity.Team;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.*;
import static study.querydsl.entity.QMember.*;

@SpringBootTest
@Transactional
public class QuerydslBasicTest {

	@Autowired
	EntityManager em;

	JPAQueryFactory queryFactory;

	@BeforeEach
	public void before() {

		queryFactory = new JPAQueryFactory(em);

		Team teamA = new Team("a");
		Team teamB = new Team("b");

		em.persist(teamA);
		em.persist(teamB);

		Member member1 = new Member("kang", 10, teamA);
		Member member2 = new Member("member2", 20, teamA);
		Member member3 = new Member("member3", 30, teamB);
		Member member4 = new Member("member4", 40, teamB);
		em.persist(member1);
		em.persist(member2);
		em.persist(member3);
		em.persist(member4);
	}

	@Test
	public void testJPQL() {
		String qlString = "select m from Member m " +
							"where m.userName = :userName";

		Member findMember = em.createQuery(qlString, Member.class)
				.setParameter("userName", "kang")
				.getSingleResult();

		assertThat(findMember.getUserName()).isEqualTo("kang");
	}

	@Test
	public void testQuerydsl() {

		//QMember m = new QMember("m"); // 같은 테이블을 조인해서 쓸때만 권장.
		//QMember m = QMember.member;

		// member 를 static으로 올리면 깔끔 ..!
		Member findMember = queryFactory
				.select(member)
				.from(member)
				.where(member.userName.eq("kang"))
				.fetchOne();

		assertThat(findMember.getUserName()).isEqualTo("kang");

	}

	@Test
	public void search() {
		Member findMember = queryFactory
				.selectFrom(member)
//				.where(member.userName.eq("kang")
//						//.and(member.age.eq(10)))
//						.and(member.age.between(10,30)))
//				.fetchOne();
				//and 랑 같은 방법 ( 동적쿼리를 위해서 이렇게 하는게 조음
				.where(
						member.userName.eq("kang"),
						member.age.eq(10)
				)
				.fetchOne();

		assertThat(findMember.getUserName()).isEqualTo("kang");
	}

}