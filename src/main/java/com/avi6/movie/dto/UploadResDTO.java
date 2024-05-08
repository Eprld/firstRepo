package com.avi6.movie.dto;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//이미지 저장시 클라이언트에 리턴될 DTO
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadResDTO {

	private String fileName;
	private String uuid;
	private String folderPath;
	
	//Image 가 없는 URL 경로를 리턴하는 메서드 정의
	public String getImageURL() {
		//이 URL 정보는 업로드한 이미지를 볼 수 있도록 URL 정보를 담고 있음
		//그런데, 이 정보를 주고 받을 때, 기본적인 en/decoding 을 통해서 처리하도록 함,, URIEncoder, encoding, decoding
		
		try {
			return URLEncoder.encode(folderPath + "/" + uuid  + fileName, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public String getThumImgURL() {
		//이 URL 정보는 업로드한 이미지를 볼 수 있도록 URL 정보를 담고 있음
		//그런데, 이 정보를 주고 받을 때, 기본적인 en/decoding 을 통해서 처리하도록 함,, URIEncoder, encoding, decoding
		
		try {
			return URLEncoder.encode(folderPath + "/" + "thum" + uuid + fileName, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
