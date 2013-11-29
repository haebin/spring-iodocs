package com.github.haebin.iodocs.mock.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.haebin.iodocs.annotation.IoDocsDescription;
import com.github.haebin.iodocs.annotation.IoDocsName;
import com.github.haebin.iodocs.mock.model.UserParameter;

@Controller
@RequestMapping("api/v1")
public class SomeController {
	@ResponseBody
	@RequestMapping("/user/create")
	@IoDocsName("1 haha!")
	@IoDocsDescription("This is blah blah blah API!")
	public Map<String, Object> createUser(@Valid @RequestBody UserParameter param,
			BindingResult bindResult, HttpServletRequest request) throws Exception {
		Map<String, Object> response = new HashMap<String, Object>();
		return response;
	}
	
	@IoDocsName("2 aaa!")
	@IoDocsDescription("Awesome API~")
	@ResponseBody
	@RequestMapping("/user/test")
	public Map<String, Object> testUser(@Valid UserParameter param, @IoDocsName("test") String test,
			BindingResult bindResult, HttpServletRequest request) throws Exception {
		Map<String, Object> response = new HashMap<String, Object>();
		return response;
	}
	
	@IoDocsDescription("Super super API!")
	@ResponseBody
	@RequestMapping("/user/hoho")
	public Map<String, Object> hohoUser(@Valid UserParameter param,
			BindingResult bindResult, HttpServletRequest request) throws Exception {
		Map<String, Object> response = new HashMap<String, Object>();
		return response;
	}
}
