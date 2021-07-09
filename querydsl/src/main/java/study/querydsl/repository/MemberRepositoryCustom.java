package study.querydsl.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import study.querydsl.dto.MemberSearchCondition;
import study.querydsl.dto.TeamOfMemberDto;

import java.util.List;

public interface MemberRepositoryCustom {
    List<TeamOfMemberDto> search(MemberSearchCondition condition);
    Page<TeamOfMemberDto> searchPageSimple(MemberSearchCondition condition, Pageable pageable);
    Page<TeamOfMemberDto> searchPageComplex(MemberSearchCondition condition,  Pageable pageable);
    Page<TeamOfMemberDto> searchPageComplexUpgrade(MemberSearchCondition condition,  Pageable pageable);
}
