package com.mypoc.rest;

import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


@Component  
public class MyConfigHandler {


	private final MyCustomConfig config;
   
	//@Autowired
    public MyConfigHandler(MyCustomConfig config) {
    	super();
        this.config = config;
    }
	
	private final static Logger LOGGER =  
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	public MyCustomConfig getConfig() {
		return config;
	} 
	
    
    
}
