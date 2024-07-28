package com.mypoc.rest;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;


//@ConfigurationProperties ??
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(MyCustomConfig.class)//I had high hopes that this mechanism will be able to get the values from .properties/yaml and config, but it seems to be failing??
public class MyConfig {
}




/*
 * 
 * Some code snippet for future experiments to make the env variables work:
 * @SpringBootConfiguration(proxyBeanMethods = false)
@EnableAutoConfiguration
@Import({ SomeConfiguration.class, AnotherConfiguration.class })
But the code that I used above is also totally valid one as given in documentation Reference.
 */
