package com.avi6.movie.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.avi6.movie.dto.ReviewDTO;
import com.avi6.movie.service.ReviewService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {

	private final ReviewService reviewService;
	
	/*
	 * 모든 요청 및 응답은 Rest 로 합니다.
	 * 때문에, 요청을 주고 받을때 기본적으로 pathVariable 형태로 사용합니다.{영화번호}
	 * 이때, 영화번호에 대한 전체 리뷰를 받을지, 수정을 위한 하나의 리뷰만 받을지 pathVariable 에서 구분자로 구분함
	 * 
	 * 마지막으로, 등록, 수정, 등에대한 Rest 요청에 대한 데이터는 @RequestBody 를 이용하고,, 매핑할 DTO 를 선언시킵니다. (알아서 매핑됨)
	 */
	
	//영화(mno) 에 대한 모들 리뷰 리턴 메서드
	@GetMapping("/{mno}/all")
	public ResponseEntity<List<ReviewDTO>> getList(@PathVariable("mno") Long mno){
		System.out.println(mno + "영화에 대한 리뷰 리스트 요청됨..");
		
		List<ReviewDTO> reviewDTO = reviewService.getReList(mno);
		
		return new ResponseEntity<>(reviewDTO,HttpStatus.OK);
	}
	
	//리뷰 등록 매핑
	@PostMapping("/{mno}")
	public ResponseEntity<Long> addReview(@RequestBody ReviewDTO reviewDTO){//Rest 에서 요청시 요청 바디를 파람으로 처리하는 @ 사용
		System.out.println("신규 리뷰에 요청 처리함, 요청된 내용 -> " + reviewDTO);
		
		Long reviewNum = reviewService.register(reviewDTO);
		return new ResponseEntity<>(reviewNum, HttpStatus.OK);
	}
	
	//영화(mno) 에 대한 특정 리뷰에 대한 수정
	@PutMapping("/{mno}/{reviewNum}")//요청시엔 이렇게 보냄 ex> review/3/2 --> 3번 영화에 2번 리뷰를 요청한다는 의미
	public ResponseEntity<Long> modifyReview(@PathVariable("mno") Long mno, @PathVariable("reviewNum") Long reviewNum, 
			@RequestBody ReviewDTO reviewDTO){
		System.out.println("리뷰 수정 요청됨,, 영화번호/리뷰번호 및 내용 ---> " + mno + " : " + reviewNum + " : " + reviewDTO);
		
		reviewService.modify(reviewDTO);
		
		return new ResponseEntity<>(reviewNum, HttpStatus.OK);
		
	}
	
	@DeleteMapping("/{mno}/{reviewNum}")
	public void delReview(@PathVariable("mno") Long mno, @PathVariable("reviewNum") Long reviewNum, @RequestBody ReviewDTO reviewDTO) {
		
		
		System.out.println("리뷰 삭제 요청됨, 요청된 내용 -> " + mno + " : " +  reviewNum + " : " + reviewDTO);
		
		
		
		
	}
}
