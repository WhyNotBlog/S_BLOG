package com.ssafy.webblog.controller.account.normal;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ssafy.webblog.controller.account.kakao.HttpConnection;
import com.ssafy.webblog.model.dto.SignupInformation;
import com.ssafy.webblog.model.dto.User;
import com.ssafy.webblog.model.service.JwtDecoder;
import com.ssafy.webblog.model.service.JwtService;
import com.ssafy.webblog.model.service.SendEmailService;
import com.ssafy.webblog.model.service.UserAccountService;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/account")
public class RestAccountController {

	static Logger logger = LoggerFactory.getLogger(RestAccountController.class);

	@Autowired
	JwtService jwtService;

	@Autowired
	UserAccountService userAccountService;	

	@Autowired
	SendEmailService sService;	
	
	@Autowired
    private FileUploadDownloadService service;

	@Value("${backurl}")
	private String backurl;
	@Value("${fronturl}")
	private String fronturl;
	@Value("${profileUrl}")
	private String profileUrl;	
	
	
	@PostMapping("/login")
	@ApiOperation(value = "로그인")
	public ResponseEntity<Map<String, Object>> login(HttpServletResponse res, @RequestBody User user)
			throws JsonProcessingException, IOException {
		logger.debug("login : " + user.toString());
		ResponseEntity<Map<String, Object>> entity = null;
		try {
			User result = userAccountService.LoginUserByEmailAndPassword(user.getEmail(), user.getPassword());
			String token = jwtService.create(result, 0, "");
			res.setHeader("jwt-auth-token", token);
			System.out.println(token);
			result.setPassword("");
			entity = handleSuccess(result);
		} catch (RuntimeException e) {
			entity = handleException(e);
		}
		return entity;
	}

	@PostMapping("/regist")
	@ApiOperation(value = "회원가입")
	public ResponseEntity<Map<String, Object>> regist(HttpServletResponse res, @RequestBody User user)
			throws JsonProcessingException, IOException {
		logger.debug("login : " + user.toString());
		ResponseEntity<Map<String, Object>> entity = null;	

		try {
			User result = userAccountService.getUserByEmail(user.getEmail());
			String token = "";
        	String key = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789^*";
        	Random random = new Random();
        	for (int i = 0; i < 10; i++) {
        		int index = random.nextInt(key.length());
        		
        		token+=key.charAt(index)+"";
			}
        	
        	System.out.println(token);
        	
            SignupInformation signupInformation = 
            		new SignupInformation(null, user.getEmail(), user.getNickname(), user.getPassword(), null, null, user.getPicture(), user.getIntroduce(), user.getGiturl(), token);
            System.out.println(signupInformation.toString());
            
            sService.save(signupInformation);
            
            String url="http://"+fronturl+"/user/auth/"+signupInformation.getSignupid()+"/"+token;
            String subject = "회원가입 인증 메일"; // 메일 제목
            String message = "환영합니다! 회원가입을 위해 다음 링크를 클릭해주세요.\n"+url; // 메일 내용
             
            // SMTP 서버 설정
            final String host = "smtp.gmail.com"; // 사용할 smtp host, naver라면 smtp.naver.com
            final String accountId = "A601dummy";
            final String accountPwd = "@123@123";
            final int port = 465; // SMTP 포트
             
            String receiver = user.getEmail(); // 받는사람 이메일
            String sender = "ssafy@muticampus.com"; // 보내는사람 이메일
             
            // Property 정보 생성
            Properties props = System.getProperties();
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.port", port);
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.ssl.enable", "true");
            props.put("mail.smtp.ssl.trust", host);
             
            // 사용자 인증
            Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                protected javax.mail.PasswordAuthentication getPasswordAuthentication(){
                    return new javax.mail.PasswordAuthentication(accountId, accountPwd);
                }
            });
            session.setDebug(true);
             
            Message mimeMessage = new MimeMessage(session); //MimeMesage 생성
            
            try {
				mimeMessage.setFrom(new InternetAddress(sender));
			    mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(receiver)); 
	             
