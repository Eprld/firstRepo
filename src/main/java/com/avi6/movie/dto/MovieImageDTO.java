package com.avi6.movie.dto;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


//영화포스터 DTO

/*
 * 여기서 신경써야할 부분,, 영화 이미지 정보는 en/decoding 되어져야함
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MovieImageDTO {

	private Long inum;
	
	private String uuid;
	
	private String imgName;
	
	private String path;
	
	public String getImageURL() {
		
		try {
			return URLEncoder.encode(path + "/" + uuid + "_" + imgName, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public String getThumImgURL() {
		
		try {
			return URLEncoder.encode(path + "/" + "thum" + uuid + imgName, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
