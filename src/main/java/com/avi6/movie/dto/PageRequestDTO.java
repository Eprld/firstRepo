package com.avi6.movie.dto;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/*
 * 사용자가 list 에서 페이지 요청시 페이지 값을 담는 DTO
 */

@Builder
@AllArgsConstructor//이놈은 파라미터를 받는 생성자를 생성해주는 anno임
@Data
public class PageRequestDTO {
	private int page;//페이지 번호
	private int size;//페이지당 목록수
	private String type;//조건 검색의 타입(제목 or 내용 or 작성자)
	private String keyword;//검색어
	
	public PageRequestDTO() {
		//기본값으로 각 값 설정함
		this.page = 1;
		this.size = 10;
	}
	
	//나중에 Page 처리항 Pageable 리턴 메서드 정의 정렬객체를 파라미터로 받아서 처리함.
	public Pageable getPageable(Sort sort) {
		return PageRequest.of(page-1, size, sort);//페이지는 0부터 시작하기 때문에 윙서 초기값은 1에서 -1 해서 0페이지 리턴
	}
	
	//JAVA 의 Function interface 에 대해서 알아보도록 해봐요
	
}
