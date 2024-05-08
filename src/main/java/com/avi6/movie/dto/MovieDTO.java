package com.avi6.movie.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MovieDTO {

	private Long mno;
	
	private String title;
	
	//첨부되는 영화의 이미지 정보를 담는 list 생성,, 이유는 이미지가 무조건 온다라는 가정하에 매핑을 시키기 위함임
	@Builder.Default
	private List<MovieImageDTO> imageDTOList = new ArrayList<>();
	
	private double avg;
	
	private int reviewCount;
	
	private LocalDateTime regDate, modDate;
	
	
}
