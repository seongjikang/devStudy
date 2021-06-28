package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.List;
import java.util.Optional;

// 화면에 맞춘 복잡한 쿼리는 별도로 만들어서 진행하는게 낫다.
// 핵심 비지니스와 화면에 맞춘 클래스는 쪼개버리자
public interface MemberRepository extends JpaRepository<Member, Long>, CustomMemberRepository {
	List<Member> findByUserNameAndAgeGreaterThan(String userName, int age);
	// 짤막짤막한 쿼리는 메서드 이름을 가지고 쿼리를 안짜고 이렇게 호출이 가능함
	//조회 : find...By, read...By, query...By, get...By
	//Count : count...By ( long 반환)
	//Exist: exists...By (boolean 반환)
	//삭제 : delete...By, remove...By (long 반환)
	//DISTINCT : findDistinct, findMemberDistinctBy
	// LIMIT : findFirst3, findFirst, findTop, findTop3

//	@Query(name = "Member.findByUserName") // 얘가 없어도 동작함..
	// 아래 메서드는 만약에 먼저 네임드 쿼리가 있으면 동작 없으면 메스드 이름으로 쿼리 만들어서 동작...
	// 이 기능은 거의 ~~~ 사용 안함 ... 실무에서는....
	// 근데 장점이 있음 .... 그냥 query를 메서드 내부에서 작성하면 ... 고객이 기능을 눌려야 문법오류가 나오지만
	// NamedQuery는 로딩할때 에러를 내서 에러를 알려줌. (파싱을 미리 해줌 ...!)
	List<Member> findByUserName(@Param("userName") String userName);

	@Query("select m from Member m where m.userName = :userName and m.age = :age")
	List<Member> findUser(@Param("userName") String userName, @Param("age") int age);

	@Query("select m.userName from Member m")
	List<String> findUserNameList();

	//DTO 로 조회하는 방법 개발
	@Query("select new study.datajpa.dto.MemberDto(m.id, m.userName, t.name) from Member m join m.team t")
	List<MemberDto> findMemberDto();

	//List 같은 컬렉션을 쿼리로 돌릴때
	@Query("select m from Member m where m.userName in :names")
	List<Member> findByNames(@Param("names") List<String> names);

	List<Member> findListByUserName(String userName); // 컬렉션
	Member findMemberByUserName(String userName); // 단건
	Optional<Member> findOptionalByUserName(String userName); // 단건 optional

	// 카운트 할때는 조인을 할 필요가 없다 ..! 그래서 이런 로직을 넣어줘야함.
	@Query(value = "select m from Member m left join m.team t",
			countQuery = "select count(m.userName) from Member m")
	Page<Member> findPageByAge(int age, Pageable pageable);

	Slice<Member> findSliceByAge(int age, Pageable pageable);

	List<Member> findByAge(int age, Pageable pageable);

	@Modifying(clearAutomatically = true) // executeUpdate 같은 기능을 함.
	@Query("update Member m set m.age = m.age + 1 where m.age >= :age")
	int bulkAgePlus(@Param("age") int age);

	// n+1 문제가 있음 ...
	@Query("select m from Member m left join fetch m.team")
	List<Member> findMemberFetchJoin();

	//위에것을 깔끔하게 하는 방법
	@Override
	@EntityGraph(attributePaths = {"team"})
	List<Member> findAll();

	// 쿼리를 짰는데 ... 엔티티그래프 쓸때도 가능
	@EntityGraph(attributePaths = {"team"})
	@Query("select m from Member m")
	List<Member> findMemberEntityGraph();

	// 그리고 named query에서도 entity graph 사용 가능
	//@EntityGraph(attributePaths = {"team"})
	@EntityGraph("Member.all")
	List<Member> findEntityGraphByUserName(@Param("userName") String userName);

	@QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
	Member findReadOnlyByUserName(String userName);

	// 실시간 서비스는 왠만하면 걸지 말것 !
	// 거실려면 Optimistic 락이라고 버저닝이라는 메커니즘으로 해결하는 방법을 하자...
	// 금융권에서는 lock은 필수임으로 ... 잘 고려해서 사용해야함 ..
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	List<Member> findLockByUserName(String userName);

	//List<UserNameOnly> findProjectionsByUserName(@Param("userName") String userName);
	List<UserNameOnlyDto> findProjectionsByUserName(@Param("userName") String userName);

	<T> List<T> findProjectionsByUserName(@Param("userName") String userName, Class<T> type);
}
