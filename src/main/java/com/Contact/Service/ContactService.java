package com.Contact.Service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import com.Contact.Model.Contact;

public interface ContactService {
	
	public Page<Contact> findContactByUser(@Param("userId") int userId,Pageable pageable);
	public Contact findByID(int id);
	public String deleteContact(int id);
	public Contact createContact(Contact contact);
	

}
