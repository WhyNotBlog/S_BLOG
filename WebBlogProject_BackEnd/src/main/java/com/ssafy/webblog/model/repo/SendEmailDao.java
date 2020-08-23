package com.ssafy.webblog.model.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.webblog.model.dto.SignupInformation;


public interface SendEmailDao extends JpaRepository<SignupInformation, String> {
	SignupInformation getSignupInformationBySignupidAndToken(int signupid, String token);
	
}
