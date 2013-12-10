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

import com.github.haebin.iodocs.mock.model.UserParameter;

@Controller
public class RequestBodyController {
	@ResponseBody
	@RequestMapping("/user/create")
	public Map<String, Object> createUser(@Valid @RequestBody UserParameter param,
			BindingResult bindResult, HttpServletRequest request) throws Exception {
		return new HashMap<String, Object>();
	}
}
