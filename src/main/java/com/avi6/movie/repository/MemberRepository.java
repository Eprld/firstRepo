package com.avi6.movie.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.avi6.movie.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

}
