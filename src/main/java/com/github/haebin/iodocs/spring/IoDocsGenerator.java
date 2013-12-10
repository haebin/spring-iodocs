package com.github.haebin.iodocs.spring;

import java.beans.Introspector;
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

import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ValueConstants;

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
		return generateIoDocs(endpoints, null);
	}
	
	public String generateIoDocs(Class<?>[] endpoints, Map<String, Object> props) {
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
			for (IoDocsMethod method : getIoDocsMethods(pathPrefix, endpoint, props)) {
				methods.add(method.getData());
			}
			Collections.sort(methods, new MapComparator("MethodName"));
			endpointMap.put("methods", methods);

			endpointsJson.add(endpointMap);
		}
		Collections.sort(endpointsJson, new MapComparator("name"));
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(json);
	}
	
	private <T> List<IoDocsMethod> getIoDocsMethods(String pathPrefix, Class<T> springControllerClass, Map<String, Object> props) {
		List<IoDocsMethod> methods = Lists.newArrayList();
		
		for (Method method : springControllerClass.getMethods()) {
			if (method.isAnnotationPresent(IoDocsIgnore.class) 
					|| !method.isAnnotationPresent(RequestMapping.class)) {
				continue;
			}
			
			String httpMethod = "GET";
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
			
			String methodName = method.getName();
			String synopsis = null;
			String uri = null;
			
			for (Annotation annotation : method.getAnnotations()) {
				if (annotation.annotationType().equals(IoDocsName.class)) {
					methodName = ((IoDocsName) annotation).value();
				} else if (annotation.annotationType().equals(IoDocsDescription.class)) {
					synopsis = ((IoDocsDescription) annotation).value();
				} else if (annotation.annotationType().equals(RequestMapping.class)) {
					RequestMapping rm = (RequestMapping)annotation;
					RequestMethod[] httpMethods = rm.method();
					if(httpMethods != null && httpMethods.length > 0) {
						httpMethod = httpMethods[0].toString();
					}
					
					Object ext = (props == null? null:props.get("extension"));
					String path = rm.value()[0];
					List<String> pathVars = parsePathVariables(path);
					for(String pathVar: pathVars) {
						path = path.replaceFirst("\\{" + pathVar + "\\}", ":" + pathVar);
					}
					uri = pathPrefix + path + (ext == null ? "":ext.toString());
				}
			}
			
			List<IoDocsParameter> parameters = processParameters(method);
			methods.add(new IoDocsMethod(methodName, httpMethod, synopsis, uri, parameters));
			
		}
		return methods;
	}
	
	public List<String> parsePathVariables(String path) {
		String[] tokens = path.split("/");
		List<String> vars = new ArrayList<String>();
		for(String token: tokens) {
			if(StringUtils.isEmpty(token)) {
				continue;
			}
			
			if(token.startsWith("{")) {
				vars.add(token.substring(1, token.length()-1));
			}
		}	
		return vars;
	}
	
	public static Field getField(Class<?> type, String name) {
		Field field = null;
		try {
			field = type.getDeclaredField(name);
		} catch (Exception e) {
			;
		}
		if(field == null) {
	    	field = getField(type.getSuperclass(), name);
	    }
	    return field;
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
			if(typeClass.equals(BindingResult.class) || typeClass.equals(HttpServletRequest.class) || typeClass.equals(HttpMethod.class)) {
				// Since these are at the end, after these types, no need to check it.
				continue;
			}
			
			if(Primitives.isPrimitive(typeClass) || Primitives.isWrapperType(typeClass) || typeClass.equals(String.class)
				|| typeClass.equals(List.class)	) {
				Object defaultValue = null;
				boolean required = false;
				String name = "PLEASE_GIVE_PARAMETER_#" + (paramIndex+1) + "_A_NAME_WITH_@IoDocsName";
				for(Annotation annotation: parameterAnnotations) {
					if(annotation.annotationType().equals(IoDocsName.class)) {
						name = ((IoDocsName)annotation).value();
						required = ((IoDocsName)annotation).required();
						break;
					} else if(annotation.annotationType().equals(PathVariable.class)) {
						name = ((PathVariable)annotation).value();
						required = true;
						break;
					} else if(annotation.annotationType().equals(RequestParam.class)) {
						name = ((RequestParam)annotation).value();
						defaultValue = ((RequestParam)annotation).defaultValue();
						if(defaultValue.equals(ValueConstants.DEFAULT_NONE)) {
							defaultValue = "";
						}
						required = ((RequestParam)annotation).required();
						break;
					} else if(annotation.annotationType().equals(RequestBody.class)) {
						
					}
				}
				
				parameters.add(processParam(name, typeClass.getSimpleName().toLowerCase(), defaultValue, required, typeClass));
			} else {
				List<Method> publicMethods = Arrays.asList(typeClass.getMethods());
				Collections.sort(publicMethods,new MethodNameComparator());
				//getAllFields(publicMethods, typeClass);
				
				Object typeObject = null;
				try {
					typeObject = typeClass.newInstance();
					
					for(Method publicMethod: publicMethods) {
						//field.setAccessible(true);
						if(!publicMethod.getName().startsWith("get") && !publicMethod.getName().startsWith("is")) {
							continue;
						}
						
						if(publicMethod.getName().equals("getClass")) {
							continue;
						}
						
						if(publicMethod.getParameterTypes().length > 0) {
							continue;
						}
						
						Object defaultValue = publicMethod.invoke(typeObject);
						if(defaultValue instanceof Boolean) {
							defaultValue = defaultValue.toString();
						}
						String name = "";
						//publicMethod.getName()
						if(publicMethod.getName().startsWith("get")) {
							name = publicMethod.getName().replaceFirst("get", "");
						} else {
							name = publicMethod.getName().replaceFirst("is", "");
						}
						name = Introspector.decapitalize(name);
						
						parameters.add(processParam(name, publicMethod.getReturnType().getSimpleName().toLowerCase(), 
								defaultValue, false, getField(typeClass,name)));
					}
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}	
		}
		return parameters;
	}
	
	private IoDocsParameter processParam(String name, String type, Object defaultValue, boolean required, 
			AnnotatedElement field) {
		String description = "";
		
		Location location = null;
		List<String> enumeratedList = Lists.newArrayList();
		List<String> enumeratedDescription = Lists.newArrayList();
		
		if(field.isAnnotationPresent(IoDocsIgnore.class)) {
			return null;
		}
		
		if(field.isAnnotationPresent(NotNull.class)) {
			required = true;
		}
		
		if(field.isAnnotationPresent(IoDocsDescription.class)) {
			description = field.getAnnotation(IoDocsDescription.class).value();
		}

		return new IoDocsParameter(name, description, location, 
				type, required, defaultValue, enumeratedList, enumeratedDescription);
	}
	
	class MapComparator implements Comparator<Map<String, Object>> {
		private String key = "name"; 
		public MapComparator(String key) {
			this.key = key;
		}
		
		public int compare(Map<String, Object> x, Map<String, Object> y) {
			return ((String)x.get(key)).compareTo((String)y.get(key));
		}
	}
	
	class MethodNameComparator implements Comparator<Method> { 
		public int compare(Method x, Method y) {
			return ((String)x.getName()).compareTo((String)y.getName());
		}
	}
}
