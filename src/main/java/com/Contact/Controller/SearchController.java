package com.Contact.Controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.Contact.Model.Contact;
import com.Contact.Model.User;
import com.Contact.ServiceImpl.ContactServiceImpl;
import com.Contact.ServiceImpl.UserServiceImpl;

@RestController
public class SearchController {
	
	@Autowired
	private UserServiceImpl userServiceImpl;
	@Autowired
	private ContactServiceImpl contactServiceImpl;
	
	//search handler
	@GetMapping("/search/{query}")
	public ResponseEntity<?> search(@PathVariable String query ,Principal principal){
		System.out.println(query);
		User user=this.userServiceImpl.findByEmail(principal.getName());
		List<Contact> contacts = contactServiceImpl.findByNameContainingAndUser(query, user);
		System.out.println(query);
//		System.out.println(user);
//		System.out.println(contacts);
		return ResponseEntity.ok(contacts);
		
		
		
	}

}
