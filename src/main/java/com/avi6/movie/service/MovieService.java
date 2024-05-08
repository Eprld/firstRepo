package com.avi6.movie.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.jaxb.SpringDataJaxb.PageRequestDto;
import org.springframework.data.repository.query.Param;

import com.avi6.movie.dto.MovieDTO;
import com.avi6.movie.dto.MovieImageDTO;
import com.avi6.movie.dto.PageRequestDTO;
import com.avi6.movie.dto.PageResultDTO;
import com.avi6.movie.entity.Movie;
import com.avi6.movie.entity.MovieImage;

/*
 * Entity <-> DTO 변환 메서드가 있어야 함..
 */
public interface MovieService {
	
	//movieDto 리턴 메서드 정의
	MovieDTO getMovie(@Param("mno") Long mno);
	
	
	//entity -> DTO 로 변환하는 메서드 정의합니다.
	//Repo 에서 get 한 movie 정보의 각 Row의 배열 내부에 있는 정보를 모두 분리해서 DTO 의 필드에 매핑시킵니다.
	//최종 리턴타입은 DTO 로 합니다
	
	
	default MovieDTO entityToDTO(Movie movie, List<MovieImage> movieImages, Double avg, Long reviewCount) {
		MovieDTO movieDTO = MovieDTO.builder()
				.mno(movie.getMno())
				.title(movie.getTitle())
				.regDate(movie.getRegDate())
				.modDate(movie.getModDate())
				.build();
		
		List<MovieImageDTO> movieImageDTOs = movieImages
				.stream()
				.map(m ->{
					MovieImageDTO mImage = MovieImageDTO.builder()
							.inum(m.getInum())
							.imgName(m.getImgName())
							.path(m.getPath())
							.uuid(m.getUuid())
							.build();
					return mImage;
				}).collect(Collectors.toList());
		movieDTO.setImageDTOList(movieImageDTOs);
		movieDTO.setAvg(avg);
		movieDTO.setReviewCount(reviewCount.intValue());
		
		return movieDTO;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	

	//등록 메서드 선언 등록된 mno 를 리턴하도록 함
	Long register(MovieDTO movieDTO);
	
	//DTO 를 entity 로 변환 메서드 정의함
	//문제는 MovieDTO 에는 하나 이상의 Image 정보를 어떻게 처리할 것인가..임
	//Movie 정보는 Movie Entity 로 Image 정보는 ImageEntity 로 가야함
	//Map<String, Object> 로 리턴하도록 변환 메서드 정의
	
	default Map<String, Object> dtoToEntity(MovieDTO movieDTO){
		Map<String, Object> entityMap = new HashMap<>();//HashMap 객체 생성
		
		//Movie Entity 생성
		Movie movie = Movie.builder()
				.mno(movieDTO.getMno())
				.title(movieDTO.getTitle())
				.build();
		//Map 에 movie 라는 key 로 movie 객체 put 
		entityMap.put("movie", movie);
		
		//이미지 리스트 추출해서 List 에 담기
		List<MovieImageDTO> imageDTOList = movieDTO.getImageDTOList();
		
		//이미지 DTO 저리하기
		if(imageDTOList != null && imageDTOList.size()>0) {
			List<MovieImage> imageEntities = imageDTOList.stream()
					.map(movieImageDTO -> {
						MovieImage movieImage = MovieImage.builder()
								.path(movieImageDTO.getPath())
								.imgName(movieImageDTO.getImgName())
								.uuid(movieImageDTO.getUuid())
								.movie(movie)
								.build();
						//생성된 MovieImage Entity 를 List 로 변환해야 하니, 이 Map 스트림에서 collect() 의 생산자로 넘겨야 합니다
						//그래야 collect() 를 받아서, Collectors 를 이용, list 로 리턴해줌
						return movieImage;
					}).collect(Collectors.toList());
			entityMap.put("imageList", imageEntities);
		}
		return entityMap;
		
	}

	PageResultDTO<MovieDTO, Object[]> getList(PageRequestDTO pageRequestDTO);
}
