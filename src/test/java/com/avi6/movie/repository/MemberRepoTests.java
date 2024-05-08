package com.avi6.movie.repository;

import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.avi6.movie.entity.Member;

@SpringBootTest
public class MemberRepoTests {

	@Autowired
	private MemberRepository memberRepository;
	
	//멤버 정보 밀어넣기
	//@Test
	void insertMember() {
		IntStream.rangeClosed(1, 100).forEach(i -> {
			Member member = Member.builder()
					.email("rev" + i + "avi6@.com")
					.pw("1111")
					.nickName("review" + i)
					.build();
			
			memberRepository.save(member);
			
			//이건 주석입니다
		});
	}
}
