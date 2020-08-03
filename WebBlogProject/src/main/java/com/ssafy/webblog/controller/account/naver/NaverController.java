package com.ssafy.webblog.controller.account.naver;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.ssafy.webblog.controller.account.kakao.HttpConnection;
import com.ssafy.webblog.controller.account.kakao.KakaoController;
import com.ssafy.webblog.model.dto.User;
import com.ssafy.webblog.model.service.JwtDecoder;
import com.ssafy.webblog.model.service.JwtService;
import com.ssafy.webblog.model.service.SNSAccessTokenService;
import com.ssafy.webblog.model.service.SNSDataService;
import com.ssafy.webblog.model.service.UserAccountService;

/**
 * Handles requests for the application home page.
 */
@Controller
@RequestMapping("/account")
@CrossOrigin(origins = { "*" }, maxAge = 6000)
public class NaverController {

	static Logger logger = LoggerFactory.getLogger(KakaoController.class);

	/* NaverLoginBO */
	private NaverLoginBO naverLoginBO;

	@Autowired
	private void setNaverLoginBO(NaverLoginBO naverLoginBO) {
		this.naverLoginBO = naverLoginBO;
	}

	@Value("${backurl}")
	private String backurl;
	@Value("${fronturl}")
	private String fronturl;
	
	@Autowired
	JwtService jwtService;

	@Autowired
	UserAccountService userAccountService;

	@Autowired
	SNSAccessTokenService SService;
	
	@Autowired
	SNSDataService SDService;

	// 로그인 첫 화면 요청 메소드
	@RequestMapping(value = "/naverlogin", method = { RequestMethod.GET})
	public String login(HttpSession session) {
		/* 네이버아이디로 인증 URL을 생성하기 위하여 naverLoginBO클래스의 getAuthorizationUrl메소드 호출 */
		String naverAuthUrl = naverLoginBO.getAuthorizationUrl(session);
		System.out.println("네이버:" + naverAuthUrl);
		/* 생성한 인증 URL을 View로 전달 */
		System.out.println("Re direct url : " + "redirect:" + naverAuthUrl);
		return "redirect:" + naverAuthUrl;
	}

	// 네이버 로그인 성공시 callback호출 메소드
	@RequestMapping(value = "/naverlogin/callback", method = { RequestMethod.GET})
	public String callback(@RequestParam String code, @RequestParam String state, HttpSession session)
			throws IOException, ParseException {
		System.out.println("여기는 callback");
		OAuth2AccessToken oauthToken;
		oauthToken = naverLoginBO.getAccessToken(session, code, state);
		// 로그인 사용자 정보를 읽어온다.
		String apiResult = naverLoginBO.getUserProfile(oauthToken);
		logger.info("API RESULT 결과값 :" + apiResult);
		// 리턴

		JSONParser parser = new JSONParser();
		Object obj = null;
		try {
			obj = parser.parse(apiResult);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		JSONObject jsonobj = (JSONObject) obj;
		JSONObject response = (JSONObject) jsonobj.get("response");

		String email = (String) response.get("email");
		String nickname = (String) response.get("nickname");
		logger.info("naver login  email:" + email);
		logger.info("naver login  nickname:" + nickname);

		User user = userAccountService.getUserByEmail(email);
		System.out.println(email);
		System.out.println(oauthToken.getAccessToken());
		if(user != null && (user.getLoginkind() & 4)> 1) { // 카카오 가입정보가 있을경우 로그인
			String token = jwtService.create(user, 2, oauthToken.getAccessToken());
			SService.makeSNSData(token, email);
			return "redirect:http://"+fronturl+"/user/snsLogin";
		}
		if(user != null) { // 다른 종류 가입정보가 있을경우
			SDService.makeSNSData(nickname, email, 4);
			return "redirect:http://"+fronturl+"/user/snsCombine";
		}
		else { // 가입정보가 없는경우
			SDService.makeSNSData(nickname, email, 4);
			return "redirect:http://"+fronturl+"/user/snsRegist";
		}
	}

}