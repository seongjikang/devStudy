package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.datajpa.entity.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
	List<Member> findByUserNameAndAgeGreaterThan(String userName, int age);
	// 짤막짤막한 쿼리는 메서드 이름을 가지고 쿼리를 안짜고 이렇게 호출이 가능함
	//조회 : find...By, read...By, query...By, get...By
	//Count : count...By ( long 반환)
	//Exist: exists...By (boolean 반환)
	//삭제 : delete...By, remove...By (long 반환)
	//DISTINCT : findDistinct, findMemberDistinctBy
	// LIMIT : findFirst3, findFirst, findTop, findTop3
}
