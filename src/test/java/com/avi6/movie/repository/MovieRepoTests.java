package com.avi6.movie.repository;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;

import com.avi6.movie.entity.Movie;
import com.avi6.movie.entity.MovieImage;

import jakarta.transaction.Transactional;

@SpringBootTest
public class MovieRepoTests {
	
	/*
	 * Movie 와 MovieImage 데이터 밀어넣기
	 * 
	 * 중요! MovieImage 는 Movie 를 참조하고 있기 때문에, 반드기 Movie 가 먼저 생성되고,
	 * 그 참조값이 가야 함
	 * 
	 * 하나의 메서드로 두 개의 테이블에 모두 넣어야 하기 떄문에 @Commit, @Transactional 을 선언
	 */
	
	//100개의 영화를 넣고, 하나의 영화당 이미지는 랜덤하게 5개 내외로 생성해서 넣도록 합니다.
	//Repository 가 있어야 Entity 의 데이터가 테이블에 들어가니, Movie, Image 모두 선언해서 사용함
	
	@Autowired
	private MovieRepository movieRepository;
	
	@Autowired
	private MovieImageRepository movieImageRepository;
	

	
	
	@Test
	void getMovie() {
		List<Object[]> res = movieRepository.getMovieWithAll(100L);
		
		System.out.println("뭔데 " + res );
		
		for(Object[] arr : res) {
			System.out.println(Arrays.toString(arr));
		}
	}
	
	//영화의 정보와, 리뷰수, 평점평균등을 리턴하는 메서드 태스트(마직 image 는 안 가져옴)
	//@Test
	void getListPage() {
		PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("mno").descending());
		
		Page<Object[]> res = movieRepository.getListPage(pageRequest);
		
		for(Object[] objs : res.getContent()) {
			System.out.println(Arrays.toString(objs));
		} 
	}
	
	@Commit
	@Transactional
	//@Test
	void insertMovieAndImage() {
		
		IntStream.rangeClosed(1, 100).forEach(i -> {
			Movie movie = Movie.builder()
					.title("Movie(영화)...." + i)
					.build();
			movieRepository.save(movie);
		
		
		//이미지 갯수에 사용될 랜덤값 설정
		
		int cnt = (int)(Math.random() * 5) + 1;
		
		//Image 생성 및 save
		for(int j = 0; j<cnt; j++) {
			MovieImage movieImage = MovieImage.builder()
					.uuid(UUID.randomUUID().toString())//java.util 의 UUID
					.movie(movie)
					.imgName("test" + j + ".jpg")
					.build();
			
			movieImageRepository.save(movieImage);
			
		}
	});
		
		
		
		
		
		
		
		
	}
	
}
