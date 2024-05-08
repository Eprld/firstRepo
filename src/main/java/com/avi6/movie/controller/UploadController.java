package com.avi6.movie.controller;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.avi6.movie.dto.UploadResDTO;

import net.coobird.thumbnailator.Thumbnailator;

@RestController
public class UploadController {

	//업로드 파일이 저장될 폴더 패스 빌드 선언
	@Value("${com.avi6.movie.path}") //properties 의 변수 값
	private String uploadPath;
	
	//이미지를 뿌려주는 display 요청 처리
	/*
	 * 중요~ 내부에 있는 이미지를 보여주는 게 아니라, 외부에 있는 파일을 브라우저에 출력을 시키느 ㄴ것임
	 * 이 부분은 jsp 참조
	 * 스트림을 이용해서 브라우저에 이미지 데이터를 전송한다고 보면 됨
	 * 떄문에, 클라이언트에서는 어떤 이미지를 보여줄건지에 대한 file 정보를, 서버에서는 이 정보를 이용해서 
	 * 파일객체를 생성후에, 브라우저에 스트림을 전송해야 함
	 * 
	 * 주의~ mimeType 은 일반적인 형태가 아니라, binary 이기 떄문
	 * 이부분은 nio 패키지의 api 가 쉽게 처리함
	 */
	@GetMapping("/display")
	public ResponseEntity<byte[]> getFile(@RequestParam("fileName") String fileName){
		ResponseEntity<byte[]> responseEntity = null;
		
		//encoding 된 파라미터 정보를 decode
		try {
			String srcFileName = URLDecoder.decode(fileName);
			
			System.out.println("요청된 파일 명 --> " + srcFileName);
				
			//해당 파일을 File 객체로 생성함
			File file = new File(uploadPath + File.separator + srcFileName);
			
			//브라우저에게 마임타입을 전송해야함, 이때 header 에 정보를 실어보내는데
			//boot 에서는 HttpHeaders 라는 객체를 이용해서 key,value 의 map 형태로 add 해서 보낼 수 있음
			//반드시 데이터가 넘어가기 전에 헤더부터 전송해야함
		
			
			HttpHeaders headers = new HttpHeaders();
			//MIME 처리;
			headers.add("Content-Type", Files.probeContentType(file.toPath()));
			
			//스트림을 통해서 브라우저에 데이터 전송, 이걸 간단하게 해주는 스프링부트의 API 가 있는데
			//FileCopyUtils 임 이중 byte[] 을 리턴해주는 copy....() 메서드를 사용함
			//반드시 ResponseEntity 에 담아서 가야함
			responseEntity = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file),HttpStatus.OK);
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return responseEntity;
	}
	
	@PostMapping("/uploadAjax")
	
	public ResponseEntity<List<UploadResDTO>> uploadFile(@RequestParam("uploadFiles") MultipartFile[] multipartFiles) { //@RequestParam  @param 차이
		
		
		//클라이언트 브라우저에 보낼 DTO list 생성
		List<UploadResDTO> resDTOList = new ArrayList<>();

		
		
		for(MultipartFile file : multipartFiles) {
			//브라우저에 따라서 업로드 된 파일명이 path 를 포함할 때도 있고 아닐때도 있음(IE 는 포함)
			//떄문에 만약 경로정보까지 포함되어 있다면 분리해서 파일명만 추출함
			//파일에 대한 모든 정보는 MultipartFile 이 다 가지고 있음 메서드로 필요한 정보 get
			
			//특정 확장자를 가진 파일만 올려야 하도록 보안이 설정되어있을 떄
			//파일 업로드시 반드시 특정 확장자인 경우인지를 확안해야 하는 로직이 공통코드에 설정되어있음
			//예를들어, 이미지 파일만 올릴 수 있도록 필터링 해봄
			//업로드 요청된 파일의 모든 정보는 MultipartFile 이 가지고 있음
			//이중 파일타입은 getConnectType() 으로 얻어낼 수 있음
			
			//결과형식은 json 으로 보낼건데, 보낼 데이터는 아래와 같은
			/*
			 * 1. 업로드된 origin file Name
			 * 2. 파일의 uuid 값
			 * 3. 업로드된 파일의 저장 패스
			 * 
			 * 이렇게 하는 이유는 나중에 위 정보를 이용해서 삭제까지 하기위함
			 */
			
			String oriName = file.getOriginalFilename();
			String fileName = oriName.substring(oriName.indexOf("\\") + 1);		
			
			//System.err.println("파일 타입 --> " + file.getContentType());
			if(file.getContentType().startsWith("image") == false) {
				System.out.println("이미지 파일 아님");
				return new ResponseEntity<>(null,HttpStatus.FORBIDDEN);
			}
			
			//저장할 폴더경로 얻어냄
			String folderPath = mkFolder();
			
			//파일은 오리진이름과, UUID 를 이용한 변조된 이름 두 개를 갖도록 함
			
			String uuid = UUID.randomUUID().toString();
			
			//저장할 파일이름 중간에 "_" 이용해서 UUID 와 합침
			//실제 이 이름이 저장될 이름으로 사용될 예정
			String saveName = uploadPath + File.separator + folderPath + File.separator + uuid + fileName;
			
			Path savePath = Paths.get(saveName);
			
			try {
				file.transferTo(savePath);
				
				//썸네일 이미지도 생성해서 같이 저장, UUID 전에 s라는 소문자를 넣어서 이름을 구성하도록 함
				//썸네일 이미지를 생성하는 객체 사용법
				/*
				 * Thumbnailator 객체의 static 메서드인 create...() 을 호출해서
				 * 파라미터만 채워주면 됨
				 * 
				 * 기본적으로, 파일 저장 path, 대상파일, wjdth, heigh 등이 파라미터임
				 */
				
				String thumbImgName = uploadPath + File.separator + folderPath + File.separator + "thum" + uuid + fileName;
				
				//File 객체를 위 경로를 대상으로 생성합니다.
				File thumFile = new File(thumbImgName);
				
				//썸네일 생성
				Thumbnailator.createThumbnail(savePath.toFile(), thumFile, 100, 100);
		
				//파일이 저장이 완료됐으니, DTO 를 생성해서 리스트에 넣어주면 됨
				resDTOList.add(new UploadResDTO(fileName, uuid, folderPath));
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.out.println("업로드 요청된 파일 : " + uploadPath);			
			System.out.println("업로드 요청된 파일 : " + fileName);
		}//End of for...
		
		return new ResponseEntity<>(resDTOList,HttpStatus.OK);
	}
	
	//삭제 요청 처리 매핑
	@PostMapping("/removeFile")
	public ResponseEntity<Boolean> delFile(@RequestParam("fileName") String fileName){
		//삭제는 File 객체의 delete() 호출
		
		String targetFile = null;
		System.out.println("요한이 천재");
		
		try {
			targetFile = URLDecoder.decode(fileName, "UTF-8");
			String targetFile2 = targetFile.replace("thum", "");
			
			System.out.println("삭제 요청된 파일 명 : " + targetFile);
			
			File file = new File(uploadPath + File.separator +targetFile);
			file.delete();
			
			File file2 = new File(uploadPath + File.separator +targetFile2);
			file2.delete();
			
			return new ResponseEntity<>(true, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
		
	}
	
	//Root 하위에 생성된 날짜이름으로 생성 폴더를 생성하는 메서드 정의
	private String mkFolder() {
		//현재 날짜정보를 얻어냄 (폴더명으로 사용)
		String str = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
		
		//새로운 폴더를 생성함, 반드시 root 하위에 있어야 함
		File uploadPathFolder = new File(uploadPath, str);
		if(!uploadPathFolder.exists()) {
			uploadPathFolder.mkdirs();
		}
		return str;
		
	}
	
	
	
	
	
	
	
	
}
