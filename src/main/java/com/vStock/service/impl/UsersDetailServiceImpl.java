package com.vStock.service.impl;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.vStock.dao.NormalUserDao;
import com.vStock.model.NormalUser;

@Service
public class UsersDetailServiceImpl implements UserDetailsService {
	
	@Autowired
	private NormalUserDao normalUserdao;
	
	private static final Logger logger = LogManager.getLogger(UsersDetailServiceImpl.class);

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		logger.info("loadUserByUsername(username): "+username);
		//查詢使用者帳戶是否存在
		Optional<NormalUser> userInfo = normalUserdao.findByUsername(username);
		logger.info("這個是userinfo: "+userInfo.orElseThrow(()->new UsernameNotFoundException("查無使用者")));
		return User.builder()
					.username(username)
					.password(userInfo.get().getPassword())
					.authorities(AuthorityUtils.commaSeparatedStringToAuthorityList(userInfo.get().getUserRole()))
					.disabled(userInfo.get().isEnabled()? false : true)
					.build();
	}

}
