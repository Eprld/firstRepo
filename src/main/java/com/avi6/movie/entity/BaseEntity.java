package com.avi6.movie.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

/*
 * Base Entity 는 추상클래스로 선언
 */
@MappedSuperclass
@EntityListeners(value = {AuditingEntityListener.class})//이렇게 선언하면, Main 클래스에 @Enable 을 반드시 선언
@Getter
public abstract class BaseEntity {
	
	//회원가입일들이나, 작성일, 수정일 등을 자동관리하도록 필드선언함
	
	@CreatedDate
	@Column(name = "regdate", updatable = false)//수정 금지 , 컬럼명은 regdate 로 설정
	private LocalDateTime regDate;
	
	@LastModifiedDate
	@Column(name = "modDate")//수정 금지 , 컬럼명은 regdate 로 설정
	private LocalDateTime modDate;
	
	

}
