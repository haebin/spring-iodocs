package com.github.haebin.iodocs.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The name to use for the method in Mashery I/O Docs.
 * 
 * If not present, the class and method names are used to create a automatic name.
 */
@Target({ElementType.TYPE,ElementType.METHOD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface IoDocsName {
	String value();
}
