package com.vStock.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vStock.model.NormalUser;

@Repository
public interface NormalUserDao extends JpaRepository<NormalUser, Integer>{

}
