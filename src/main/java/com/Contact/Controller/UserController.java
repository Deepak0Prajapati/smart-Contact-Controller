package com.Contact.Controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.Contact.Model.Contact;
import com.Contact.Model.User;
import com.Contact.Repository.UserRepository;
import com.Contact.helper.Message;

import jakarta.servlet.http.HttpSession;

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
	public String processControl(@ModelAttribute Contact contact,@RequestParam("profileimage") MultipartFile file,Principal principal,HttpSession session) {
		try {
			String name = principal.getName();
			User user = userRepository.findByName(name);
			
			if (file.isEmpty()) {
				System.out.println("file is empty");
			}else {
				contact.setImage(file.getOriginalFilename());
				File file2 = new ClassPathResource("static/img").getFile();
				
				Path path = Paths.get(file2.getAbsolutePath()+File.separator+file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			}
			
			contact.setUser(user);
			user.getContacts().add(contact);
			userRepository.save(user);
			System.out.println("data: " + contact);
			
			//message success
			session.setAttribute("message", new Message("your contact is added!! add more..","success"));
			
			
		} catch (Exception e) {
			session.setAttribute("message", new Message("Something Went wrong!! Try again..","danger"));
			System.out.println("Error in saving "+e.getMessage() );
		}
		
		return "normal/add_contact_form";
	}
	
	

}
