package com.Contact.Service;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import com.Contact.Model.Contact;
import com.Contact.Model.User;

public interface ContactService {
	
	public Page<Contact> findContactByUser(@Param("userId") int userId,Pageable pageable);
	public Contact findByID(int id);
	public String deleteContact(int id);
	public Contact createContact(Contact contact);
	public List<Contact> findByNameContainingAndUser(String keywords,User user);
	

}
