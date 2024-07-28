package com.mypoc.rest;

import java.io.Serializable;

/* Commented out when moving towards a supposed eBPF
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
*/
/* Commented out when moving towards reactive r2dbc
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
*/
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
//Experiment Changing id from Long to int

//@Entity // This tells Hibernate to make a table out of this class
public class MyClient  implements Serializable{     // Persistable<Long>
    
	
	public MyClient(Long id, String name, String phone, String address, String email, String balance) {
		super();
		this.id = id;
		this.name = name;
		this.phone = phone;
		this.address = address;
		this.email = email;
		this.balance = balance;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id //Very important, this guy, a "third" incarnation of Id annotation discovered when working with R2DBC
    //@GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
	private String name;
	private String phone;
	private String address;
	private String email;
	private String balance;
	

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}


	        
    
}

