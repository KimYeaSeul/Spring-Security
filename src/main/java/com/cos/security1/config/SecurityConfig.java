package com.cos.security1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity   // 스프링 시큐리티 필터가 스프링 필터 체인에 등록이 됩니다.
@EnableGlobalMethodSecurity(securedEnabled=true, prePostEnabled = true) // Secured 어노테이션 활성화, PreAuthorize,PostAuthorize 어노테이션 활성화.
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	
 	@Bean //해당 메서드의 리턴되는 오브젝트를 IoC로 등록해준다. 어디서든 쓸수있음.
	public BCryptPasswordEncoder encodePwd() {
		return new BCryptPasswordEncoder();
	}
 	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.authorizeRequests()
			.antMatchers("/user/**").authenticated()
			.antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
			.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
			.anyRequest().permitAll()
			.and()
			.formLogin()
			.loginPage("/loginForm")
			.loginProcessingUrl("/login") // login주소가 호출되면 시큐리티가 낚아채서 대신 로그인을 진행해줌.
			.defaultSuccessUrl("/");
	}
	
}
