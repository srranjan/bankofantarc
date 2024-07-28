package com.mypoc.rest;


import java.net.URI;

import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/backsvc/clients2")
public class MyRestController2 {
 
	@Autowired  //Let me keep the @Autowired thing at bay for sometime
	private ClientRepository clientRepository;
	
    private final MediaType mediaType = MediaType.APPLICATION_JSON;
    private final MyRestService myClientService;

    MyRestController2(MyRestService myClientService) {
        this.myClientService = myClientService;
    }
 
	@RequestMapping(method = RequestMethod.POST)
	//@PostMapping("/clients2")
    //public Mono<MyClient> createMyClient(
    public Publisher<ResponseEntity<MyClient>>  createMyClient2(
    		@RequestBody     //Publisher<MyClient> clientStream)  What is the difference between this one and the one below signatures??
    		MyClient myClient) 
    {
		
		System.out.println(" In  createMyClient2: "  );
		return this.myClientService
	            .create(myClient)
	            .map(p -> ResponseEntity.created(URI.create("/clients2/" + p.getId()))
	                .contentType(mediaType)
	                .build());
    }

	@RequestMapping(method = RequestMethod.GET)
    //@GetMapping("/clients2")
    public Publisher<MyClient> listMyClients2() {
		
		System.out.println(" In listMyClients2: "  );
        return this.myClientService.all();
    }
	
	
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    //@GetMapping("/clients2/{id}")
    public Publisher<MyClient> getClient2(
    		@PathVariable("id") 
    		Long id) 
    		//throws NotFoundException 
    {
    	System.out.println(" In getClient2: "  );
    	return this.myClientService.get(id);
    }
 
    
    //id is in 2 places, probably a code quality issue. The 2 values are assumed same (one in url, the other in payload)
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    //@PutMapping("/clients2/{id}")
    public Publisher<ResponseEntity<MyClient>> updateMyClient2(
    		@PathVariable("id") 
    		String id, 
    		@RequestBody //Publisher<MyClient> clientStream)  What is the difference between this and the below
    		MyClient myClient) 
    		//		throws NotFoundException 
    {
    	System.out.println(" In updateMyClient2: id = "  + id);
    	
    	return Mono
                .just( myClient)
                .flatMap(p -> this.myClientService.update(myClient)) //I veer away a little from the horse's mouth
                .map(p -> ResponseEntity
                    .ok()
                    .contentType(this.mediaType)
                    .build());
    }
 
 
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    //@DeleteMapping("/clients2/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public  Publisher<MyClient>  deleteMyClient2(
    		@PathVariable("id") 
    		Long id) 
    	{
    	System.out.println(" In deleteMyClient2: id = " + id);
    	
    	
    	//Mono<MyClient> myClient = this.clientRepository.findById(id);
    	 return this.myClientService.delete(id);
    	
    }
    
}
