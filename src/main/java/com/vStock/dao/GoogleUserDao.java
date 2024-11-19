package com.vStock.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vStock.model.GoogleUser;

@Repository
public interface GoogleUserDao extends JpaRepository<GoogleUser, Integer>{
	
	@Query(nativeQuery = true
			,value="SELECT * FROM GOOGLE_USER WHERE USERNAME = :username")
	@Transactional(readOnly = true)
	public Optional<GoogleUser> findByUsername(String username);
	
}
