package com.ssafy.webblog.model.service;
import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ThumbnailUploadDownloadService {
	 private final Path fileLocation;
	    
	    @Autowired
	    public ThumbnailUploadDownloadService(ThumbnailUploadProperties prop) throws FileUploadException {
	        this.fileLocation = Paths.get(prop.getUploadDir())
	                .toAbsolutePath().normalize();
	        
	        try {
	            Files.createDirectories(this.fileLocation);
	        }catch(Exception e) {
	            throw new FileUploadException("파일을 업로드할 디렉토리를 생성하지 못했습니다.", e);
	        }
	    }
	    public String storeFile(MultipartFile file, String articleNum) throws FileUploadException {
//	        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
	        String fileName = articleNum + ".jpg";
	        System.out.println(fileName);
	        try {
	            // 파일명에 부적합 문자가 있는지 확인한다.
	            if(fileName.contains(".."))
	                throw new FileUploadException("파일명에 부적합 문자가 포함되어 있습니다. " + fileName);
	            
	            Path targetLocation = this.fileLocation.resolve(fileName);
	            
	            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
	            
	            return fileName;
	        }catch(Exception e) {
	            throw new FileUploadException("["+fileName+"] 파일 업로드에 실패하였습니다. 다시 시도하십시오.",e);
	        }
	    }
	    public Resource loadFileAsResource(String fileName) throws MalformedURLException {
	        try {
	            Path filePath = this.fileLocation.resolve(fileName).normalize();
	            System.out.println(filePath);
	            Resource resource = new UrlResource(filePath.toUri());
	            
	            if(resource.exists()) {
	                return resource;
	            }
	        }catch(MalformedURLException e) {
	            throw e;
	        }
			return null;
	    }
	    public String rename(File file, String articleNum, String newArticleNum) throws FileUploadException {
//	        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
	        String fileName = articleNum + ".jpg";
	        System.out.println(fileName);
	        try {
	            // 파일명에 부적합 문자가 있는지 확인한다.
	            if(fileName.contains(".."))
	                throw new FileUploadException("파일명에 부적합 문자가 포함되어 있습니다. " + fileName);
	            
	            Path targetLocation = this.fileLocation.resolve(fileName);
	            System.out.println("파일이름 : " + targetLocation.getFileName());
	            System.out.println(targetLocation.getParent());
	            file.renameTo(new File(targetLocation.getParent() + "/" + newArticleNum + ".jpg"));
	            
	            return fileName;
	        }catch(Exception e) {
	            throw new FileUploadException("["+fileName+"] 파일 업로드에 실패하였습니다. 다시 시도하십시오.",e);
	        }
	    }


}
