package com.vStock.service.impl;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsersDetailServiceImpl implements UserDetailsService {
	
//	@Autowired
//	private AdministratorDao dao;
	
	

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("loadUserByUsername(username): "+username);
		//查詢使用者帳戶是否存在
//		Optional<Administrator> userInfo = dao.findByAccount(username);
//		System.out.println("這個是userinfo: "+userInfo.orElseThrow().toString());
		//若不存在則返回username not found
//		if(!userInfo.isPresent()) throw new UsernameNotFoundException("user not found");
//		System.out.println("執行步驟B: "+userInfo);
		//若帳戶存在則取得其密碼與使用者權限資料
		
		
//		BCryptPasswordEncoder b = new BCryptPasswordEncoder();
//		String adminPassword = userInfo.get().getAdminPassword();
//		String encodedPas = b.encode(adminPassword);
//		System.out.println("這是加密後的密碼: "+encodedPas);
		
		
		
//		String adminRole = userInfo.get().getAdminRole();
//		System.out.println("資料庫密碼: "+adminPassword+"角色: "+adminRole);
		//返回輸入的使用者名稱(若資料庫內存在)以及其加密後的密碼和其使用者權限
		return new User(username,
//						adminPassword,
						"1234",
//						AuthorityUtils.commaSeparatedStringToAuthorityList(adminRole));
						AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
	}

}
