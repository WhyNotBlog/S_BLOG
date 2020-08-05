package com.ssafy.webblog.model.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.webblog.model.dto.Tagkind;
import com.ssafy.webblog.model.repo.TagkindDao;

@Service
@Transactional
public class TagkindService {

	@Autowired
	TagkindDao tDao;

	public Tagkind insertTagkind(Tagkind tagkind) {
		Tagkind result = tDao.save(tagkind);
		return result;
	}
	
	public List<Tagkind> getTagkind(){
		return tDao.findTop5ByOrderByTagcountDesc();
	}

	public void delete(String tagname) {
		tDao.deleteTagkindByTagname(tagname);		
	}
}
