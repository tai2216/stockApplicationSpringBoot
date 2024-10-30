package com.vStock.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.vStock.model.NormalUser;

@Repository
public interface NormalUserDao extends JpaRepository<NormalUser, Integer>{
	@Query(nativeQuery = true
			,value="SELECT * FROM NORMAL_USER WHERE USERNAME = :username")
	public Optional<NormalUser> findByUsername(String username);
	
	@Query(nativeQuery = true
			, value = "SELECT * FROM NORMAL_USER WHERE EMAIL = :email")
	public Optional<NormalUser> findByEmail(String email);
}
