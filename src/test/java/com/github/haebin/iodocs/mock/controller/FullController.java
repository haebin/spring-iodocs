package com.github.haebin.iodocs.mock.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.haebin.iodocs.annotation.IoDocsIgnore;
import com.github.haebin.iodocs.mock.model.UserParameter;

@Controller
@RequestMapping("/api/v1/internal")
public class FullController  {
	
	@ResponseBody
	@RequestMapping(value = "/users/{mid}", method = RequestMethod.GET)
	public Map show(@PathVariable("mid") String mid) throws Exception {
		return new HashMap();
	}

	@ResponseBody
	@RequestMapping(value = "/users/{mid}", method = RequestMethod.PUT, consumes = "application/json")
	public Map update(@PathVariable("mid") String mid, @RequestBody UserParameter user) throws Exception {
		return new HashMap();
	}

	@IoDocsIgnore
	@ResponseBody
	@RequestMapping(value = "/users/{mid}", method = RequestMethod.POST, consumes = "application/json", params = {"method=PUT"})
	public Map update(
			@RequestParam(value = "method", required = true) HttpMethod method,
			@PathVariable("mid") String mid,
			@RequestBody UserParameter user) throws Exception {
		return new HashMap();
	}
	
	@ResponseBody
	@RequestMapping(value = "/users/{mid}/likes", method = RequestMethod.GET)
	public Map likes(
			@PathVariable("mid") String mid,
			@RequestParam(value = "type", required = false, defaultValue = "ALL") String typeName,
			@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
			@RequestParam(value = "includeLikeCount", required = false, defaultValue = "false") boolean includeLikeCount,
			@RequestParam(value = "maxTimestamp", required = false) Long offsetTimestamp) throws Exception {
		return new HashMap();
	}
	
	@ResponseBody
	@RequestMapping(value = "/users/{mid}/recommends", method = RequestMethod.GET)
	public Map recommends(
			@PathVariable("mid") String mid,
			@RequestParam(value = "type", required = false, defaultValue = "ALL") String typeName,
			@RequestParam(value = "inDays", required = false, defaultValue = "0") int inDays,
			@RequestParam(value = "limit", required = false, defaultValue = "5") int limit) throws Exception {
		return new HashMap();
	}
	
	@ResponseBody
	@RequestMapping(value = "/types/{type}", method = RequestMethod.DELETE)
	public Map delete(@PathVariable("type") String typeName) throws Exception {
		return new HashMap();
	}
	
	@ResponseBody
	@RequestMapping(value = "/types", method = RequestMethod.POST, consumes = "application/json")
	public Map create(@RequestBody UserParameter type) throws Exception {
		return new HashMap();
	}
}
