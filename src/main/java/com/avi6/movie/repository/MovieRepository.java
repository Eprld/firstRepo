package com.avi6.movie.repository;



import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.avi6.movie.entity.Movie;


public interface MovieRepository extends JpaRepository<Movie, Long> {

	/*
	 * 여기서 할 데이터 조회 내용들,,
	 * 
	 * 1.리스트 화면에서 영화의 제목, 이미지 하나, 영화 리뷰의 평점, 리뷰수를 출력할 데이터 get
	 * 2.영화 조회(상세) 에서 사용될 영화와 이미지들, 리뷰의 평균점수/갯수 get
	 * 3.리뷰에 대한 정보에는 멤버의 이메일 or nickName 을 같이 view 해줄 예정임
	 */
	
	//3번 처리 Query 정의. mno 를 받아서 처리하는 방법,
		//2 --> 5개의 리뷰가 리턴됨,, 갯수와 영화정보를 같이 보여줌
	
	
	
	
	//1번을 처리할 Query 정의
	@Query("select m, mi, AVG(COALESCE(r.grade,0)),COUNT(distinct r) "
			+ "from Movie m left outer join MovieImage mi on mi.movie = m "
			+ "left outer join Review r on r.movie = m group by m")
	Page<Object[]> getListPage(Pageable pageable);
	
	//영화조회시 필요한 이미지 가져오기 처리 메서드 정의,, 여기서 주의할 점은 nativie 쿼리를 사용하는 것
	//native 쿼리란, ORM 매퍼가 아닌, 실제 DB query 를 이용해서 사용하는 것을 말함
	/*
	 @Query("SELECT m.mno, mi.inum,mi.img_name,mi.path, "
	+ "mi.uuid,COUNT(r.rno)리뷰수, avg(COALESCE(r.grade,0)) "
   + "FROM movie m "
  + "LEFT OUTER JOIN " 
   + "movie_image mi ON m.mno = mi.movie_mno "
   + "LEFT OUTER join "
  + "review r ON m.mno = r.movie_mno "
   + "WHERE m.mno = 100 "
   + "GROUP BY mi.inum", nativeQuery = true)
	 */
	
	@Query("Select m, mi, avg(coalesce(r.grade,0)),count(distinct r) " +
			"from Movie m left outer join MovieImage mi on mi.movie = m " +
			"left outer join Review r on r.movie = m where m.mno =:mno group by mi.inum")
	List<Object[]> getMovieWithAll(@Param("mno") Long mno);
}
