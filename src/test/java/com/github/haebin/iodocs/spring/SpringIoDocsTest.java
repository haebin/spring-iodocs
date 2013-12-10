package com.github.haebin.iodocs.spring;

import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import com.github.haebin.iodocs.mock.controller.FullController;
import com.github.haebin.iodocs.mock.controller.ListParamController;
import com.github.haebin.iodocs.mock.controller.NoParamController;
import com.github.haebin.iodocs.mock.controller.NotUsedController;
import com.github.haebin.iodocs.mock.controller.ParamWithBooleanController;
import com.github.haebin.iodocs.mock.controller.RequestBodyController;
import com.github.haebin.iodocs.mock.controller.SimpleController;
import com.github.haebin.iodocs.mock.controller.SomeController;
import com.github.haebin.iodocs.mock.controller.ThatController;

public class SpringIoDocsTest {
	@Test
	public void notUsedParameterTest() {
		Class<?>[] clazzes = new Class<?>[]{NotUsedController.class};
		String json = new IoDocsGenerator().generateIoDocs(clazzes);
		//String file = loadClasspathResourceAsString("requestBodyTest.json");
		System.out.println(json);
		//Assert.assertEquals(json, file);
	}
	
	@Test
	public void requetBodyControllerTest() {
		Class<?>[] clazzes = new Class<?>[]{RequestBodyController.class};
		String json = new IoDocsGenerator().generateIoDocs(clazzes);
		String file = loadClasspathResourceAsString("requestBodyTest.json");
		System.out.println(json);
		Assert.assertEquals(json, file);
	}
	
	@Test
	public void withListControllerTest() {
		Class<?>[] clazzes = new Class<?>[]{ListParamController.class};
		String json = new IoDocsGenerator().generateIoDocs(clazzes);
		String file = loadClasspathResourceAsString("withListTest.json");
		System.out.println(json);
		Assert.assertEquals(json, file);
	}
	
	@Test
	public void fullControllerTest() {
		Class<?>[] clazzes = new Class<?>[]{FullController.class};
		String json = new IoDocsGenerator().generateIoDocs(clazzes);
		String file = loadClasspathResourceAsString("fullTest.json");
		System.out.println(json);
		Assert.assertEquals(json, file);
	}
	
	@Test
	public void noParamTest() {
		Class<?>[] clazzes = new Class<?>[]{NoParamController.class};
		String json = new IoDocsGenerator().generateIoDocs(clazzes);
		String file = loadClasspathResourceAsString("noParamTest.json");
		System.out.println(json);
		Assert.assertEquals(json, file);
	}
	
	@Test
	public void booleanTest() {
		Class<?>[] clazzes = new Class<?>[]{ParamWithBooleanController.class};
		String json = new IoDocsGenerator().generateIoDocs(clazzes);
		String file = loadClasspathResourceAsString("paramWithBooleanTest.json");
		System.out.println(json);
		Assert.assertEquals(json, file);
	}
	
	@Test
	public void simpleTest() {
		Class<?>[] clazzes = new Class<?>[]{SimpleController.class};
		String json = new IoDocsGenerator().generateIoDocs(clazzes);
		String file = loadClasspathResourceAsString("simpleTest.json");
		System.out.println(json);
		Assert.assertEquals(json, file);
	}

	@Test
	public void normalTest() {
		Class<?>[] clazzes = new Class<?>[]{SomeController.class, ThatController.class};
		String json = new IoDocsGenerator().generateIoDocs(clazzes);
		String file = loadClasspathResourceAsString("normalTest.json");
		System.out.println(json);
		Assert.assertEquals(json, file);
	}
	
	@Test
	public void normalTestProp() {
		Map<String, Object> props = new HashMap<String, Object>();
		props.put("extension", ".json");
		Class<?>[] clazzes = new Class<?>[]{SomeController.class, ThatController.class};
		String json = new IoDocsGenerator().generateIoDocs(clazzes, props);
		System.out.println(json);
	}
		
	public static String loadClasspathResourceAsString(String filename) {
		try {
			return IOUtils.toString(ClassLoader.getSystemResourceAsStream(filename), "UTF-8").replaceAll("\r", "").trim();
		}
		catch (Exception e) {
			fail("Could not load resource from classpath '" + filename + "': " + e.getMessage());
			return "";
		}
	}
	
	@Test
	public void regexTest() throws Exception {
		//String path = "/users/{mid}";
		//String path = "/{mid}";
		IoDocsGenerator gen = new IoDocsGenerator();
		
		String path = "/users/{mid}/{test}";
		List<String> vars = gen.parsePathVariables(path);
		Assert.assertEquals(vars.size(), 2);
		
		path = "{mid}/{test}";
		vars = gen.parsePathVariables(path);
		Assert.assertEquals(vars.size(), 2);
		
		path = "/";
		vars = gen.parsePathVariables(path);
		Assert.assertEquals(vars.size(), 0);
		
		path = "{mid}";
		vars = gen.parsePathVariables(path);
		Assert.assertEquals(vars.size(), 1);
		
		path = "/{mid}";
		vars = gen.parsePathVariables(path);
		Assert.assertEquals(vars.size(), 1);
	}
	
}
