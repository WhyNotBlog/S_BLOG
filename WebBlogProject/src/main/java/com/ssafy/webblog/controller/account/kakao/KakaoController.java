package com.ssafy.webblog.controller.account.kakao;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.webblog.model.dto.User;
import com.ssafy.webblog.model.service.JwtDecoder;
import com.ssafy.webblog.model.service.JwtService;
import com.ssafy.webblog.model.service.SNSAccessTokenService;
import com.ssafy.webblog.model.service.SNSDataService;
import com.ssafy.webblog.model.service.UserAccountService;


@Controller
@RequestMapping("/account")
@CrossOrigin(origins = { "*" }, maxAge = 6000)
public class KakaoController {
	
	
	@Value("${backurl}")
	private String backurl;
	@Value("${fronturl}")
	private String fronturl;
	
	static Logger logger = LoggerFactory.getLogger(KakaoController.class);	
	
	HttpConnection conn = HttpConnection.getInstance();		

	@Autowired
	JwtService jwtService;	
	
	@Autowired
	SNSAccessTokenService SService;

	@Autowired
	UserAccountService userAccountService;
	
	@Autowired
	SNSDataService SDService;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		return "home";
	}
//	http://localhost:8080/account/kakaoLogin
	@RequestMapping(value = "/kakaoLogin", method = RequestMethod.GET)
	public String kakao(HttpServletResponse res) throws IOException {
		
		StringBuffer loginUrl = new StringBuffer();
		loginUrl.append("https://kauth.kakao.com/oauth/authorize?client_id=");
		loginUrl.append("5759bdc7b3bd7db0a39b00b177425462"); //카카오 앱에 있는 REST KEY
		loginUrl.append("&redirect_uri=");
		loginUrl.append("http://"+backurl+"/account/redirect"); //카카오 앱에 등록한 redirect URL
		loginUrl.append("&response_type=code");
		System.out.println(loginUrl.toString());
		return "redirect:"+loginUrl.toString();
	}	
	
	@RequestMapping(value = "/redirect", method = RequestMethod.GET)
	public String redirect(@RequestParam String code, HttpServletResponse res) throws IOException, ParseException {		
		//code
		//사용자가 취소 누르면 error 파라메터를 받음 
		// 그때 여기서 구분해야할듯
		Map<String, String> map = new HashMap<String, String>();
		map.put("grant_type", "=authorization_code");
		map.put("client_id", "=5759bdc7b3bd7db0a39b00b177425462"); //카카오 앱에 있는 REST KEY
		map.put("redirect_uri", "=http://"+backurl+"/account/redirect"); //카카오 앱에 등록한 redirect URL
		map.put("code", "="+code);
		
		String out = conn.HttpPostConnection("https://kauth.kakao.com/oauth/token", map).toString();
		ObjectMapper mapper = new ObjectMapper();
		String access_token = mapper.readTree(out).findValue("access_token").toString();
		System.out.println("access_token : " + access_token);
		map.clear();
		map.put("Authorization", "Bearer "+ access_token);
		out = conn.HttpPostConnection("https://kapi.kakao.com/v2/user/me", map).toString();
		String email = mapper.readTree(out).findValue("kakao_account").findValue("email").toString();
		String nickname = mapper.readTree(out).findValue("properties").findValue("nickname").toString();
		System.out.println("email : "+  email);
		System.out.println("nickname : "+  nickname);
		
		access_token = access_token.substring(1, access_token.length()-1);
		email = email.substring(1, email.length()-1);
		nickname = nickname.substring(1, nickname.length()-1);
		System.out.println(access_token);
		User user = userAccountService.getUserByEmail(email);
		if(user != null && (user.getLoginkind() & 2)> 1) { // 카카오 가입정보가 있을경우 로그인
			String token = jwtService.create(user, 1, access_token);
			SService.makeSNSData(token, email);
			return "redirect:http://"+fronturl+"/user/snsLogin";
		}
		if(user != null) { // 다른 종류 가입정보가 있을경우
			SDService.makeSNSData(nickname, email, 2);
			return "redirect:http://"+fronturl+"/user/snsCombine";
		}
		else { // 가입정보가 없는경우
			SDService.makeSNSData(nickname, email, 2);
			return "redirect:http://"+fronturl+"/user/snsRegist";
		}
//		
	}
	
	
}
