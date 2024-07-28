package com.mypoc.rest;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;




// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface ClientRepository extends ReactiveCrudRepository<MyClient, Long> {

	/*
	 * 
	 * 
	 */

}
