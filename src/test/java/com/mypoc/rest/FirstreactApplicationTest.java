package com.mypoc.rest;

import static org.assertj.core.api.Assertions.assertThat;

//import org.junit.Test;
//import org.junit.runner.RunWith;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

//@RunWith(SpringRunner.class)
//@SpringBootTest
@ExtendWith(SpringExtension.class)  //May be this thing not needed.
//We create a `@SpringBootTest`, starting an actual server on a `RANDOM_PORT`
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) 
//May be (webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) not needed for this test.
public class FirstreactApplicationTest {
	
	//IMPORTNT TBD in future at right time - use the following Azure sample to create junit tests for CRUD operations:
	//https://github.com/Azure-Samples/quickstart-spring-data-r2dbc-mysql/blob/master/src/test/java/com/example/demo/TodoRepositoryTest.java
	
	

	@Autowired
	private MyRestController controller;
	
	@Test
	public void contextLoads() {
		assertThat(controller).isNotNull();
	}

}
