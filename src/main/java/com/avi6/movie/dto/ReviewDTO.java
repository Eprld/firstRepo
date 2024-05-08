package com.avi6.movie.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTO {

	private Long reviewNum;
	
	//read/html 에 parameter 로 오기 떄문에
	//매핑만 하면 됨
	private Long mno;
	
	//member id
	private Long mid;
	
	private String nickName;
	
	private String email;
	
	private int grade;
	
	private String text;
	
	private LocalDateTime regDate,modDate;
	
}
