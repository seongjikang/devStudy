package study.datajpa.repository;

//Projection 에는 close Projection 과 open Projection 이 있음

import org.springframework.beans.factory.annotation.Value;

public interface UserNameOnly {
//	@Value("#{target.userName + ' ' + target.age}") // 이게 있으면 open Projections 임 (entity를 다 가져와서 처리), 없으면 close projections
	String getUserName();
}
