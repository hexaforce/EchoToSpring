package com.example.demo.service;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DemoServiceImpl implements DemoService {

	@Override
	public String demo(int val) {
		log.info("val is {}", val);
		return String.format("Springで %d を受け取りました", val);
	}

}
