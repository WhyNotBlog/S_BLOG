package com.ssafy.webblog.model.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.webblog.model.dto.Articletemp;
import com.ssafy.webblog.model.dto.User;
import com.ssafy.webblog.model.repo.ArticletempDao;
import com.ssafy.webblog.model.repo.UserDao;

@Service
@Transactional
public class ArticletempService {

	@Autowired
	ArticletempDao artiTempDao;
	
	/*
	 * 작성중인 글 임시저장에 필요한기능은? 임시글 저장/ 임시글 수정 /임시글 삭제/ 임시글 목록(임시글 리스트 반환) 불러오기 (유저 아이디로)/
	 * 임시글 한개(임시글 객체 한개 반환) 불러오기 (임시 글id로)
	 * 
	 */
	// 삽입
	public Articletemp insertArticletemp(Articletemp articletemp) {
		Articletemp result = artiTempDao.save(articletemp);
		return result;
	}

	// 삭제
	public void deleteArticletemp(String articleid) {
		Articletemp deleteArticletemp = artiTempDao.getArticletempByArticleid(Integer.parseInt(articleid));
		artiTempDao.delete(deleteArticletemp);
	}

	// 업데이트
	public Articletemp updateArticletemp(Articletemp articletemp) {
		Articletemp result = artiTempDao.save(articletemp);
		return result;
	}
	
	public List<Articletemp> getArticletempListByWriterid(int writerid){
		List<Articletemp> result = artiTempDao.getArticletempByWriterid(writerid);
		return result;
	}
	
	public Articletemp getArticletempByArticletempid(int articleid) {
		return artiTempDao.getArticletempByArticleid(articleid);
	}

}
