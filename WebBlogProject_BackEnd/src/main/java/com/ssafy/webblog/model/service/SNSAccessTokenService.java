package com.ssafy.webblog.model.service;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

@Service
@Transactional
public class SNSAccessTokenService {
	static String SNS_Access_Token = "";
	static String email = "";

	public void makeSNSData(String SNS_Access_Token, String email) {
		System.out.println("SNS Token : " + this.SNS_Access_Token);
		System.out.println("ID : " + this.email);
		this.SNS_Access_Token = SNS_Access_Token;
		this.email = email;
	}

	public String getToken() {
		String result =  this.SNS_Access_Token;
		this.SNS_Access_Token = "";
		return result;
	}
	
	public String getEmail() {
		String result =  this.email;
		this.email = "";
		return result;
	}
}
