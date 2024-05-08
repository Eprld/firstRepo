package com.avi6.movie.entity;



import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
/*
 * 이 엔티티는 관계 엔티티로 사용됨
 * 
 * 영화와, 멤버 사이에 Review 를 담당하기 때문에, 양쪽 다 M:1 M:1 의 관계로 설정 해야함
 * 위 두 개의 필드와 내용 그리고 평점을 필드로 선언함
 */

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class Review extends BaseEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long rno;
	
	private String text;
	
	private int grade;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Movie movie;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Member member;
	
	public void setText(String text) {
		this.text = text;
	}
	
	public void setGrade(int grade) {
		this.grade = grade;
	}

}
