package com.vStock.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

@Service
public interface NormalUserService {
	
	public void registerUser(HttpServletRequest req, HttpServletResponse res);
	
	public void enableUser(String username,HttpServletRequest req, HttpServletResponse res);

}
