package com.cos.security1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cos.security1.auth.PrincipalDetails;
import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;


@Controller  // view를 리턴하겠다.
public class IndexController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@GetMapping("/test/login")
	public @ResponseBody String testLogin(Authentication authentication, 
			@AuthenticationPrincipal UserDetails userDetails) { //DI(의존성 주입)
//		@AuthenticationPrincipal PrincipalDetails userDetails
		System.out.println("/test/login ==========");
		// PrincipalDetails type 으로 down casting 
		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		System.out.println("authentication : "+ principalDetails.getUser());
		// @AuthenticationPrincipal 을 UserDetails를 상속한 PrincipalDetails type으로 선언하면
		// get User를 할 수 있다. ( OAuth 로그인에서는  ClassCastException error 발생)
//		System.out.println("userDetails : " + userDetails.getUser());
		System.out.println("userDetails : " + userDetails.getUsername());
		return "세션 정보 확인하기";
	}
	
	@GetMapping("/test/oauth/login")
	public @ResponseBody String testOAuthLogin(Authentication authentication,
			@AuthenticationPrincipal OAuth2User oauth) { //DI(의존성 주입)
		System.out.println("/test/oauth/login ==========");
		// google 로그인시 PrincipalDetails로 Casting이 안됨. 뭐로 해야하냐면
		OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
		System.out.println("authentication : "+ oauth2User.getAttributes());
		System.out.println("oauth2User: " + oauth.getAttributes());
		return "Oauth 세션 정보 확인하기";
	}
	
	@GetMapping({"", "/"})
	public String index() {
		// 머스테칫 템플릿 엔진 사용
		// src/main/resources/
		// ViewResolver 설정 : templates(prefix), mustache(suffix)  => 생략가능
		return "index";
	}
	
	@GetMapping("/user")
	public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
		System.out.println("principalDetails " + principalDetails.getUser());
		return "user";
	}
	
	@GetMapping("/admin")
	public @ResponseBody String admin() {
		return "admin";
	}
	
	@GetMapping("/manager")
	public @ResponseBody String manager() {
		return "manager";
	} 
	
	// Spring Security가 낚아챔 -> SecurityConfig 파일 생성 후 작동 안함(안낚아챔).
	@GetMapping("/loginForm")
	public String loginForm() {
		return "loginForm";
	}
	
	@GetMapping("/joinForm")
	public String joinForm() {
		return "joinForm";
	}
	
	@PostMapping("/join")
	public String join(User user) { // data만 주고 받음
		System.out.println(user);
		user.setRole("ROLE_USER");
		String rawPassword = user.getPassword();
		String encPassword = bCryptPasswordEncoder.encode(rawPassword);
		user.setPassword(encPassword);
		userRepository.save(user); // 회원가입은 되지만 비밀번호가 노출됨.
		// 패스워드가 암호화가 되지 않았기 때문에 시큐리티로 로그인을 할 수 없음.
		return "redirect:/loginForm"; // 함수 재사용 가능
	}
	
	// antMatchers("/manager/**").access("hasRole('ROLE_ADMIN')") 대신 어노테이션으로 대체 가능.
	@Secured("ROLE_ADMIN")
	@GetMapping("/info")
	public @ResponseBody String info() {
		return "개인정보";
	}
	
	// @Secured랑 같은 기능. 여러개 걸고 싶을때 사용.
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
	@GetMapping("/data")
	public @ResponseBody String data() {
		return "개인정보";
	}
}
