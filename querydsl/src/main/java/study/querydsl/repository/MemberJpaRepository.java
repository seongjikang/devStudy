package study.querydsl.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import study.querydsl.dto.MemberSearchCondition;
import study.querydsl.dto.QTeamOfMemberDto;
import study.querydsl.dto.TeamOfMemberDto;
import study.querydsl.entity.Member;
import study.querydsl.entity.QMember;
import study.querydsl.entity.QTeam;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static org.springframework.util.StringUtils.*;
import static study.querydsl.entity.QMember.*;
import static study.querydsl.entity.QTeam.*;

@Repository
@RequiredArgsConstructor
public class MemberJpaRepository {
    private final EntityManager em; // 여긴 가짜 Proxy가 생성됨..그렇기에 .. multi thread 환경에서 문제없음
    private final JPAQueryFactory queryFactory;

//    public MemberJpaRepository(EntityManager em, JPAQueryFactory queryFactory) {
//        this.em =em;
//        //this.queryFactory = new JPAQueryFactory(em);
//        this.queryFactory = queryFactory;
//    }
    public void save(Member member) {
        em.persist(member);
    }

    public Optional<Member> findById(Long id) {
        Member findMember = em.find(Member.class, id);
        return Optional.ofNullable(findMember);
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public  List<Member> findAllQueryDsl() {
        return queryFactory
                .selectFrom(member)
                .fetch();
    }

    public List<Member> findByUserName(String userName) {
        return em.createQuery("select m from Member m where m.userName = :userName", Member.class)
                .setParameter("userName", userName)
                .getResultList();
    }

    public List<Member> findByUserNameQueryDsl(String userName) {
        return queryFactory
                .selectFrom(member)
                .where(member.userName.eq(userName))
                .fetch();
    }

    public List<TeamOfMemberDto> searchByBuilder(MemberSearchCondition condition) {

        BooleanBuilder builder = new BooleanBuilder();
        if (hasText(condition.getUserName())) {
            builder.and(member.userName.eq(condition.getUserName()));
        }
        if (hasText(condition.getTeamName())) {
            builder.and(team.name.eq(condition.getTeamName()));
        }
        if (condition.getAgeGoe() != null) {
            builder.and(member.age.goe(condition.getAgeGoe()));
        }
        if (condition.getAgeLoe() != null) {
            builder.and(member.age.goe(condition.getAgeLoe()));
        }

        return queryFactory
                .select(new QTeamOfMemberDto(
                        member.id.as("memberId"),
                        member.userName,
                        member.age,
                        team.id.as("teamId"),
                        team.name.as("teamName")
                ))
                .from(member)
                .leftJoin(member.team, team)
                .where(builder)
                .fetch();
    }

    public List<TeamOfMemberDto> searchByWhereParam(MemberSearchCondition condition) {
        return queryFactory
                .select(new QTeamOfMemberDto(
                        member.id.as("memberId"),
                        member.userName,
                        member.age,
                        team.id.as("teamId"),
                        team.name.as("teamName")
                ))
                .from(member)
                .leftJoin(member.team, team)
                .where(
                        userNameEq(condition.getUserName()),
                        teamNameEq(condition.getTeamName()),
                        ageGoe(condition.getAgeGoe()),
                        ageLoe(condition.getAgeLoe())
                )
                .fetch();
    }

    public List<Member> searchMember(MemberSearchCondition condition) {
        return queryFactory
                .selectFrom(member)
                .from(member)
                .leftJoin(member.team, team)
                .where(
//                        userNameEq(condition.getUserName()),
//                        teamNameEq(condition.getTeamName()),
//                        ageGoe(condition.getAgeGoe()),
//                        ageLoe(condition.getAgeLoe())
                        allCheck(condition)
                )
                .fetch();
    }

    private BooleanExpression userNameEq(String userName) {
        return hasText(userName) ? member.userName.eq(userName) : null;
    }

    private BooleanExpression teamNameEq(String teamName) {
        return hasText(teamName) ? team.name.eq(teamName) : null;
    }

    private BooleanExpression ageGoe(Integer ageGoe) {
        return ageGoe != null ? member.age.goe(ageGoe) : null;
    }

    private BooleanExpression ageLoe(Integer ageLoe) {
        return ageLoe != null ? member.age.loe(ageLoe) : null;
    }

    private BooleanExpression ageBetween(int ageLoe, int ageGoe) {
        return ageGoe(ageLoe).and(ageGoe(ageGoe));
    }

    private BooleanExpression allCheck(MemberSearchCondition condition) {
        return userNameEq(condition.getUserName())
                .and(teamNameEq(condition.getTeamName()))
                .and(ageGoe(condition.getAgeGoe()))
                .and(ageLoe(condition.getAgeLoe()));
    }
}
