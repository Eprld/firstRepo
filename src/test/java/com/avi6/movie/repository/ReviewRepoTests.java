package com.avi6.movie.repository;

import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import com.avi6.movie.entity.Member;
import com.avi6.movie.entity.Movie;
import com.avi6.movie.entity.Review;

import jakarta.transaction.Transactional;

/*
 * 이 클래스는 리뷰를 밀어넣는데, 필수 항목으로는 참조 엔티티가 2개 필요(Movie, Member)
 * 이 객체가 존재하지 않으면 안됨
 * 
 * 리뷰의 컬럼내용은 영화에 대해서 어떤 Member 가 어떤 내용(text) 에 몇 점의 평점(grade)을 주는 지에 대해서 설정 하고 insert gka
 * 
 * 몇몇부분은 랜덤할 예정
 */
@SpringBootTest
public class ReviewRepoTests {

	@Autowired
	private ReviewRepository reviewRepository;
	
	@Autowired
	private MemberRepository memberRepository;
	
	
	
	@Commit 
	@Transactional 
	//@Test//리뷰 삭제
	void delMember() {
		Long mid = 6L;
		
		Member member = Member.builder().mid(mid).build();
		
		reviewRepository.deleteByMember2(member);
		memberRepository.deleteById(mid);
	}
	
	
	//@Test //리뷰Data get Test
	void testGetMovieReviewer() {
		Movie movie = Movie.builder().mno(100L).build();
		
		List<Review> res = reviewRepository.findByMovie(movie);
		
		res.forEach(movieRev -> {
			System.out.println(movieRev.getRno());
			System.out.println(movieRev.getGrade());
			System.out.println(movieRev.getText());
			System.out.println(movieRev.getMember().getEmail());//리뷰어 이메일
		});
	}
	
	

	//@Test
	void insertReviews() {
		
		//200개의 리뷰 등록
		IntStream.rangeClosed(1, 200).forEach(i -> {
			//영화 번호
			Long mno = (long)(Math.random() * 100)+ 1;
			
			//리뷰어 번호
			Long mid = (long)(Math.random() * 100) + 1;
			
			Member member = Member.builder()
					.mid(mid).build();
			
			Review review = Review.builder()
					.member(member)
					.movie(Movie.builder().mno(mno).build())
					.grade((int)(Math.random()* 5 )+1)
					.text(mno + " 번 영화에 대한 리뷰")
					.build();
			
			reviewRepository.save(review);
		});
		
	}
}

