package com.github.haebin.iodocs.spring;

import java.util.LinkedHashMap;
import java.util.List;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class IoDocsParameter {

	public enum Location {
		query, pathReplace, header, body
	}

	private String name;
	private String description;
	private Location location;
	private String type;
	private boolean required;
	private Object defaultValue;
	private List<String> enumeration;
	private List<String> enumDescriptions;
	
	public IoDocsParameter(String name, String description, Location location,
			String type, boolean required, Object defaultValue) {
		this(name, description, location, type, required, defaultValue,
			Lists.<String>newArrayList(),
			Lists.<String>newArrayList());
	}
	
	public IoDocsParameter(String name, String description, Location location,
			String type, boolean required, Object defaultValue,
			List<String> enumeration, List<String> enumDescriptions) {
		this.name = name;
		this.description = description;
		this.location = location;
		this.type = type;
		this.required = required;
		this.defaultValue = defaultValue;
		this.enumeration = enumeration;
		this.enumDescriptions = enumDescriptions;
	}

	public LinkedHashMap<String, Object> getData() {
		LinkedHashMap<String, Object> data = Maps.newLinkedHashMap();

		data.put("Name", name);
		data.put("Required", required?"Y":"N");
		data.put("Type", type);
		if(defaultValue != null) {
			data.put("Default", defaultValue);
		}
		//data.put("Location", location + "");
		data.put("Description", description);

		if (!enumDescriptions.isEmpty()	&& enumeration.size() != enumDescriptions.size()) {
			data.put("warning", "Enumeration size ("
				+ enumeration.size()
				+ ") is not equal to enumeration description size ("
				+ enumDescriptions.size() + ")");
		}

		if (!enumeration.isEmpty()) {
			data.put("enum", enumeration);
			if (!enumDescriptions.isEmpty()) {
				data.put("EnumDescription", enumDescriptions);
			}
		}
		
		return data;
	}

	public String getName() {
		return this.name;
	}

	public void extend(List<IoDocsParameter> extensionParameters) {
		for (IoDocsParameter extension : extensionParameters) {
			if (extension.name.equals(name) &&
				extension.type == type &&
				extension.location == location) {
				if (description == null) description = extension.description;
				if (enumeration == null || enumeration.isEmpty()) {
					enumeration = extension.enumeration;
					enumDescriptions = extension.enumDescriptions;
				}
				if (!required) required = extension.required;
				if (defaultValue == null) defaultValue = extension.defaultValue;
			}
		}
	}

}
