package com.avi6.movie.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.avi6.movie.dto.ReviewDTO;
import com.avi6.movie.entity.Movie;
import com.avi6.movie.entity.Review;
import com.avi6.movie.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
	
	private final ReviewRepository reviewRepository;
	
	

	@Override
	public List<ReviewDTO> getReList(Long mno) {
		Movie movie = Movie.builder().mno(mno).build();
		
		List<Review> res = reviewRepository.findByMovie(movie);
		
		List<ReviewDTO> list = res.stream().map(reviewEn -> entityToDto(reviewEn)).collect(Collectors.toList());

		return list;
	}

	@Override
	public Long register(ReviewDTO reviewDTO) {
		Review review = dtoToEntity(reviewDTO);
				
				reviewRepository.save(review);
				
		return review.getRno();
	}

	@Override
	public void modify(ReviewDTO reviewDTO) {
		Optional<Review> res = reviewRepository.findById(reviewDTO.getReviewNum());
		
		if(res.isPresent()) {
			Review review = res.get();
			review.setGrade(reviewDTO.getGrade());
			review.setText(reviewDTO.getText());
			reviewRepository.save(review);
		}

	}

	@Override
	public void remove(Long reviewNum) {
		reviewRepository.deleteById(reviewNum);

	}

}
