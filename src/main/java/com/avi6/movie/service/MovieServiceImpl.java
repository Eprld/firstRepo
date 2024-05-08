package com.avi6.movie.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.util.Map;
import java.util.function.Function;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.avi6.movie.dto.MovieDTO;
import com.avi6.movie.dto.PageRequestDTO;
import com.avi6.movie.dto.PageResultDTO;
import com.avi6.movie.entity.Movie;
import com.avi6.movie.entity.MovieImage;
import com.avi6.movie.repository.MovieImageRepository;
import com.avi6.movie.repository.MovieRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

	private final MovieRepository movieRepository;
	
	private final MovieImageRepository movieImageRepository;
	
	//이 메서드에서는 동시에 두 개의 Repo 를 사용해서 insert 를 해야 합니다
	//따라서, Transactional, Commit 까지 선언
	@Transactional
	@Override
	public Long register(MovieDTO movieDTO) {
		//변환 메서드를 호출해서 객체 생성합니다.
		Map<String , Object> convertMap = dtoToEntity(movieDTO);
		Movie movie = (Movie)convertMap.get("movie");
		List<MovieImage> movieImages = (List<MovieImage>)convertMap.get("imageList");
		System.out.println(movie + "요한1");
		movieRepository.save(movie);
		
		//이미지는 list 로 되어있으니, 순회해서 entity 를 하나씩 추출 후 save 해야함 
		movieImages.forEach(imgEntity ->{
			movieImageRepository.save(imgEntity);
		});
		
		return movie.getMno();
	}
	
	
	@Override
	public PageResultDTO<MovieDTO, Object[]> getList(PageRequestDTO pageRequestDTO){
		Function<Object[], MovieDTO> converFunction = (entity -> entityToDTO
				((Movie)entity[0],
						(List<MovieImage>)Arrays.asList((MovieImage)entity[1]),
						(double)entity[2] , (Long)entity[3]));
		
		Pageable pageable =  pageRequestDTO.getPageable(Sort.by("mno").descending());
		Page<Object[]> page =  movieRepository.getListPage(pageable);
		
		page.getContent().forEach(row->{
			System.out.println("요청된 페이지 리턴 Data --> " + Arrays.toString(row));
		});
		
		return new PageResultDTO<>(page, converFunction);
		
	}
	
	//영화 정보 get 메서드
	public MovieDTO getMovie(Long mno) {
		List<Object[]> result = movieRepository.getMovieWithAll(mno);
		
		//위 result 에서 각각의 entity 를 분리해서  하나의 MovieDTO 에 세팅함
		Movie movie = (Movie)result.get(0)[0];
		
		List<MovieImage> movieImages = new ArrayList<>();
		
		//영화 이미지를 찾아서, movieImage 에 add 시킴
		result.forEach(entity -> {
			MovieImage image = (MovieImage)entity[1];
					movieImages.add(image);
		});
		
		Double avg = (Double)result.get(0)[2];
		Long revieCnt = (Long)result.get(0)[3];
		
		MovieDTO movieDTO = entityToDTO(movie, movieImages, avg, revieCnt);	
		
		return movieDTO;
		
	}
	
	
	

}
