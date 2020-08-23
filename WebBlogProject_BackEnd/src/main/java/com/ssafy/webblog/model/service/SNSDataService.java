package com.ssafy.webblog.model.service;

import org.springframework.stereotype.Service;

@Service
public class SNSDataService {
	private String nickname;
	private String email;
	private int loginkind;
	
	public void makeSNSData(String nickname, String email, int loginkind) {
		this.nickname = nickname;
		this.email = email;
		this.loginkind = loginkind;
	}
	
	public String getSNSNickname() {
		return this.nickname;
	}
	public String getSNSEmail() {
		return this.email;
	}
	public int getSNSLoginKind() {
		return this.loginkind;
	}
}
