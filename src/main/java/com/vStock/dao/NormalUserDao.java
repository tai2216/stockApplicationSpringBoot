package com.vStock.dao;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vStock.model.NormalUser;

@Repository
public interface NormalUserDao extends JpaRepository<NormalUser, Integer>{
	@Query(nativeQuery = true
			,value="SELECT * FROM NORMAL_USER WHERE USERNAME = :username")
	@Transactional(readOnly = true)
	public Optional<NormalUser> findByUsername(String username);
	
	@Query(nativeQuery = true
			,value="SELECT * FROM NORMAL_USER WHERE USERNAME = :username AND REMARK = :remark")
	@Transactional(readOnly = true)
	public Optional<NormalUser> findByUsernameAndRemark(String username,String remark);
	
	@Query(nativeQuery = true
			,value="SELECT * FROM NORMAL_USER WHERE REMARK LIKE :startWith%")
	@Transactional(readOnly = true)
	public List<NormalUser> findByRemarkStartWith(String startWith);
	
	@Query(nativeQuery = true
			, value = "UPDATE NORMAL_USER SET ENABLED = 1, ENABLED_DATE =:date WHERE USER_ID = :id")
	@Modifying(clearAutomatically = true, flushAutomatically = true)
	public void enableUser(int id, Timestamp date);
	
	@Query(nativeQuery = true
			, value = "UPDATE NORMAL_USER SET LAST_LOGIN_DATE = :date WHERE USER_ID = :id")
	@Modifying(clearAutomatically = true, flushAutomatically = true)
	public void updateLoginDate(int id, Timestamp date);
	
	@Query(nativeQuery = true
			, value = "UPDATE NORMAL_USER SET REMARK = :remark WHERE USERNAME = :username")
	@Modifying(clearAutomatically = true, flushAutomatically = true)
	public void updateRemarkByUsername(String username, String remark);
	
	@Query(nativeQuery = true
			, value = "UPDATE NORMAL_USER SET PASSWORD = :newPassword WHERE USER_ID = :id")
	@Modifying(clearAutomatically = true, flushAutomatically = true)
	public void updatePassword(int id, String newPassword);
	
	@Query(nativeQuery = true
			, value = "UPDATE NORMAL_USER SET ENABLED = 0 WHERE USER_ID = :id")
	@Modifying(clearAutomatically = true, flushAutomatically = true)
	public void disableUser(int id);
	
	@Query(nativeQuery = true
			, value = "SELECT * FROM NORMAL_USER WHERE EMAIL = :email")
	@Transactional(readOnly = true)
	public Optional<NormalUser> findByEmail(String email);
	
	
	
}
