package com.onboarding.preonboarding.controller;


import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
@ResponseBody
@RequestMapping("/roleTest")
public class TestController {
	@GetMapping("/test")
	public String index() {
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		return "HELLO " + name;
	}

	@GetMapping("/admin")
	public String admin() {
		return "admin confirm";
	}
}
