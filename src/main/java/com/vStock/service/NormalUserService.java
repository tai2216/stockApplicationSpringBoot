package com.vStock.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface NormalUserService {
	
	public void registerUser(HttpServletRequest req, HttpServletResponse res);
	
	public void enableUser(HttpServletRequest req, HttpServletResponse res);
	
	public void updateLoginDate(String username);

}
