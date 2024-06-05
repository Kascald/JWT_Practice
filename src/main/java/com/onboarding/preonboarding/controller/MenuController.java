package com.onboarding.preonboarding.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MenuController {
	@RequestMapping(value = "/list" , method = RequestMethod.GET)
	public String list() {
		return "item/list";
	}

	@RequestMapping(value = "/signup" , method = RequestMethod.GET)
	public String signup() {
		return "user/signup";
	}

	@RequestMapping(value = "/login" , method = RequestMethod.GET)
	public String login() {
		return "user/login";
	}
	@RequestMapping(value = "/logout" , method = RequestMethod.GET)
	public String logout() {
		return "user/logout";
	}

}
