package com.Contact.Controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.Contact.Model.Contact;
import com.Contact.Model.User;
import com.Contact.Repository.UserRepository;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@ModelAttribute
	public void addCommonData(Model model, Principal principal) {
		String userName = principal.getName();
		System.out.println("username: "+userName);
		User user = userRepository.findByEmail(userName);
		System.out.println(user);
		model.addAttribute("user",user);
	}
	
	@GetMapping("/index")
	public String dashboard(Model model,Principal principal) {
		model.addAttribute("tittle","User Dashboard");
		return "normal/user_dashboard";
	}
	
	
	@GetMapping("/add-contact")
	public String openAddContactForm(Model model) {
		model.addAttribute("tittle","Add Contact");
		model.addAttribute("contact",new Contact());
		return "normal/add_contact_form";
	}
	
	@PostMapping("/process-contact")
	public String processControl(@ModelAttribute Contact contact,Principal principal) {
		String name = principal.getName();
		User user = userRepository.findByName(name);
		contact.setUser(user);
		user.getContacts().add(contact);
		userRepository.save(user);
		System.out.println("data: "+contact);
		return "normal/add_contact_form";
	}
	
	

}
