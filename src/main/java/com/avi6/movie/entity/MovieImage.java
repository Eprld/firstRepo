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
 * 속성으로는 Inum, uuid(unique 한 id), name, 참조 Movie(ManyToOne), 이미지 경로(path --> 나중에 noi.parh 로 사용)
 */

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class MovieImage extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long inum;
	
	private String uuid;
	
	private String imgName;
	
	private String path;//이미지 경로
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Movie movie;
}
