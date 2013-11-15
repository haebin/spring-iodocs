# Spring REST Mashery I/O Docs Generator

A library to generate Mashery I/O Docs JSON based on Spring annotated classes.

This library is derived from Dave Rogers' wonderful jaxrs-iodocs(https://github.com/daverog/jaxrs-iodocs). 
 
# Getting started

To generate the JSON String, do the following:

```java 
new IoDocsGenerator().generateIoDocs( 
	new Class<?>[]{
		EndpointOneController.class,
		EndpointTwoController.class,
	}
)
```

# Features

* ...