package com.ssafy.webblog.controller.article;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ssafy.webblog.model.dto.Articletemp;
import com.ssafy.webblog.model.dto.Tagtemp;
import com.ssafy.webblog.model.service.ArticletempService;
import com.ssafy.webblog.model.service.FileUploadResponse;
import com.ssafy.webblog.model.service.TagtempService;
import com.ssafy.webblog.model.service.ThumbnailUploadDownloadService;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/articletemp")
public class RestArticletempController {

	static Logger logger = LoggerFactory.getLogger(RestArticletempController.class);

	@Autowired
	ArticletempService artiTempService;

	@Autowired
	TagtempService ttService;

	@GetMapping("/{articleid}")
	@ApiOperation(value = "임시 게시글 조회")
	public ResponseEntity<Map<String, Object>> getArticletemp(HttpServletRequest req, HttpServletResponse res, @PathVariable String articleid)
			throws JsonProcessingException, IOException {
		logger.debug("Articletemp select by article id: " + articleid);
		ResponseEntity<Map<String, Object>> entity = null;
		try {
			Articletemp result = artiTempService.getArticletempByArticletempid(Integer.parseInt(articleid));
			logger.debug(result.toString());
			entity = handleSuccess(result);
		} catch (RuntimeException e) {
			entity = handleException(e);
		}
		return entity;
	}

	@PostMapping("/regist")
	@ApiOperation(value = "임시 게시글 등록")
	public ResponseEntity<Map<String, Object>> articletempRegist(HttpServletRequest req, HttpServletResponse res, @RequestBody Articletemp articletemp)
			throws IOException {
		logger.debug("Articletemp regist : " + articletemp.toString());
		ResponseEntity<Map<String, Object>> entity = null;
		try {
			Articletemp result = artiTempService.insertArticletemp(articletemp);
			entity = handleSuccess(result);
		} catch (RuntimeException e) {
			entity = handleException(e);
		}
		return entity;
	}


	@DeleteMapping("/delete/{articleid}")
	@ApiOperation(value = "임시 게시글 삭제")
	public ResponseEntity<Map<String, Object>> articletempDelete(HttpServletRequest req, HttpServletResponse res, @PathVariable String articleid)
			throws IOException {
		logger.debug("delete articletemp by articleid: " + articleid);
		ResponseEntity<Map<String, Object>> entity = null;
		try {
			//임시 게시글 지우고
			artiTempService.deleteArticletemp(articleid);
			//임시게시글에 등록된 태그 전부 삭제
			List<Tagtemp> deleteTagtempList = ttService.getTagtempListByArticleid(Integer.parseInt(articleid));
			for(Tagtemp tagtemp : deleteTagtempList) {
				ttService.deleteTagtemp(tagtemp.getTagid());
			}
			//성공 반환
			entity = handleSuccess("success");
		} catch (RuntimeException e) {
			entity = handleException(e);
		}
		return entity;
	}

	@PutMapping("/update")
	@ApiOperation(value = "임시 게시글 수정")
	public ResponseEntity<Map<String, Object>> articletempUpdate(HttpServletRequest req, HttpServletResponse res, @RequestBody Articletemp articletemp)
			throws  IOException {
		logger.debug("update article temp before : " + artiTempService.getArticletempByArticletempid(articletemp.getArticleid()));
		logger.debug("update article temp after : " + articletemp.toString());
		ResponseEntity<Map<String, Object>> entity = null;
		try {
			Articletemp result = artiTempService.updateArticletemp(articletemp);
			entity = handleSuccess(result);
		} catch (RuntimeException e) {
			entity = handleException(e);
		}
		return entity;
	}

	@GetMapping("/user/{userid}")
	@ApiOperation(value = "유저가 작성중인 임시 게시글 조회")
	public ResponseEntity<Map<String, Object>> getArticletempBy(HttpServletRequest req, HttpServletResponse res, @PathVariable int userid)
			throws JsonProcessingException, IOException {
		logger.debug("Articletemp select by user id: " + userid);
		ResponseEntity<Map<String, Object>> entity = null;
		try {
			List<Articletemp> result = artiTempService.getArticletempListByWriterid(userid);
			logger.debug(result.toString());
			entity = handleSuccess(result);
		} catch (RuntimeException e) {
			entity = handleException(e);
		}
		return entity;
	}
	
	@Autowired
	private ThumbnailUploadDownloadService service;
	
	@PostMapping("/uploadThumbnail")
	public FileUploadResponse uploadFile(HttpServletRequest req, @RequestParam("file") MultipartFile file) {
		String fileName = "";
		try {
			String articleNum = req.getHeader("articleNum");
			fileName = service.storeFile(file, "temp_" + articleNum);
			System.out.println(fileName);
		} catch (FileUploadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
				.path("/downloadFile/")
				.path(fileName)
				.toUriString();

		System.out.println(fileDownloadUri);

		return new FileUploadResponse(fileName, fileDownloadUri, file.getContentType(), file.getSize());
	}



	@GetMapping("/downloadThumbnail/{fileName:.+}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) throws MalformedURLException{
		// Load file as Resource
		Resource resource = service.loadFileAsResource("temp_" + fileName);
		// Try to determine file's content type
		String contentType = null;
		try {
			System.out.println(resource.getFile().getAbsolutePath());
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