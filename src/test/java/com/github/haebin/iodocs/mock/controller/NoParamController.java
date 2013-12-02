package com.github.haebin.iodocs.mock.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("api/v1")
public class NoParamController {
	@ResponseBody
	@RequestMapping("/user/create")
	public Map<String, Object> createUser() throws Exception {
		Map<String, Object> response = new HashMap<String, Object>();
		return response;
	}
}
