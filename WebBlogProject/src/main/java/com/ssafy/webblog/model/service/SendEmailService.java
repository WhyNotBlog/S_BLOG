package com.ssafy.webblog.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.webblog.model.dto.SignupInformation;
import com.ssafy.webblog.model.repo.SendEmailDao;

@Service
public class SendEmailService {
	
	@Autowired
	SendEmailDao sDao;
	
	public SignupInformation getSignupInformationBySignupidAndToken(int signupid, String token) {
		SignupInformation result = sDao.getSignupInformationBySignupidAndToken(signupid, token);
		return result;
	}
	public void delete(SignupInformation signupInformation) {
		sDao.delete(signupInformation);
	}
	
	public void save(SignupInformation signupInformation) {
		sDao.save(signupInformation);
	}
}
