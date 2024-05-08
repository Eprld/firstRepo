package com.avi6.movie.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.avi6.movie.entity.Member;
import com.avi6.movie.entity.Movie;
import com.avi6.movie.entity.Review;


public interface ReviewRepository extends JpaRepository<Review, Long> {
	
	//리뷰 리턴 메서드 선언
	//EntityGraph : Lazy 로 설정된 연관엔티티의 설정을 선언된 시점에 Eager 로 되돌리는 어노테이션
	//속성이 몇 가지 있는데, 제일 중요한 속성으로는 어떤 연관 엔티티를 지정할 건지의 attributePaths 임
	//값으로는 Eager 로 지정한 속성필드를 배열로 줄 수 있음
	//두 번째로는 Type 이라는 속성 값으로는 Fetch 값으로 일반적으로 사용됨
	//FETCH/load 차이점 : fetch 는 명시된 엔티티만 Enger 로 변경, 나머진 Lazy 로
	//load 는 지정된 엔티티 Enger, 나머지는 엔티티 클래스에 명시된 기본 방식으로 처리함
	@EntityGraph(attributePaths = {"member"}, type = EntityGraphType.FETCH)
	List<Review> findByMovie(Movie movie);
	
	//JPQL 을 이용한 review 와 member 동시 삭제
	//삭제될 Review 를 조건절을 이용해서 member 객체를 매핑시키도록 함, 이렇게 되면 메서드 호출시에 Review 삭제 되고
	//이후 Member 삭제시킴,, 단 JPQL 의 up,del 시엔 반드시 modifying 선언해야함
	
	@Modifying
	@Query("delete from Review r where r.member =:member")
	void deleteByMember2(@Param("member") Member member);
	
	//이건 JPQ 메서드를 이용해서 리뷰지우고, Member 삭제하는 메서드
	void deleteByMember(Member member);
	

}
