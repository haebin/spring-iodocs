# Mashery I/O Docs Generator for Spring

A library to generate Mashery I/O Docs JSON based on Spring annotated classes.

This library is derived from Dave Rogers' wonderful jaxrs-iodocs(https://github.com/daverog/jaxrs-iodocs). 

Use it with https://github.com/vkorapaty/iodocs.
 
# Using Library
```xml
...
<repositories>
	<repository>
		<id>com.github.haebin</id>
		<url>https://github.com/haebin/mvn-repo/raw/master</url>
		<!-- use snapshot version -->
		<snapshots>
			<enabled>true</enabled>
			<updatePolicy>always</updatePolicy>
		</snapshots>
	</repository>
<repositories>
...
<dependencies>
	...
	<!-- spring-iodocs -->
	<dependency>
		<groupId>com.github.haebin</groupId>
		<artifactId>spring-iodocs</artifactId>
		<version>0.1.1</version>
	</dependency>
	...
</dependencies>

```
# Getting started

To generate the JSON String, do the following:

```java 
Map<String, Object> props = new HashMap<String, Object>();
props.put("extension", ".json");
new IoDocsGenerator().generateIoDocs( 
	new Class<?>[]{
		EndpointOneController.class,
		EndpointTwoController.class
	},
	props
)
```

# Features
Refer test cases for supported features.

