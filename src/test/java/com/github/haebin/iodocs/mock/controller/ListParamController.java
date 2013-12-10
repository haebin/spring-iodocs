package com.github.haebin.iodocs.mock.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.haebin.iodocs.annotation.IoDocsName;
import com.github.haebin.iodocs.mock.model.WithListParameter;

@Controller
public class ListParamController {
	@ResponseBody
	@RequestMapping(value = "/objects/{oid}/regions", method = RequestMethod.POST, consumes = "application/json")
	public Map addRegion(@PathVariable("oid") String objectId, @RequestBody List<String> regions) throws Exception {
		return new HashMap();
	}
	
	@ResponseBody
	@RequestMapping(value = "/objects/{oid}/regions", method = RequestMethod.POST, consumes = "application/json")
	public Map addRegion1(@PathVariable("oid") String objectId, @RequestBody WithListParameter param) throws Exception {
		return new HashMap();
	}
	
	@ResponseBody
	@RequestMapping(value = "/objects/{oid}/regions", method = RequestMethod.POST, consumes = "application/json")
	public Map addRegionWithIoDocs(@PathVariable("oid") String objectId, 
			@IoDocsName(value="", required=true) @RequestBody List<String> regions) throws Exception {
		return new HashMap();
	}
	
	@ResponseBody
	@RequestMapping(value = "/objects/{oid}/regions", method = RequestMethod.POST, consumes = "application/json")
	public Map addRegionWithIoDocs1(@PathVariable("oid") String objectId, 
			@IoDocsName("REQUEST_BODY") @RequestBody List<String> regions) throws Exception {
		return new HashMap();
	}
}
