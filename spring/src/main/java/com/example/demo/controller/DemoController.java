package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.DemoService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class DemoController {

	@Autowired
	private DemoService demoService;

	@GetMapping("/")
	public String inidex() {
		return health();
	}

	@GetMapping("/health")
	public String health() {
		log.info("health check OK!");
		return "OK";
	}

	// curl -X POST localhost:8080/demo --header "Content-Type: application/json;charset=utf-8" --data '{"val":100}' | jq

	@PostMapping(value = "/demo")
	public @ResponseBody DemoResponse demo(@RequestBody DemoRequest request) {
		String val = demoService.demo(request.val);
		DemoResponse response = DemoResponse.builder().val(val).build();
		return response;
	}
	
}
