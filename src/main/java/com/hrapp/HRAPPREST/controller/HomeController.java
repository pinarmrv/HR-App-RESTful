package com.hrapp.HRAPPREST.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

	@GetMapping("/")
	@ResponseBody
	public String home() {
		String response = "Welcome to the RESTful library system!";

		return response;
	}
}
