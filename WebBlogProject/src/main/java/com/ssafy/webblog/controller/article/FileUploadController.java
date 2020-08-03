package com.ssafy.webblog.controller.article;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ssafy.webblog.model.dto.User;
import com.ssafy.webblog.model.service.UserAccountService;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/article")
public class FileUploadController {
	
	static Logger logger = LoggerFactory.getLogger(RestArticleController.class);
	@Autowired
	UserAccountService userAccountService;	
	
	@Value("${profileUrl}")
	private String profileUrl;
	
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
	
	@Autowired
    private FileUploadDownloadService service;
    
    
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
