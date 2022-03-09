package com.cos.security1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller  // view를 리턴하겠다.
public class IndexController {

	@GetMapping({"", "/"})
	public String index() {
		// 머스테칫 템플릿 엔진 사용
		// src/main/resources/
		// ViewResolver 설정 : templates(prefix), mustache(suffix)  => 생략가능
		return "index";
	}
}
