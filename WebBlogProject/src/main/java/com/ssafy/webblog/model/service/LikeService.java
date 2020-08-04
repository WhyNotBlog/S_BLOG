package com.ssafy.webblog.model.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.webblog.model.dto.Likearticle;
import com.ssafy.webblog.model.repo.LikearticleDao;

@Service
@Transactional
public class LikeService {

	@Autowired
	LikearticleDao lDao;

	//내가 좋아요 누른 게시글id의 목록
	public List<Integer> getAllArticleByUserid(int userid){
		List<Likearticle> likes= lDao.getLikeByUserid(userid);
		List<Integer> result = new ArrayList<Integer>();
		for(Likearticle like : likes) {
			result.add(like.getArticleid());
		}
		return result;
	}
	
	//게시글하나에 달린 좋아요 수
	public int getArticleLikeCountByArticleid(int articleid) {
		List<Likearticle> result = lDao.getLikeByArticleid(articleid);
		return result.size();
	}
	public Likearticle registLike(Likearticle like) {
		Likearticle result = lDao.save(like);
		return result;
	}
	
	public void deleteLike(Likearticle like) {
		lDao.delete(like);
	}
	
	public int getLikekeyByUseridAndArticleid(int userid, int articleid) {
		Likearticle result = lDao.getLikeByUseridAndArticleid(userid, articleid);
		return result.getLikekey();
	}

}
