package com.ssafy.webblog.model.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.webblog.model.dto.Tagtemp;

public interface TagtempDao extends JpaRepository<Tagtemp, String> {
	List<Tagtemp> getTagtempByArticleid(int articleid);
	//삭제할때 찾기 위함
	Tagtemp getTagtempByArticleidAndTagname(int articleid, String tagname);
	
	Tagtemp getTagtempByTagid(int tagid);

	List<Tagtemp> getTagtempByTagname(String tagname);
}
