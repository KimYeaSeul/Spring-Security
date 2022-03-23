package com.cos.security1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;


@Controller  // view를 리턴하겠다.
public class IndexController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@GetMapping({"", "/"})
	public String index() {
		// 머스테칫 템플릿 엔진 사용
		// src/main/resources/
		// ViewResolver 설정 : templates(prefix), mustache(suffix)  => 생략가능
		return "index";
	}
	
	@GetMapping("/user")
	public String user() {
		return "user";
	}
	
	@GetMapping("/admin")
	public String admin() {
		return "admin";
	}
	
	@GetMapping("/manager")
	public String manager() {
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
}