	            // Message Setting
	            mimeMessage.setSubject(subject);
	            mimeMessage.setText(message);
	            Transport.send(mimeMessage); // Transfer
	            
			} catch (AddressException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // 보내는 EMAIL (정확히 적어야 SMTP 서버에서 인증 실패되지 않음)
            
			entity = handleSuccess(result);
		} catch (RuntimeException e) {
			entity = handleException(e);
		}
		return entity;
	}	
	
	@PostMapping("/snsRegist")
	@ApiOperation(value = "sns회원가입")
	public ResponseEntity<Map<String, Object>> snsRegist(HttpServletResponse res, @RequestBody User user) {
		logger.debug("snsRegist : " + user.toString());
		ResponseEntity<Map<String, Object>> entity = null;	
		try {		
			userAccountService.insertUser(user);
			entity = handleSuccess(user);
		} catch (RuntimeException e) {
			entity = handleException(e);
		}
		return entity;
	}	
	
	@GetMapping("/authEmail/{signupid}/{token}")
	@ApiOperation(value = "인증메일수신")
	public ResponseEntity<Map<String, Object>> mailSender(@PathVariable int signupid, @PathVariable String token) throws AddressException, MessagingException{
		
		ResponseEntity<Map<String, Object>> entity = null;
		try {
			SignupInformation result = sService.getSignupInformationBySignupidAndToken(signupid, token);
			User user = new User(0, result.getEmail(), result.getNickname(), result.getPassword(), result.getCreatedate(), result.getPicture(), result.getIntroduce(), result.getGiturl(), 1);
			userAccountService.insertUser(user);
			sService.delete(result);
			entity = handleSuccess(user);
		} catch (RuntimeException e) {
			entity = handleException(e);
		}
		return entity;
	}

	@GetMapping("/getUserInfo/{nickname}")
	@ApiOperation(value = "유저 정보 조회")
	public ResponseEntity<Map<String, Object>> getUserInfo(HttpServletRequest res, @PathVariable String nickname)
			throws JsonProcessingException, IOException {
		logger.debug("get user info search by nickname : " + nickname);
		ResponseEntity<Map<String, Object>> entity = null;
		try {
			String temp = res.getHeader("jwt-auth-token");
			User result = userAccountService.getUserByNickname(nickname);
			result.setPassword("");
			entity = handleSuccess(result);
		} catch (RuntimeException e) {
			entity = handleException(e);
		}
		return entity;
	}

	@DeleteMapping("/delete/{nickname}")
	@ApiOperation(value = "유저 정보 삭제")
	public ResponseEntity<Map<String, Object>> userWithdrawal(HttpServletRequest res, @PathVariable String nickname)
			throws JsonProcessingException, IOException, ParseException {
		logger.debug("delete user email: " + nickname);
		ResponseEntity<Map<String, Object>> entity = null;
		try {
			userAccountService.deleteUser(nickname);
			String result = "";
			String jwt_token = res.getHeader("jwt-auth-token");
			JwtDecoder decoder = new JwtDecoder(jwt_token);
			Long loginKind = decoder.getLoginKind();
			String token = decoder.getAccessToken();
			if (loginKind == 0) {

			} else if (loginKind == 1) {
				result = kakaoLogout(token);
			} else if (loginKind == 2) {
				result = naverLogout(token);
			} else if (loginKind == 3) {

			}
			entity = handleSuccess("success");
		} catch (RuntimeException e) {
			entity = handleException(e);
		}
		return entity;
	}

	@PutMapping("/update")
	@ApiOperation(value = "유저 정보 변경")
	public ResponseEntity<Map<String, Object>> userInfoUpdate(HttpServletResponse res, @RequestBody User user)
			throws JsonProcessingException, IOException {
		logger.debug("update user before : " + userAccountService.getUserByEmail(user.getEmail()));
		logger.debug("update user after : " + user.toString());
		ResponseEntity<Map<String, Object>> entity = null;
		try {
			User temp = userAccountService.getUserByEmail(user.getEmail());
			user.setLoginkind(temp.getLoginkind());
			User result = userAccountService.updateUser(user);
			entity = handleSuccess(result);
		} catch (RuntimeException e) {
			entity = handleException(e);
		}
		return entity;
	}
	
	@PostMapping("/setProfile")
	@ApiOperation(value = "프로필 사진 업로드")
	public ResponseEntity<Map<String, Object>> setProfile(HttpServletResponse res, HttpServletRequest req, @RequestParam("file") MultipartFile[] file)
			throws JsonProcessingException, IOException {
		logger.debug("프로필 사진 업로드");
		ResponseEntity<Map<String, Object>> entity = null;		
		try {
			String filename = req.getHeader("filename");
			String realPath = System.getProperty("user.dir") + "\\profile\\" + filename;
			System.out.println(realPath);
			User user = userAccountService.getUserById(Integer.parseInt(filename));
			for (MultipartFile multipartFile : file) {			
				multipartFile.transferTo(new File(realPath + ".jpg"));
			}		
			user.setPicture(realPath + ".jpg");
			userAccountService.updateUser(user);
			entity = handleSuccess("");
		} catch (RuntimeException e) {
			entity = handleException(e);
		}
		return entity;
	}
	
	@PostMapping("/getProfile")
	@ApiOperation(value = "프로필 사진 업로드")
	public ResponseEntity<Map<String, Object>> getProfile(HttpServletResponse res, HttpServletRequest req, String email)
			throws JsonProcessingException, IOException {
		logger.debug("프로필 사진 가져오기");
		ResponseEntity<Map<String, Object>> entity = null;		
		try {
			String realPath = System.getProperty("user.dir") + "\\profile\\";
			File file = new File(realPath + email + ".jpg"); 
			MultipartFile multipartFile = (MultipartFile) file;
			multipartFile.transferTo(new File(realPath + "egawrgwag.jpg"));
			entity = handleSuccess("");
		} catch (RuntimeException e) {
			entity = handleException(e);
		}
		return entity;
	}

	HttpConnection conn = HttpConnection.getInstance();

	@PostMapping("/logout")
	public ResponseEntity<Map<String, Object>> logout(HttpServletRequest res)
			throws JsonProcessingException, IOException, ParseException {
		System.out.println("logout");
		ResponseEntity<Map<String, Object>> entity = null;
		try {
			String result = "";
			String jwt_token = res.getHeader("jwt-auth-token");
			JwtDecoder decoder = new JwtDecoder(jwt_token);
			Long loginKind = decoder.getLoginKind();
			String token = decoder.getAccessToken();
			if (loginKind == 0) {

			} else if (loginKind == 1) {
				result = kakaoLogout(token);
			} else if (loginKind == 2) {
				result = naverLogout(token);
			} else if (loginKind == 3) {

			}

			System.out.println(token);
			System.out.println("result : " + result);
			entity = handleSuccess("success");
		} catch (RuntimeException e) {
			entity = handleException(e);
		}
		return entity;
	}

	private String kakaoLogout(String token) throws IOException {
		Map<String, String> map = new HashMap<String, String>();
		map.put("Authorization", "Bearer " + token);
		String result = conn.HttpPostConnection("https://kapi.kakao.com/v1/user/unlink", map).toString();
		return result;
	}

	public String naverLogout(String token) throws JsonProcessingException, IOException {
		System.out.println("Naver logout");
		ResponseEntity<Map<String, Object>> entity = null;
		System.out.println(token);
		/* logout url making */
		String url = "https://nid.naver.com/oauth2.0/token?grant_type=delete";
		String client_id = "LwH6DZMYh297nQGCkkUi";
		String client_secret = "XJ3rMnBnbs";
		String service_provider = "NAVER";
		String logoutUrl = url + "&" + client_id + "&" + client_secret + "&" + token + "&" + service_provider;
		/* Naver Access token delete request */ 
		HttpConnection conn = HttpConnection.getInstance();
		String result = conn.HttpGetConnection(logoutUrl).toString();
		return result;
	}
	
	@GetMapping("/findNickname/{nickname}")
	@ApiOperation(value = "nickname")
	public ResponseEntity<Map<String, Object>> findId(@PathVariable String nickname) throws AddressException, MessagingException{
		
		ResponseEntity<Map<String, Object>> entity = null;
		User result = userAccountService.getUserByNickname(nickname);
		if(result != null) entity = handleSuccess(0);
		else entity = handleSuccess(1);
		return entity;
	}
	
	@GetMapping("/findEmail/{email}")
	@ApiOperation(value = "이메일중복확인")
	public ResponseEntity<Map<String, Object>> findEmail(@PathVariable String email) throws AddressException, MessagingException{
		
		ResponseEntity<Map<String, Object>> entity = null;
		User result = userAccountService.getUserByEmail(email);
		if(result != null) entity = handleSuccess(0);
		else entity = handleSuccess(1);
		return entity;
	}
	
	@PostMapping("/upload")
	@ApiOperation(value = "파일 업로드")
	public ResponseEntity<Map<String, Object>> getArticle(HttpServletResponse res, HttpServletRequest req, @RequestParam("file") MultipartFile[] file)
			throws JsonProcessingException, IOException {
		ResponseEntity<Map<String, Object>> entity = null;		
		try {
			String filename = req.getHeader("filename");
			String realPath = System.getProperty("user.dir") + profileUrl + filename;
			System.out.println(realPath);
			User user = userAccountService.getUserById(Integer.parseInt(filename));
			for (MultipartFile multipartFile : file) {			
				multipartFile.transferTo(new File(realPath + ".jpg"));
			}		
			user.setPicture(realPath + ".jpg");
			userAccountService.updateUser(user);
			entity = handleSuccess("");
		} catch (RuntimeException e) {
			entity = handleException(e);
		}
		return entity;
	}
	
    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) throws MalformedURLException{
         // Load file as Resource
        Resource resource = service.loadFileAsResource(fileName);
 
        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }
 
        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }
 
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }


	private ResponseEntity<Map<String, Object>> handleSuccess(Object data) {
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("status", true);
		resultMap.put("data", data);
		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	}

	private ResponseEntity<Map<String, Object>> handleException(Exception e) {
		logger.error("예외 발생 : ", e);
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("status", false);
		resultMap.put("data", e.getMessage());
		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}