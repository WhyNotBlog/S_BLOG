package com.ssafy.webblog.model.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.webblog.model.dto.Tagkind;

public interface TagkindDao extends JpaRepository<Tagkind, String> {
	List<Tagkind> findTop5ByOrderByTagcountDesc();

	Object deleteTagkindByTagname(String tagname);

	Tagkind findTagkindByTagname(String tagname);
}
