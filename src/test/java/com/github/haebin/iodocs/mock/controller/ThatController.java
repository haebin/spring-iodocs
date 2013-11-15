package com.github.haebin.iodocs.mock.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.haebin.iodocs.annotation.IoDocsDescription;
import com.github.haebin.iodocs.annotation.IoDocsName;
import com.github.haebin.iodocs.mock.model.UserParameter;

@IoDocsName("1 허허허 API")
@Controller
@RequestMapping("api/v1")
public class ThatController {
	@ResponseBody
	@RequestMapping("/user/create")
	@IoDocsName("1 haha!")
	@IoDocsDescription("이 API는 어쩌고 저쩌고" + 
			"어쩌고 저쪼고<br> 이오! ")
	public Map<String, Object> createUser(@Valid UserParameter param,
			BindingResult bindResult, HttpServletRequest request) throws Exception {
		Map<String, Object> response = new HashMap<String, Object>();
		return response;
	}
	
	@IoDocsName("2 aaa!")
	@IoDocsDescription("이 API는 어쩌고 저쩌고" + 
			"어쩌고 저쪼고<br> 이오! ")
	@ResponseBody
	@RequestMapping("/user/test")
	public Map<String, Object> testUser(@Valid UserParameter param,
			BindingResult bindResult, HttpServletRequest request) throws Exception {
		Map<String, Object> response = new HashMap<String, Object>();
		return response;
	}
	
	@IoDocsDescription("이 API는 어쩌고 저쩌고" + 
			"어쩌고 저쪼고<br> 이오! ")
	@ResponseBody
	@RequestMapping("/user/hoho")
	public Map<String, Object> hohoUser(@Valid UserParameter param,
			BindingResult bindResult, HttpServletRequest request) throws Exception {
		Map<String, Object> response = new HashMap<String, Object>();
		return response;
	}
}
