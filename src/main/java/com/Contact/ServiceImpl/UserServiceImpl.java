package com.Contact.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Contact.Model.User;
import com.Contact.Repository.UserRepository;
import com.Contact.Service.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public User createUser(User user) {
		User save = userRepository.save(user);
		return save;
	}

	@Override
	public User findByEmail(String email) {
		
		return userRepository.findByEmail(email);
	}
	
	

}
