package study.querydsl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.dto.MemberDto;
import study.querydsl.dto.MemberSearchCondition;
import study.querydsl.dto.QMemberDto;
import study.querydsl.dto.TeamMemberDto;
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

		Member member1 = new Member("kangseongji", 10, teamA);
		Member member2 = new Member("kangbabo", 20, teamA);
		Member member3 = new Member("parkpp", 30, teamB);
		Member member4 = new Member("leeds", 40, teamB);


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
				.selectFrom(member)
				.fetchOne();

		Member fetchFirst = queryFactory
				.selectFrom(member)
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

	@Test
	public void basicCase() {
		List<String> result = queryFactory
				.select(member.age
						.when(10).then("10살")
						.when(20).then("20살")
						.otherwise("기타"))
				.from(member)
				.fetch();

		for(String s : result) {
			System.out.println("s = " + s);
		}
	}

	@Test
	public void complexCase() {
		List<String> result = queryFactory
				.select(new CaseBuilder()
						.when(member.age.between(0, 20)).then("0~20세")
						.when(member.age.between(21, 30)).then("21~30")
						.otherwise("나머지"))
				.from(member)
				.fetch();

		for(String s : result) {
			System.out.println("s = " + s);
		}
	}

	@Test
	public void constant() {
		List<Tuple> result = queryFactory
				.select(member.userName, Expressions.constant("상수"))
				.from(member)
				.fetch();

		for (Tuple tuple : result) {
			System.out.println("tuple = " + tuple);
		}
	}

	@Test
	public void distinct() {
		List<String> result = queryFactory
				.select(member.userName).distinct()
				.from(member)
				.fetch();

		for (String s : result) {
			System.out.println("name = " + s);
		}
	}

	@Test
	public void concat() {
		String member = queryFactory
				.select(QMember.member.userName.concat("&").concat(QMember.member.age.stringValue())) //ENUM 처리할때
				.from(QMember.member)
				.where(QMember.member.userName.eq("kang"))
				.fetchOne();

		System.out.println("member = " + member);
	}

	// 프로젝션 대상이 하나인 경우
	@Test
	public void simpleProjection() {
		List<String> result = queryFactory
				.select(member.userName)
				.from(member)
				.fetch();

		for (String member : result) {
			System.out.println("member = " + member);
		}
	}

	// 프로젝션 대상이 두개 이상인 경우 (Tuple로 조회)
	// tuple 죄회는 repository 안에서 정도만 쓰는게 좋다. (이것도 노출되면 좀 곤란한 데이터이니 ..)
	@Test
	public void tupleProjection() {
		List<Tuple> result = queryFactory
				.select(member.userName, member.age)
				.from(member)
				.fetch();

		for (Tuple tuple : result) {
			String userName = tuple.get(member.userName);
			Integer age = tuple.get(member.age);
			System.out.println(userName);
			System.out.println(age);
		}
	}

	@Test
	public void findDtoByJPQL() {
		List<MemberDto> result = em.createQuery(
						"select " +
								"new study.querydsl.dto.MemberDto(m.userName, m.age) "+
								"from Member m", MemberDto.class)
				.getResultList();

		for(MemberDto memberDto : result) {
			System.out.println("memberDto = " + memberDto);
		}
	}

	@Test
	public void findDtoBySetter() {
		List<MemberDto> result = queryFactory
				.select(Projections.bean(MemberDto.class,
						member.userName, member.age)) // 이게 dto 랑 정확히 매칭 되어야 쓸수 있음
				.from(member)
				.fetch();

		for(MemberDto memberDto : result) {
			System.out.println("memberDto = " + memberDto);
		}
	}

	@Test
	public void findDtoByField() {
		List<MemberDto> result = queryFactory
				.select(Projections.fields(MemberDto.class,
						member.userName, member.age)) // 이게 dto 랑 정확히 매칭 되어야 쓸수 있음
				.from(member)
				.fetch();


		for(MemberDto memberDto : result) {
			System.out.println("memberDto = " + memberDto);
		}
	}

	@Test
	public void findDtoByConstructor() {
	// 생성자의 경우 type만 맞으면됨
		List<TeamMemberDto> result = queryFactory
//				.select(Projections.constructor(MemberDto.class,
//						member.userName, member.age)) //
				.select(Projections.constructor(TeamMemberDto.class,
						member.userName, member.age)) //
				.from(member)
				.fetch();

		for(TeamMemberDto memberDto : result) {
			System.out.println("memberDto = " + memberDto);
		}
	}

	@Test
	public void findDtoTeamMember() {
		List<TeamMemberDto> result = queryFactory
				.select(Projections.fields(TeamMemberDto.class,
						member.userName.as("teamMemberName"), // 맞지 않으면 .. 이렇게 as를 써야함
						member.age))
				.from(member)
				.fetch();

		for(TeamMemberDto memberDto : result) {
			System.out.println("teamMemberDto = " + memberDto);
		}
	}

	@Test
	public void findDto() {
		QMember subMember = new QMember("subMember");

		List<TeamMemberDto> result = queryFactory
				.select(Projections.fields(TeamMemberDto.class,
						member.userName.as("teamMemberName"), // 아래와 같은 표현
						//ExpressionUtils.as(member.userName, "teamMemberName"),
						ExpressionUtils.as(
								JPAExpressions
									.select(subMember.age.max())
									.from(subMember), "age")
				))
				.from(member)
				.fetch();

		for(TeamMemberDto memberDto : result) {
			System.out.println("teamMemberDto = " + memberDto);
		}
	}

	@Test
	public void findQDtoByQueryProjection() {
		// 오류 발생시 ..런타임에서 잡을수 있다는 치명적인 단점을 없앨수 있다는 장점
		// 근데 이렇게하면 QueryDSL에 의존적이게 된다는 단점이 있음
		List<MemberDto> result = queryFactory
				.select(new QMemberDto(member.userName, member.age))
				.from(member)
				.fetch();

		for (MemberDto memberDto : result) {
			System.out.println("teamMemberDto = " + memberDto);
		}
	}

	@Test
	public void dynamicQueryBooleanBuilder() {
		String userNameParam = "kang";
		Integer ageParam = 10;

		List<Member> result = searchMemberCheckCond(userNameParam, ageParam);
		assertThat(result.size()).isEqualTo(1);
	}

	private List<Member> searchMemberCheckCond(String nameCond, Integer ageCond) {
		//BooleanBuilder builder = new BooleanBuilder(member.userName.eq(nameCond)); // 이런식으로 초기값 설정 가능
		BooleanBuilder builder = new BooleanBuilder();
		if (nameCond != null) {
			builder.and(member.userName.eq(nameCond));
		}

		if (ageCond != null) {
			builder.and(member.age.eq(ageCond));
		}

		return queryFactory
				.selectFrom(member)
				.where(builder)
				.fetch();
	}

	@Test
	public void dynamicQueryWhereParam() {
		String userNameParam = "kang";
		Integer ageParam = 10;

		List<Member> result = searchMemberCheckCond2(userNameParam, ageParam);
		assertThat(result.size()).isEqualTo(1);
	}

	private List<Member> searchMemberCheckCond2(String nameCond, Integer ageCond) {

		return queryFactory
				.selectFrom(member)
				//.where(userNameEq(nameCond), ageEq(ageCond))
				.where(allEq(nameCond, ageCond)) // 이런식으로 조립을해서 가능하다는 큰 장점 , 이렇게 만들어두면 다른 쿼리에서도 재활용할수 있는 장점
				.fetch();
	}

	private BooleanExpression userNameEq(String nameCond) {
//		if (nameCond != null ) {
//			return  member.userName.eq(nameCond);
//		} else {
//			return null; // where에 null 이들어가면 무시됨.그걸이용해서 동적쿼리를 만듦
//		}
		return nameCond != null ? member.userName.eq(nameCond) : null;
	}

	private BooleanExpression ageEq(Integer ageCond) {
		return ageCond != null ? member.age.eq(ageCond) : null;
	}

	private BooleanExpression allEq(String nameCond, Integer ageCond) {
		return userNameEq(nameCond).and(ageEq(ageCond));
	}

	@Test
	@Commit
	public void bulkUpdate() {
		long updateCnt = queryFactory
				.update(member)
				.set(member.userName, "babo")
				.execute();

		List<Member> members = queryFactory
				.selectFrom(member)
				.fetch();

		for(Member member: members) {
			System.out.println(member.getUserName()); // 아무것도 변경되지 않음
		}
	}

	@Test
	@Commit
	public void bulkUpdate2() {
		long updateCnt = queryFactory
				.update(member)
				.set(member.userName, "babo")
				.set(member.age, member.age.add(1))
				.execute();

		em.flush();
		em.clear();

		List<Member> members = queryFactory
				.selectFrom(member)
				.fetch();

		for(Member member: members) {
			System.out.println(member.getUserName());
		}
	}

	@Test
	@Commit
	public void bulkUpdate3() {
		long updateCnt = queryFactory
				.update(member)
				//.set(member.age, member.age.add(1))
				.set(member.age, member.age.multiply(2))
				.execute();

		em.flush();
		em.clear();

		List<Member> members = queryFactory
				.selectFrom(member)
				.fetch();

		for(Member member: members) {
			System.out.println(member);
		}
	}

	@Test
	public void bulkUpdate4() {
		long deleteCnt = queryFactory
				.delete(member)
				.execute();
	}

	@Test
	public void sqlFunc() {
		List<String> result = queryFactory
				.select(Expressions.stringTemplate(
						"function('replace', {0}, {1} , {2})",
						member.userName, "kang", ""
				))
				.from(member)
				.fetch();

		for (String name : result) {
			System.out.println(name);
		}
	}

	@Test
	public void sqlFunc2() {
		List<String> result = queryFactory
				.select(member.userName)
				.from(member)
//				.where(member.userName.eq(
//						Expressions.stringTemplate(
//								"function('lower', {0})",
//								member.userName )))
				.where(member.userName.eq(member.userName.lower()))
				.fetch();

		for (String name : result) {
			System.out.println(name);
		}
	}

}
