package com.github.haebin.iodocs.spring;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.haebin.iodocs.annotation.IoDocsDescription;
import com.github.haebin.iodocs.annotation.IoDocsIgnore;
import com.github.haebin.iodocs.annotation.IoDocsName;
import com.github.haebin.iodocs.spring.IoDocsParameter.Location;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.Primitives;

public class IoDocsGenerator {
	public String generateIoDocs(Class<?>[] endpoints) {
		LinkedHashMap<String, Object> json = Maps.newLinkedHashMap();
		List<LinkedHashMap<String, Object>> endpointsJson = Lists.newArrayList();
		json.put("endpoints", endpointsJson);

		for (Class<?> endpoint : endpoints) {
			LinkedHashMap<String, Object> endpointMap = Maps.newLinkedHashMap();
			if(!endpoint.isAnnotationPresent(Controller.class)) {
				throw new RuntimeException("Spring @Controller only!");
			}
			
			String pathPrefix = "";
			RequestMapping prefix = endpoint.getAnnotation(RequestMapping.class);
			if(prefix != null) {
				pathPrefix = prefix.value()[0];
			}
			
			IoDocsName endpointName = endpoint.getAnnotation(IoDocsName.class);
			endpointMap.put("name",
					endpointName == null ? endpoint.getSimpleName()
							: endpointName.value());
			
			List<LinkedHashMap<String, Object>> methods = Lists.newArrayList();
			for (IoDocsMethod method : getIoDocsMethods(pathPrefix, endpoint)) {
				methods.add(method.getData());
			}
			Collections.sort(endpointsJson, new NameComparator("MethodName"));
			endpointMap.put("methods", methods);

			endpointsJson.add(endpointMap);
		}
		Collections.sort(endpointsJson, new NameComparator("name"));
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(json);
	}
	
	private <T> List<IoDocsMethod> getIoDocsMethods(String pathPrefix, Class<T> springControllerClass) {
		List<IoDocsMethod> methods = Lists.newArrayList();
		
		for (Method method : springControllerClass.getMethods()) {
			if (method.isAnnotationPresent(IoDocsIgnore.class) 
					|| !method.isAnnotationPresent(RequestMapping.class)) {
				continue;
			}
			
			String methodName = springControllerClass.getSimpleName() + "_" + method.getName();
			String synopsis = null;
			String httpMethod = "GET";
			String uri = null;
			
			Annotation[][] annotatesMap = method.getParameterAnnotations();
			for(Annotation[] annotates: annotatesMap) {
				for(Annotation annotate: annotates) {
					if (annotate.annotationType().equals(RequestBody.class)) {
						httpMethod = "POST";
						break;
					}
				}
				if(!httpMethod.equals("GET")) {
					break;
				}
			}
			
			for (Annotation annotation : method.getAnnotations()) {
				if (annotation.annotationType().equals(IoDocsName.class)) {
					methodName = ((IoDocsName) annotation).value();
				} else if (annotation.annotationType().equals(IoDocsDescription.class)) {
					synopsis = ((IoDocsDescription) annotation).value();
				} else if (annotation.annotationType().equals(RequestMapping.class)) {
					RequestMapping rm = (RequestMapping)annotation;
					uri = pathPrefix + (rm).value()[0];
				}
			}
			
			List<IoDocsParameter> parameters = processParameters(method);
			methods.add(new IoDocsMethod(methodName, httpMethod, synopsis, uri, parameters));
			
		}
		return methods;
	}
	
	public static List<Field> getAllFields(List<Field> fields, Class<?> type) {
	    if (type.getSuperclass() != null) {
	    	fields = getAllFields(fields, type.getSuperclass());
	    }
	    fields.addAll(Arrays.asList(type.getDeclaredFields()));
	    return fields;
	}
	
	public List<IoDocsParameter> processParameters(Method method) {
		List<IoDocsParameter> parameters = Lists.<IoDocsParameter> newArrayList();
		for (int paramIndex = 0; paramIndex < method.getParameterTypes().length; paramIndex++) {
			Annotation[] parameterAnnotations = method.getParameterAnnotations()[paramIndex];

			boolean ignore = false;
			for (Annotation parameterAnnotation : parameterAnnotations) {
				if (parameterAnnotation.annotationType().equals(IoDocsIgnore.class)) {
					ignore = true;
					break;
				}
			}
			
			if (ignore) { 
				continue;
			}
			
			Class<?> typeClass = method.getParameterTypes()[paramIndex];
			if(typeClass.equals(BindingResult.class) || typeClass.equals(HttpServletRequest.class)) {
				// Since these are at the end, after these types, no need to check it.
				continue;
			}
			
			if(Primitives.isPrimitive(typeClass) || typeClass.getSimpleName().equals("String")) {
				String name = "PLEASE_MARK_PARAMETER_WITH_@IoDocsName";
				for(Annotation annotation: parameterAnnotations) {
					if(annotation.annotationType().equals(IoDocsName.class)) {
						name = ((IoDocsName)annotation).value();
						break;
					}
				}
				
				processParam(name, typeClass.getSimpleName().toLowerCase(), typeClass, parameters);
			} else {
				List<Field> fields = new ArrayList<Field>();
				getAllFields(fields, typeClass);
				
				for(Field field: fields) {
					processParam(field.getName(), field.getType().getSimpleName().toLowerCase(), 
							field, parameters);
				}
			}	
		}
		return parameters;
	}
	
	private boolean processParam(String name, String type, 
			AnnotatedElement field, List<IoDocsParameter> parameters) {
		boolean required = false;
		String description = "";
		
		Object defaultValue = null;
		Location location = null;
		List<String> enumeratedList = Lists.newArrayList();
		List<String> enumeratedDescription = Lists.newArrayList();
		
		if(field.isAnnotationPresent(IoDocsIgnore.class)) {
			return false;
		}
		
		if(field.isAnnotationPresent(NotNull.class)) {
			required = true;
		}
		
		if(field.isAnnotationPresent(IoDocsDescription.class)) {
			description = field.getAnnotation(IoDocsDescription.class).value();
		}
		
		parameters.add(new IoDocsParameter(name, description, location, 
				type, required, defaultValue, enumeratedList, enumeratedDescription));
		return true;
	}
	
	class NameComparator implements Comparator<Map<String, Object>> {
		private String key = "name"; 
		public NameComparator(String key) {
			this.key = key;
		}
		
		public int compare(Map<String, Object> x, Map<String, Object> y) {
			return ((String)x.get(key)).compareTo((String)y.get(key));
		}
	}
}
