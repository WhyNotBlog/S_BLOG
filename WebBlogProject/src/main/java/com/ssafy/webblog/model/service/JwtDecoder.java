package com.ssafy.webblog.model.service;


import org.apache.tomcat.util.codec.binary.Base64;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JwtDecoder {
	private static final Base64 base64Url = new Base64();
	private JSONObject jsonHeaderObject;
	private String jwtToken;

	public JwtDecoder(String jwtToken) {
		this.jwtToken = jwtToken;
	}

	public String isValid() throws ParseException {
		String[] jwtTokenValues = jwtToken.split("\\.");
		String jwtHeader = "";
		String jwtBody = "";

		if (jwtTokenValues.length > 0) {
			jwtHeader = new String(base64Url.decode(jwtTokenValues[0].getBytes()));
			System.out.println("JWT Header : " + jwtHeader);

//			JSONParser parser = new JSONParser();
//			try {
//				jsonHeaderObject = (JSONObject) parser.parse(jwtHeader);
//				System.out.println("JWT Header jsonHeaderObject : " + jsonHeaderObject);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
		}

		if (jwtTokenValues.length > 1) {
			jwtBody = new String(base64Url.decode(jwtTokenValues[1].getBytes()));
			System.out.println("JWT Body : " + jwtBody);
		}
		
		
		JSONParser parser = new JSONParser();
		Object obj = parser.parse( jwtBody );
		JSONObject jsonObj = (JSONObject) obj;

		Long loginKind = (Long) jsonObj.get("loginKind");
		String token =  (String) jsonObj.get("token"); 
		
		System.out.println("loginKind : " + loginKind);
		System.out.println("token : " + token);


		return jwtBody;
	}
	
	public Long getLoginKind() throws ParseException {
		String[] jwtTokenValues = jwtToken.split("\\.");
		String jwtBody = "";
		if (jwtTokenValues.length > 1) {
			jwtBody = new String(base64Url.decode(jwtTokenValues[1].getBytes()));
		}
		JSONParser parser = new JSONParser();
		Object obj = parser.parse( jwtBody );
		JSONObject jsonObj = (JSONObject) obj;

		Long loginKind = (Long) jsonObj.get("loginKind");
		
		return loginKind;
	}
	
	public String getAccessToken() throws ParseException {
		String[] jwtTokenValues = jwtToken.split("\\.");
		String jwtBody = "";
		if (jwtTokenValues.length > 1) {
			jwtBody = new String(base64Url.decode(jwtTokenValues[1].getBytes()));
		}
		JSONParser parser = new JSONParser();
		Object obj = parser.parse( jwtBody );
		JSONObject jsonObj = (JSONObject) obj;

		String accessToken = (String) jsonObj.get("token");
		
		return accessToken;
	}

}