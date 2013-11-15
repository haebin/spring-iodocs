package com.github.haebin.iodocs.spring;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.google.common.collect.Maps;

public class IoDocsMethod {
	
	private String methodName;
	private String httpMethod;
	private String synopsis;
	private String uri;
	private List<IoDocsParameter> parameters;
	
	public IoDocsMethod(
			String name, String httpMethod, String description,
			String path, List<IoDocsParameter> parameters) {
		this.methodName = name;
		this.httpMethod = httpMethod;
		this.synopsis = description;
		this.uri = path;
		this.parameters = parameters;
	}

	public LinkedHashMap<String, Object> getData() {
		LinkedHashMap<String, Object> method = Maps.newLinkedHashMap();
		method.put("MethodName", methodName);
		method.put("HTTPMethod", httpMethod);
		method.put("URI", uri);
		
		if (!StringUtils.isBlank(synopsis))
			method.put("Synopsis", synopsis);

		List<LinkedHashMap<String, Object>> parameterData = getParameterData();
		if (!parameterData.isEmpty()) {
			method.put("parameters", parameterData);
		}
		return method;
	}

	private List<LinkedHashMap<String, Object>> getParameterData() {
		List<LinkedHashMap<String, Object>> parameterData = new ArrayList<LinkedHashMap<String, Object>>();

		for (IoDocsParameter parameter : parameters){
			//parameter.extend(extensionParameters);
			parameterData.add(parameter.getData());
		}
		
		return parameterData;
	}

	public String getName() {
		return methodName;
	}

}
