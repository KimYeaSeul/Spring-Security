package com.cos.security1.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.cos.security1.config.ouath.Principaloauth2UserService;
//
// 1. 코드받기(인증완료) 2. access token 받기(권한) 
// 3. 사용자 프로필 정보를 가져옴 
// 4-1. 사용자 정보를 토대로 회원가입을 자동으로 시켜주거나
// 4-2. 추가정보가 필요할 경우 회원가입 절차 진행
@EnableWebSecurity   // 스프링 시큐리티 필터가 스프링 필터 체인에 등록이 됩니다.
@EnableGlobalMethodSecurity(securedEnabled=true, prePostEnabled = true) // Secured 어노테이션 활성화, PreAuthorize,PostAuthorize 어노테이션 활성화.
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	@Autowired
	private Principaloauth2UserService principaloauth2UserService;
	
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
			.defaultSuccessUrl("/")
			.and()
			.oauth2Login()
			.loginPage("/loginForm") // 구글 로그인 화면 표시해줌. 구글 로그인이 완료된 뒤의 후처리가 필요함.
			// tip. 라이브러리를 사용하면 코드 안받고 엑세스토큰 + 사용자프로필정보를 한번에 받음
			.userInfoEndpoint()
			.userService(principaloauth2UserService);
	}
	
}
