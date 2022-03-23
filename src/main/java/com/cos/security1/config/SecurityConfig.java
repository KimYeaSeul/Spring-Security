package com.cos.security1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity   // 스프링 시큐리티 필터가 스프링 필터 체인에 등록이 됩니다.
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	
 	@Bean //해당 메서드의 리턴되는 오브젝트를 IoC로 등록해준다. 어디서든 쓸수있음.
	public BCryptPasswordEncoder encodePwd() {
		return new BCryptPasswordEncoder();
	}
 	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.authorizeHttpRequests()
			.antMatchers("/user/**").authenticated()
			.antMatchers("/manager/**").hasRole("ROLE_ADMIN or ROLE_MANAGER")
			.antMatchers("/admin/**").hasRole("ROLE_ADMIN")
			.anyRequest().permitAll()
			.and()
			.formLogin()
			.loginPage("/loginForm");
	}
	
}