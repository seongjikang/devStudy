package study.querydsl;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.entity.Member;
import study.querydsl.entity.QMember;
import study.querydsl.entity.QTeam;
import study.querydsl.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import java.util.List;

import static com.querydsl.jpa.JPAExpressions.*;
import static org.assertj.core.api.Assertions.*;
import static study.querydsl.entity.QMember.*;
import static study.querydsl.entity.QTeam.team;

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
		Member member2 = new Member("kim", 20, teamA);
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

	@Test
	public void resultFetch() {
		List<Member> fetch = queryFactory
				.selectFrom(member)
				.fetch();

		Member fetchOne = queryFactory
				.selectFrom(QMember.member)
				.fetchOne();

		Member fetchFirst = queryFactory
				.selectFrom(QMember.member)
				//.limit(1).fetchOne()
				.fetchFirst();

		QueryResults<Member> results = queryFactory
				.selectFrom(member)
				.fetchResults();
		// 페이징 하기 위한 total contents 가져옴
		results.getTotal();
		List<Member> content = results.getResults();

		long count = queryFactory
				.selectFrom(member)
				.fetchCount();

	}

	@Test
	public void sort() {
		em.persist(new Member(null, 50));
		em.persist( new Member("member5", 50));
		em.persist(new Member("member6", 50));

		List<Member> descAge = queryFactory
				.selectFrom(member)
				.where(member.age.eq(50))
				.orderBy(member.age.desc())
				.fetch();

		List<Member> ascUserName = queryFactory
				.selectFrom(member)
				.where(member.age.eq(50))
				.orderBy(member.userName.asc())
				.fetch();

		List<Member> result = queryFactory
				.selectFrom(member)
				.where(member.age.eq(50))
				.orderBy(member.age.desc(), member.userName.asc().nullsLast())
				.fetch();

		Member member5 = result.get(0);
		Member member6 = result.get(1);
		Member memberNull = result.get(2);
		assertThat(member5.getUserName()).isEqualTo("member5");
		assertThat(member6.getUserName()).isEqualTo("member6");
		assertThat(memberNull.getUserName()).isEqualTo(null);

	}

	@Test
	public void paging1() {
		List<Member> result = queryFactory
				.selectFrom(member)
				.orderBy(member.userName.desc())
				.offset(1)
				.limit(2)
				.fetch();

		assertThat(result.size()).isEqualTo(2);
	}

	@Test
	public void paging2() {
		QueryResults<Member> result = queryFactory
				.selectFrom(member)
				.orderBy(member.userName.desc())
				.offset(1)
				.limit(2)
				.fetchResults();

		long total = result.getTotal(); //전체 컨텐츠 갯수
		long limit = result.getLimit(); // 현재 제한한 갯수
		long offset = result.getOffset(); // 조회 시작 위치
		List<Member> results = result.getResults(); // 조회된 컨텐츠 리스트

		for(Member member : results) {
			System.out.println(member.getUserName());
		}


		assertThat(result.getTotal()).isEqualTo(4);
		assertThat(result.getLimit()).isEqualTo(2);
		assertThat(result.getOffset()).isEqualTo(1);
		assertThat(result.getResults().size()).isEqualTo(2);

	}

	@Test
	public void aggregation() {
		List<Tuple> result = queryFactory
				.select(
						member.count(),
						member.age.sum(),
						member.age.avg(),
						member.age.min(),
						member.age.max()
				)
				.from(member)
				.fetch();

		//데이터 타입이 여러개가 들어올때 튜플을 활용하면됨
		Tuple tuple = result.get(0);

		assertThat(tuple.get(member.count())).isEqualTo(4);
		assertThat(tuple.get(member.age.sum())).isEqualTo(100);
		assertThat(tuple.get(member.age.avg())).isEqualTo(25);
		assertThat(tuple.get(member.age.min())).isEqualTo(10);
		assertThat(tuple.get(member.age.max())).isEqualTo(40);
	}

	@Test
	public void group() {
		List<Tuple> result = queryFactory
				.select(
						team.name,
						member.count(),
						member.age.avg(),
						member.age.sum(),
						member.age.min(),
						member.age.max()
				)
				.from(member)
				.join(member.team, team)
				.groupBy(team.name)
				.having(team.name.eq("a"))
				.fetch();

		Tuple teamA = result.get(0);
		//Tuple teamB = result.get(1);

		String teamAName = teamA.get(team.name);
		Long teamACount = teamA.get(member.count());
		Double teamAAvgAge = teamA.get(member.age.avg());
		Integer teamASum = teamA.get(member.age.sum());
		Integer teamAMin = teamA.get(member.age.min());
		Integer teamAMax = teamA.get(member.age.max());


//		String teamBName = teamB.get(team.name);
//		Long teamBCount = teamB.get(member.count());
//		Double teamBAvgAge = teamB.get(member.age.avg());
//		Integer teamBSum = teamB.get(member.age.sum());
//		Integer teamBMin = teamB.get(member.age.min());
//		Integer teamBMax = teamB.get(member.age.max());

		assertThat(teamAName).isEqualTo("a");
		assertThat(teamAAvgAge).isEqualTo(15);

//		assertThat(teamBName).isEqualTo("b");
//		assertThat(teamBAvgAge).isEqualTo(35);
	}

	@Test
	public void join() {
		List<Member> result = queryFactory
				.selectFrom(member)
				.join(member.team, team) // innerJoin 과 같음
				//.rightJoin(member.team, team) // right outer join
				//.leftJoin(member.team, team) // left outer join
				.where(team.name.eq("a"))
				//.on(team.name.eq("a")) // 이것과 where 절은 결과가 같음.. inner join이라서
				.fetch();

		assertThat(result)
				.extracting("userName")
				.containsExactly("kang", "kim");

	}

	@Test
	public void thetaJoin() {
		// 회원의 이름과 팀의 이름이 같은 조인

		em.persist(new Member("a"));
		em.persist(new Member("b"));
		em.persist(new Member("c"));

		List<Member> result = queryFactory
				.select(member)
				.from(member, team)
				.where(member.userName.eq(team.name))
				.fetch();

		for (Member member : result) {
			System.out.println("member = " + member);
		}

		assertThat(result)
				.extracting("userName")
				.containsExactly("a", "b");
	}

	@Test
	public void joinOnFiltering() {
		// 회원과 팀을 조인하면서 팀이름이 a 인 팀만 조회하고 회원은 모두 조회함
		// select m, t from Member m left join m.team t on t.name = 'a';

		List<Tuple> result = queryFactory
				.select(member, team)
				.from(member)
				.leftJoin(member.team, team).on(team.name.eq("a"))
				.fetch();

		for (Tuple tuple : result) {
			System.out.println("tuple = " + tuple);
		}
	}

	@Test
	public void joinOnNoRelation() {
		// 회원의 이름과 팀의 이름이 같은 대상 외부 조인
		// 연관 관계가 없는 엔티티를 외부 조인

		em.persist(new Member("a"));
		em.persist(new Member("b"));
		em.persist(new Member("c"));

		List<Tuple> result = queryFactory
				.select(member, team)
				.from(member)
				//.leftJoin(member.team, team) // 기존에 leftJoin을 할때 member의 team을 꺼내서 함 , 이렇게하면 id 값으로 매칭
				.leftJoin(team).on(member.userName.eq(team.name)) // 이름으로만 매칭
				.fetch();

		for (Tuple tuple : result) {
			System.out.println("tuple = " + tuple);
		}
	}

	@PersistenceUnit
	EntityManagerFactory emf;

	//fetch
	@Test
	public void fetchJoin() {
		em.flush();
		em.clear();

		Member findMember = queryFactory
				.selectFrom(member)
				.join(member.team, team).fetchJoin()
				.where(member.userName.eq("kang"))
				.fetchOne();

		boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam());
		assertThat(loaded).as("페치 조인 적용").isTrue();
	}

	@Test
	public void subQuery() {
		QMember subMember = new QMember("subMember");

		List<Member> result = queryFactory
				.selectFrom(member)
				.where(member.age.eq(
						//JPAExpressions.
						select(subMember.age.min())
								.from(subMember) // 이거에 대한 결과가 나올거임
				))
				.fetch();

		assertThat(result).extracting("age")
				.containsExactly(10);
	}

	@Test
	public void subQuery2() {
		QMember subMember = new QMember("subMember");

		// goe, loe 등 사용하여 수치 비교하는 서브쿼리 가능
		List<Member> result = queryFactory
				.selectFrom(member)
				.where(member.age.loe(
						//JPAExpressions.
						select(subMember.age.avg())
								.from(subMember) // 이거에 대한 결과가 나올거임
				))
				.fetch();

		assertThat(result).extracting("age")
				.containsExactly(10, 20);
	}

	@Test
	public void subQuery3() {
		QMember subMember = new QMember("subMember");

		//in query도 사용 가능함
		List<Member> result = queryFactory
				.selectFrom(member)
				.where(member.age.in(
						//JPAExpressions.
						select(subMember.age)
								.from(subMember)
								.where(subMember.age.lt(40))// 이거에 대한 결과가 나올거임
				))
				.fetch();

		assertThat(result).extracting("age")
				.containsExactly(10, 20, 30);
	}

	@Test
	public void selectSubQuery() {
		QMember subMember = new QMember("subMember");

		List<Tuple> result = queryFactory
				.select(member.userName,
						//JPAExpressions.
						select(subMember.age.avg())
								.from(subMember)
				)
				.from(member)
				.fetch();

		 for (Tuple tuple : result) {
		 	System.out.println("tuple = " + tuple);
		 }
	}
}
