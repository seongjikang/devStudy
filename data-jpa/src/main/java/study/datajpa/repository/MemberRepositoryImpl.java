package study.datajpa.repository;

import lombok.RequiredArgsConstructor;
import study.datajpa.entity.Member;

import javax.persistence.EntityManager;
import java.util.List;

// 사실 이런 방식은 queryDsl 쓸때 많이 씀
@RequiredArgsConstructor
public class MemberRepositoryImpl implements CustomMemberRepository{

	//순수한 JPA로 쓰고 싶다면.
	private final EntityManager em;

	@Override
	public List<Member> customFindMember() {
		return em.createQuery("select m from Member m")
				.getResultList();
	}
}
