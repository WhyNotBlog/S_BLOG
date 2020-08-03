package com.ssafy.webblog.model.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.webblog.model.dto.Like;
import com.ssafy.webblog.model.repo.LikeDao;

@Service
@Transactional
public class LikeService {

	@Autowired
	LikeDao lDao;

	//게시글 리스트를 볼 때 게시글들 좋아요
	public List<Like> getAllArticleLike(int articleid){
		List<Like> result = lDao.getLikeByArticleid(articleid);
		return result;
	}

}
