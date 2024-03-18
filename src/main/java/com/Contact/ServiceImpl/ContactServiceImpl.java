package com.Contact.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.Contact.Model.Contact;
import com.Contact.Model.User;
import com.Contact.Repository.ContactRepository;
import com.Contact.Service.ContactService;

import jakarta.transaction.Transactional;

@Service
public class ContactServiceImpl implements ContactService {
	
	@Autowired
	private ContactRepository contactRepository;

	@Override
	public Page<Contact> findContactByUser(int userId, Pageable pageable) {
		return contactRepository.findContactByUser(userId, pageable);
	}

	@Override
	public Contact findByID(int id) {
		
		return contactRepository.findById(id).get();
	}

	@Override
	@Transactional
	public String deleteContact(int id) {
//		contactRepository.delete(contactRepository.findById(id).get());
		contactRepository.deleteById(id);
		return "Contact Deleted successfully";
	}

	@Override
	public Contact createContact(Contact contact) {
		Contact save = contactRepository.save(contact);
		// TODO Auto-generated method stub
		return save;
	}

	@Override
	public List<Contact> findByNameContainingAndUser(String keywords, User user) {
		List<Contact> list = contactRepository.findByNameContainingAndUser(keywords, user);
//		System.out.println(list);
		return list;
	}

	
	

}
