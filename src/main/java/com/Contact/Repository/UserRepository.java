package com.Contact.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.Contact.Model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	
	public User findByEmail(String email);
	@Query(nativeQuery = true,value = "SELECT * FROM user WHERE email LIKE %:name%")
	public User findByName(String name);
	
	

}
