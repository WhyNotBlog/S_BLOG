package com.ssafy.webblog.model.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.webblog.model.dto.Tag;

public interface TagDao extends JpaRepository<Tag, String> {
	List<Tag> getTagByArticleid(int articleid);
	
	Tag getTagByArticleidAndTagname(int articleid, String tagname);
	
	Tag getTagByTagid(int tagid);

	List<Tag> getTagByTagname(String tagname);
}
