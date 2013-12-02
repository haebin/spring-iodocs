package com.github.haebin.iodocs.spring;

import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import com.github.haebin.iodocs.mock.controller.ParamWithBooleanController;
import com.github.haebin.iodocs.mock.controller.SimpleController;
import com.github.haebin.iodocs.mock.controller.SomeController;
import com.github.haebin.iodocs.mock.controller.ThatController;

public class SpringIoDocsTest {
	
	@Test
	public void booleanTest() {
		Class<?>[] clazzes = new Class<?>[]{ParamWithBooleanController.class};
		String json = new IoDocsGenerator().generateIoDocs(clazzes);
		//String file = loadClasspathResourceAsString("simpleTest.json");
		System.out.println(json);
		//Assert.assertEquals(json, file);
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
			return IOUtils.toString(ClassLoader.getSystemResourceAsStream(filename), "UTF-8").replaceAll("\r", "");
		}
		catch (Exception e) {
			fail("Could not load resource from classpath '" + filename + "': " + e.getMessage());
			return "";
		}
	}
	
}
