package com.ssafy.webblog;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.ssafy.webblog.interceptor.JwtInterceptor;
import com.ssafy.webblog.model.service.FileUploadProperties;
import com.ssafy.webblog.model.service.ThumbnailUploadProperties;

@SpringBootApplication
@EnableConfigurationProperties({
    ThumbnailUploadProperties.class,
    FileUploadProperties.class
})

public class WebBlogProjectApplication implements WebMvcConfigurer{
	
	@Value("${backurl}")
	private static String backurl;
	@Value("${fronturl}")
	private static String fronturl;
	@Value("${profileUrl}")
	private static String profileUrl;	
	

	public static void main(String[] args) {
		SpringApplication.run(WebBlogProjectApplication.class, args);
		System.out.println(backurl);
		System.out.println(fronturl);
		System.out.println(profileUrl);
		
	}

	@Autowired
	private JwtInterceptor jwtInterceptor;

	public void addInterceptors(InterceptorRegistry registry) {
	      registry.addInterceptor(jwtInterceptor).
	      addPathPatterns("/account/**").
	      addPathPatterns("/article/**").
	      addPathPatterns("/comment/**").
	      addPathPatterns("/like/**").
	      addPathPatterns("/articletemp/**").
	      addPathPatterns("/tag/**").
	      addPathPatterns("/tagtemp/**").
	      excludePathPatterns(Arrays.asList(	    		  
	    		  "/account/login", 
	    		  "/account/sendMail/**", 
	    		  "/account/signup", 
	    		  "/account/redirect",
	    		  "/account/logout", 
	    		  "/account/kakaoLogin",
	    		  "/account/getSNSToken", 
	    		  "/account/getSNSData", 
	    		  "/account/naverlogin",
	    		  "/account/naverlogin/callback",
	    		  "/account/regist", 
	    		  "/account/authEmail/**",
	    		  "/account/findNickname/**",
	    		  "/account/findEmail/**", 
	    		  "/account/delete/**", 
	    		  "/account/getUserInfoById/**", 
	    		  "/account/getSNSData/**",
	    		  "/account/snsRegist", 
	    		  "/account/getProfile", 
	    		  "/account/setProfile", 
	    		  "/account/combineId",
	    		  "/account/downloadFile/**",
	    		  "/like/articlelist/**",
	    		  "/article/searchBy/**",
	    		  "/article/user/**",
	    		  "/article/downloadThumbnail/**",
	    		  "/article/uploadThumbnail/**",
	    		  "/article/detail/**",
	    		  "/article/visit/**",
	    		  "/comment/article/**",
	    		  "/tagtemp/taglist/**",
	    		  "/tag/tentaglist",
	    		  "/tag/taglist/**"
	    		  )
	    		  ); // 적용 제외 경로
	   }
	// Interceptor를 이용해서 처리하므로 전역의 Cross origin 처리를 해준다.
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
		.allowedOrigins("*")
		.allowedMethods("*")
		.allowedHeaders("*")
		.exposedHeaders("jwt-auth-token");
	}

}
