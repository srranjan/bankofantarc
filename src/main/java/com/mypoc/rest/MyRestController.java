package com.mypoc.rest;


import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


//@SpringBootApplication
@RestController
//@Controller
@RequestMapping("/backsvc/clients")
public class MyRestController {
 
	@Autowired  //Let me keep the @Autowired thing at bay for sometime
	private ClientRepository clientRepository;
    
	/*private final ClientRepository client;

	  public MyRestControllerr(ClientRepository client) {
	    this.client = client;
	  }*/
	
	//Following @Value nonsenses just for debug puposes.
	/*
	 * spring.application.name=proxysvc
spring.application.deploymode=${MYDEPLOY:}
	 */
	//Begin temp code fit for deletion. Used for some testing.
	@Value("${hexagear.providerconfig.provider_host}")
    private String provider_host;
	@Value("${hexagear.providerconfig.provider_port}")
    private String provider_port;
	@Value("${spring.application.name}")
    private String appName;
	@Value("${spring.application.deploymode}")
    private String deployMode;
	@Value("${spring.r2dbc.url}")
    private String dbUrl;
	
	@Autowired
	private MyConfigHandler myConfigHandler;
 
	//End temp code fit for deletion. Used for some testing.
	
	//@PostMapping("/clients")
	@RequestMapping(method = RequestMethod.POST)
    public Mono<MyClient> createMyClient(
    //public Mono<Void> createMyClient(
    		@RequestBody 
    //Publisher<MyClient> myClient) 
    		MyClient myClient) 
    {
		
		//myClient.setId(null);
		
		return this.clientRepository.
				//saveAll(myClient) //Probably this and corresponding code flow worked with earlier mongodb version by accident, it is not for a single record anyway
				//.then() 
		save(myClient)
				;
		//Used an alternate code as  totally valid code (using Publisher etc.) is causing issues when moving from mongo to mysql.
    }

	//@GetMapping("/clients")
    @RequestMapping(method = RequestMethod.GET)
    public Flux<MyClient> listMyClients() {
		
    	//Begin temp code fit for deletion. Used for some testing.
    	//Rather, the 3 classes MyConfig, MyConfigHanler and MyCustomConfig are not needed in this module.
		System.out.println(" In listMyClients: message "  );
		System.out.println(" In listMyClients: DEBUG3 provider_host= " + provider_host );
		System.out.println(" In listMyClients: DEBUG3 provider_port= " + provider_port);
		System.out.println(" In listMyClients: DEBUG3 appName= " + appName);
		System.out.println(" In listMyClients: DEBUG3 deployMode= " + deployMode);
		System.out.println(" In listMyClients: DEBUG3 dbUrl " + dbUrl);
		
		System.out.println(" In listMyClients: DEBUG3 myConfigHandler.getConfig().getProviderconfig().getProvider_host() " + myConfigHandler.getConfig().getProviderconfig().getProvider_host());
		System.out.println(" In listMyClients: DEBUG3 myConfigHandler.getConfig().getProviderconfig().getProvider_port() " + myConfigHandler.getConfig().getProviderconfig().getProvider_port());
		
		//End temp code fit for deletion. Used for some testing.
		return this.clientRepository.findAll();
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    //@GetMapping("/clients/{id}")
    public Mono <MyClient> getClient(
    		@PathVariable("id") 
    		Long id) 
    		//throws NotFoundException 
    {
    	System.out.println(" In getClient: id = "  + id);
    	Mono<MyClient> myClient = this.clientRepository.findById(id);
 
        if (myClient == null) {
            //throw new NotFoundException();
        }
        return myClient;
    }
 
    
    //id is in 2 places, probably a code quality issue. The 2 values are assumed same (one in url, the other in payload)
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    //@PutMapping("/clients/{id}")
    public Mono<Void> updateMyClient(
    		@PathVariable("id") 
    		String id, 
    		@RequestBody 
    		Publisher<MyClient> clientStream) 
    		//		throws NotFoundException 
    {
    	System.out.println(" In updateMyClient: id = "  + id);
    	
    	return this.clientRepository.saveAll(clientStream).then(); //What about id validation for duplicates???
    }
 
 
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    //@DeleteMapping("/clients/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public  Mono<Void>  deleteMyClient(
    		@PathVariable("id") 
    		Long id) 
    	{
    	System.out.println(" In deleteMyClient: id = " + id);
    	
    	
    	//Mono<MyClient> myClient = this.clientRepository.findById(id);
    	
    	//MyClient theClient = myClient.block();
    	
    	//https://github.com/hantsy/spring-reactive-sample/blob/master/boot-data-mongo/src/main/java/com/example/demo/DemoApplication.java
    	
    	
    	
    	//clientRepository.delete(theClient);
    	
    	return this.clientRepository.deleteById(id); 
    	//return clientRepository.deleteById(idStream).then();
    	
    }
    
}
