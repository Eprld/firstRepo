package com.avi6.movie.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.avi6.movie.dto.MovieDTO;
import com.avi6.movie.dto.PageRequestDTO;
import com.avi6.movie.service.MovieService;


import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/movie")
@RequiredArgsConstructor
public class MovieController {

	private final MovieService movieService;
	
	//read 매핑
	@GetMapping("/read")
	public void read(@RequestParam("mno") long mno, @ModelAttribute("PagerequestDTO") PageRequestDTO pageRequestDTO, Model model) {
		System.out.println("영화정보가 요청됨 --> " + mno);
		
		//서비스 호출
		MovieDTO movieDTO = movieService.getMovie(mno);
		
		model.addAttribute("dto", movieDTO);
	}
	
	@GetMapping("/list")
	public void list(PageRequestDTO pageRequestDTO, Model model) {
		System.out.println("요청된 영화 목록 페이지 --> " + pageRequestDTO);
		
		model.addAttribute("result", movieService.getList(pageRequestDTO));
	}
	
	//영화 등록에 사용할 폼요청 및 등록 정의
	
	@GetMapping("/register")
	public void register() {
		
	}
	
	
	
	//영화등록 요청 처리 매핑하기
	@PostMapping("/register")
	public String resister(MovieDTO movieDTO, RedirectAttributes redirectAttributes) {
		
		//등록 후 리턴값 확인 후 list 에 보낼 메세지를 셋업합니다.(방명록이나 게시판과 같음)
		Long mno = movieService.register(movieDTO);
		
		if(mno < 0) {
			System.out.println("영화 등록 예외 발생함..");
			return null;
		}
		
		redirectAttributes.addFlashAttribute("msg", "영화가 잘 등록되었음 등록번호 : " + mno);
		return "redirect:/movie/list";
	}
}
