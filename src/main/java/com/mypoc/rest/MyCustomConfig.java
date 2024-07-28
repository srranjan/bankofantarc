package com.mypoc.rest;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;


/*
 Using the @Value("${property}") annotation to inject configuration properties can sometimes be cumbersome, 
 especially if you are working with multiple properties or your data is hierarchical in nature. 
 Spring Boot provides an alternative method of working with properties that lets strongly typed beans govern and 
 validate the configuration of your application.
 */
/*
 The properties that map to @ConfigurationProperties classes available in Spring Boot, which are configured via properties files,
  YAML files, environment variables etc., are public API but the accessors (getters/setters) of the class itself are not meant to 
  be used directly.
 */
//@ConstructorBinding Commented out when moving towards eBPF //Need to experiment by uncommenting.
@ConfigurationProperties("hexagear")
public class MyCustomConfig{
	
	//Rajiv Should these values gone into application.yml ??
	
	private final Providerconfig providerconfig;
	
	public MyCustomConfig(Providerconfig providerconfig) {
        this.providerconfig = providerconfig;
    }
	
	
	public static class Providerconfig  {
		
		private String provider_host;
		private String  provider_port;
		
		public Providerconfig (String provider_host, String provider_port ) {
	        this.provider_host = provider_host;
	        this.provider_port = provider_port;
	       
	    }

		public String getProvider_host() {
			return provider_host;
		}

		public void setProvider_host(String provider_host) {
			this.provider_host = provider_host;
		}

		public String getProvider_port() {
			return provider_port;
		}

		public void setProvider_port(String provider_port) {
			this.provider_port = provider_port;
		}
		
	}

	public Providerconfig  getProviderconfig () {
		return providerconfig ;
	}

}