package com.ssafy.webblog.model.service;

import java.util.ArrayList;
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

	//내가 좋아요 누른 게시글id의 목록
	public List<Integer> getAllArticleByUserid(int userid){
		List<Like> likes= lDao.getLikeByUserid(userid);
		List<Integer> result = new ArrayList<Integer>();
		for(Like like : likes) {
			result.add(like.getArticleid());
		}
		return result;
	}
	
	//게시글하나에 달린 좋아요 수
	public int getArticleLikeCountByArticleid(int articleid) {
		List<Like> result = lDao.getLikeByArticleid(articleid);
		return result.size();
	}
	public Like registLike(Like like) {
		Like result = lDao.save(like);
		return result;
	}
	
	public void deleteLike(Like like) {
		lDao.delete(like);
	}
	
	public int getLikekeyByUseridAndArticleid(int userid, int articleid) {
		Like result = lDao.getLikeByUseridAndArticleid(userid, articleid);
		return result.getLikekey();
	}

}
