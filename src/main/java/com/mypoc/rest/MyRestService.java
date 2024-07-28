package com.mypoc.rest;

import lombok.extern.log4j.Log4j2;

import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
@Service
class MyRestService {

    private final ApplicationEventPublisher publisher; // <1>
    private final ClientRepository clientRepository; // <2>
    

    MyRestService(ApplicationEventPublisher publisher, ClientRepository clientRepository) {
        this.publisher = publisher;
        this.clientRepository = clientRepository;
    }
   
 
    public Mono<MyClient> create(MyClient myClient) { // <7>
    	
    	//myClient.setId(null);
        return this.clientRepository
            .save(myClient)
            //.doOnSuccess(profile -> this.publisher.publishEvent(new ClientCreatedEvent(myClient)))
            ;//commented out the above as this totally valid code is causing issues when moving from mongo to mysql.
    }
 
    public Flux<MyClient> all() {
		
		//System.out.println(" In listMyClients: message = "  + message);
        return this.clientRepository.findAll();
    }
	
    public Mono <MyClient> get(
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
 
    // (Long id, String name, String phone, String address, String potalCd, String type, String balance)
    public Mono<MyClient> update(MyClient myClient) { // <5>
        return this.clientRepository
            .findById(myClient.getId())
            .map(p -> new MyClient(p.getId(), myClient.getName(), myClient.getPhone(), myClient.getAddress(),  myClient.getEmail(), myClient.getBalance()))
            .flatMap(this.clientRepository::save);
    }
    
    public Mono<MyClient> delete(Long id) { // <6>
        return this.clientRepository
            .findById(id)
            .flatMap(p -> this.clientRepository.deleteById(p.getId()).thenReturn(p));
    }
    
}