package com.Contact.Repository;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.*;

import com.Contact.Model.Contact;
import com.Contact.Model.User;


@Repository
public interface ContactRepository extends JpaRepository<Contact, Integer> {
	
	
	@Query("FROM Contact as c where c.user.id=:userId")
	public Page<Contact> findContactByUser(@Param("userId") int userId,Pageable pageable);
	
	public List<Contact> findByNameContainingAndUser(String keywords,User user);

}
