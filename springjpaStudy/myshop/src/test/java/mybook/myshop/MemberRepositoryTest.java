package mybook.myshop;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = MyshopApplication.class)
@ActiveProfiles("test")
class MemberRepositoryTest {
	@Autowired
	MemberRepository memberRepository;

	@Test
	@Transactional // 이게 테스트에 있으면 ,, 끝나자마자 롤백을 해버린다..!
	//@Rollback(false) // 이 어노테이션 달아주면 롤백안함.
	public void testMember() throws Exception {
		//given
		Member member = new Member();
		member.setUserName("kang");

		//when
		Long savedId = memberRepository.save(member);
		Member findMember = memberRepository.find(savedId);

		//then
		Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
		Assertions.assertThat(findMember.getUserName()).isEqualTo(member.getUserName());
		Assertions.assertThat(findMember).isEqualTo(member); //같은 영속성 컨텍스트 (1차캐시..!) 안이기에 값이 같음 !
	}

}