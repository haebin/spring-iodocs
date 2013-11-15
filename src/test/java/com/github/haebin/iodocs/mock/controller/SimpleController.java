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
public class SimpleController {
	@ResponseBody
	@RequestMapping("/user/create")
	@IoDocsDescription("이 API는 어쩌고 저쩌고")
	public Map<String, Object> createUser(@Valid @RequestBody UserParameter param, @IoDocsName("test") String test2,
			BindingResult bindResult, HttpServletRequest request) throws Exception {
		Map<String, Object> response = new HashMap<String, Object>();
		return response;
	}
}