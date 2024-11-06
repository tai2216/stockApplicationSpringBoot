package com.vStock.config;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.vStock.config.filter.JWTAuthenticationFilter;
import com.vStock.config.filter.JWTLoginFilter;
import com.vStock.dao.JwtSecretKeyDao;
import com.vStock.model.JwtSecretKey;

//繼承 WebSecurityConfigurerAdapter才可自訂登入邏輯
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
//	@Autowired
//	private UsersDetailServiceImpl service;
	
	@Autowired
	private JwtSecretKeyDao jwtSecretKeyDao;
	
	@Autowired
	private UserDetailsService service;

//	@Autowired
//	private CorsFilter corsFilter;
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		try {
			//每次啟動應用都會重新生成一組jwtSecretKey並存入資料庫
			jwtSecretKeyDao.deleteAll();
			jwtSecretKeyDao.flush();
			jwtSecretKeyDao.save(JwtSecretKey.builder().id(1).jwtKey(generateRandomKey(new Random().nextInt(100,200))).build());
			String jwtSecretkey = jwtSecretKeyDao.findAll().get(0).getJwtKey();
			http
			.authorizeRequests()
			.antMatchers("/login","/checkLogin","/oauth2/authorization/google","/register","/enableUser")
			.permitAll()
			.antMatchers("/css/**/**","/images/**/**","/js/**","/default/**","/layout/**","/img/**","/Message/**")
			.permitAll()
			.anyRequest()
			.authenticated()
			.and()
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)//若這段啟用則會導致google第三方登入無法記錄使用者
			.and()
			.addFilter(new JWTLoginFilter(authenticationManager(),jwtSecretkey))
			.addFilter(new JWTAuthenticationFilter(authenticationManager(),jwtSecretkey))
			.csrf().disable()
			.formLogin()
			.disable()
//			.oauth2Login()
//				.loginPage("/oauth2/authorization/google")
//				.successHandler((req,res,auth)->{
//					OAuth2User user = (OAuth2User) auth.getPrincipal();
//					System.out.println("有近來success");
//						user.getAttributes().forEach((k, v) -> {
//							System.out.println(k + ":" + v);
//						});
//						user.getAuthorities().forEach(System.out::println);
//				})
//				.userInfoEndpoint()
//				.oidcUserService(oidcUserService())
//			.and()
//				.defaultSuccessUrl("/home",true)
//			.and()	
//		        .logout()
//	            .logoutUrl("/logout")
//	            .deleteCookies("JSESSIONID")
//	            .invalidateHttpSession(true)
//	            .clearAuthentication(true)
//	            .logoutSuccessUrl("/testLogout");
			;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//登出	
//		http.logout()
//			.deleteCookies("JSESSIONID")
//			.logoutSuccessUrl("/Backstage/adminLogin")
//			
//			//可以使用任何方法登出(不建議
//			.logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
		
		
//		這邊使用 authorizeRequests 方法開始自訂授權規則。
//		使用 antMatchers 方法，傳入 HTTP 請求方法與 API 路徑，
//		後面接著授權方式，這樣就定義好一個規則了。
//		傳入的 API 路徑可利用「萬用字元」，以同時適用多個路徑，
//		以下是萬用字元的用法。
//
//		「*」：代表0到多個字元。如「/products/*」適用於「/products」、「/products/123」，但不適用「/products/123/draft」
//		「**」：代表0到多個路徑。如「/products/**」適用於「/products」底下任何路徑。
//		「?」：代表一個字元。如「/products/?*」適用於「/products/1」、「/products/123」，但不適用「/products」。
	}
	
//	@Override
//	public void configure(WebSecurity web) throws Exception {
//		web.ignoring().antMatchers("/css/**/**","/images/**/**","/js/**","/default/**","/layout/**","/img/**","/Message/**");
//	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		try {
			auth.userDetailsService(service)
			.passwordEncoder(PasswordEncoder());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//必須建立此方法將密碼加密 建立密碼演算的instance
	@Bean
	public BCryptPasswordEncoder PasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
//    @Bean
//    public OidcUserService oidcUserService() {
//    	OidcUserService service = new OidcUserService();
//    	service.setOauth2UserService(new com.vStock.service.impl.MyOidcUserServiceImpl());
//    	return service;
//    }
	
	/*
	 * 動態生成Jwt Token的Secret Key
	 * */
    public static String generateRandomKey(int length) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] key = new byte[length];
        secureRandom.nextBytes(key);
        return Base64.getEncoder().encodeToString(key);
    }

}
