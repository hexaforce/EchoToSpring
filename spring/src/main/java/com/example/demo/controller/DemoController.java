package com.example.demo.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.DemoService;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class DemoController {

	@Data
	public static class DemoRequest {
		int val;
	}

	@Data
	@Builder
	public static class DemoResponse {
		String val;
	}

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

	@PostMapping("/demo")
/*
	curl -X POST localhost:8080/demo \
    --header "Content-Type: application/json;charset=utf-8" \
    --data '{"val":8624826}'
*/
	public @ResponseBody DemoResponse demo(@RequestBody DemoRequest request) {
		String val = demoService.demo(request.val);
		DemoResponse response = DemoResponse.builder().val(val).build();
		return response;
	}

	@GetMapping("/demo1")
	// curl http://localhost:8080/demo1 -H 'userId: testuser001' -H 'password: test'
	public @ResponseBody DemoResponse demo1(//
			@RequestHeader(name = "userId", required = true) String userId, //
			@RequestHeader(name = "password", required = true) String password//
	) {
		log.info("userId : {}", userId);
		log.info("password : {}", password);
		return DemoResponse.builder().val(UUID.randomUUID().toString()).build();
	}

	@GetMapping("/demo2")
	//curl 'http://localhost:8080/demo2?userId=testuser001&password=test'
	public @ResponseBody DemoResponse demo2(//
			@RequestParam(name = "userId", required = true) String userId, //
			@RequestParam(name = "password", required = true) String password//
	) {
		log.info("userId : {}", userId);
		log.info("password : {}", password);
		return DemoResponse.builder().val(UUID.randomUUID().toString()).build();
	}

}
