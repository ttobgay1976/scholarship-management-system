package com.sprms.system.master.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sprms.system.hbmbeans.GewogM;


@Repository
public interface GewogRepository extends JpaRepository<GewogM, Long>{

	/* List<GewogM> findByDzongkhag_DzongkhagId(Integer dzongkhagId); */
	
	List<GewogM> findByDzongkhag_DzongkhagId(Long dzongkhagId);
}
