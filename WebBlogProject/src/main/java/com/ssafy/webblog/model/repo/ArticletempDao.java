package com.ssafy.webblog.model.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.webblog.model.dto.Articletemp;

public interface ArticletempDao extends JpaRepository<Articletemp, String> {
	
	// 유저가 쓴 임시저장 목록 전체 반환
	List<Articletemp> getArticletempByWriterid(int writerid);
	
	//article id로  temp article 조회 >> 삭제할 때 id 로 찾기 위함
	Articletemp getArticletempByArticleid(int articleid);

}
