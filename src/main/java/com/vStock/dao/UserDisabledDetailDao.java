package com.vStock.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vStock.model.UserDisabledDetail;

@Repository
public interface UserDisabledDetailDao extends JpaRepository<UserDisabledDetail, Integer>{

}
