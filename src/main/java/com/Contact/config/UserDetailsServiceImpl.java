package com.Contact.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.Contact.Model.User;
import com.Contact.Repository.UserRepository;

public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User byEmail = userRepository.findByEmail(username);
		
		if(byEmail==null) {
			throw new UsernameNotFoundException("Coluld not find user!!");
		}
		
		CustomUserDetails userDetails=new CustomUserDetails(byEmail);
		return userDetails;
	}

}
