package com.vStock.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vStock.model.JwtSecretKey;

@Repository
public interface JwtSecretKeyDao extends JpaRepository<JwtSecretKey, Integer>{

}
