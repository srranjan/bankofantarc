package com.mypoc.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MyApp {
	
	
	 public static void main(String[] args) {
			//.run(MyRestController.class, args);
			SpringApplication.run(MyApp.class, args);
		}
	 
}
