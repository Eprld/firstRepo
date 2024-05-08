package com.avi6.movie.service;
/*
 * 리뷰를 처리하는 기능의 서비스, 서비스를 생성할 때는 각 도매인(entity) 당 하나씩 생성하는 게 좋음
 * 
 * 여기서 정의할 것
 * 
 * 영화에 대한 모든 리뷰 get(1 : M)
 * 영화에 대한 리뷰 수정 (단일 리뷰 수정)
 * 리뷰 삭제
 * 리뷰 등록
 * 
 * 등록과, 수정 등의 DTO -> Entity 로 변환해야 하고,
 * 반대로 view 로 보낼 리뷰는 Entity --> DTO 변환 해야함 따라서 default method 정의해야함
 */

import java.util.List;

import org.springframework.web.bind.annotation.RequestParam;

import com.avi6.movie.dto.ReviewDTO;
import com.avi6.movie.entity.Member;
import com.avi6.movie.entity.Movie;
import com.avi6.movie.entity.Review;

public interface ReviewService {

	//하나의 영화에 대한 모든 리뷰 get
	List<ReviewDTO> getReList(@RequestParam("mno") Long mno);
	
	//리뷰 신규 등록
	Long register(ReviewDTO reviewDTO);
	
	//리뷰 수정
	void modify(ReviewDTO reviewDTO);
	
	//리뷰 삭제
	void remove(@RequestParam("reviewNum") Long reviewNum);
	
	default Review dtoToEntity(ReviewDTO movieReviewDTO){

        Review movieReview = Review.builder()
                .rno(movieReviewDTO.getReviewNum())
                .movie(Movie.builder().mno(movieReviewDTO.getMno()).build())
                .member(Member.builder().mid(movieReviewDTO.getMid()).build())
                .grade(movieReviewDTO.getGrade())
                .text(movieReviewDTO.getText())
                .build();

        return movieReview;
    }

    default ReviewDTO entityToDto(Review movieReview){

        ReviewDTO movieReviewDTO = ReviewDTO.builder()
                .reviewNum(movieReview.getRno())
                .mno(movieReview.getMovie().getMno())
                .mid(movieReview.getMember().getMid())
                .nickName(movieReview.getMember().getNickName())
                .email(movieReview.getMember().getEmail())
                .grade(movieReview.getGrade())
                .text(movieReview.getText())
                .regDate(movieReview.getRegDate())
                .modDate(movieReview.getModDate())
                .build();

        return movieReviewDTO;
    }
	
	
}
