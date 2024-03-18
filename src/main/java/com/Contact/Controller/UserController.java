package com.Contact.Controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.Contact.Model.Contact;
import com.Contact.Model.User;
import com.Contact.Repository.UserRepository;
import com.Contact.ServiceImpl.ContactServiceImpl;
import com.Contact.helper.Message;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ContactServiceImpl contactServiceImpl;

	@ModelAttribute
	public void addCommonData(Model model, Principal principal) {
		String userName = principal.getName();
		System.out.println("username: " + userName);
		User user = userRepository.findByEmail(userName);
		System.out.println(user);
		model.addAttribute("user", user);
	}

	@GetMapping("/index")
	public String dashboard(Model model, Principal principal) {
		model.addAttribute("tittle", "User Dashboard");
		return "normal/user_dashboard";
	}

	@GetMapping("/add-contact")
	public String openAddContactForm(Model model) {
		model.addAttribute("tittle", "Add Contact");
		model.addAttribute("contact", new Contact());
		return "normal/add_contact_form";
	}

	@PostMapping("/process-contact")
	public String processControl(@ModelAttribute Contact contact, @RequestParam("profileimage") MultipartFile file,
			Principal principal, HttpSession session) {
		try {
			String name = principal.getName();
			User user = userRepository.findByName(name);

			if (file.isEmpty()) {
				System.out.println("file is empty");
				contact.setImage("man.png");
			} else {
				contact.setImage(file.getOriginalFilename());
				File file2 = new ClassPathResource("static/img").getFile();

				Path path = Paths.get(file2.getAbsolutePath() + File.separator + file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			}

			contact.setUser(user);
			user.getContacts().add(contact);
			userRepository.save(user);
			System.out.println("data: " + contact);

			// message success
			session.setAttribute("message", new Message("your contact is added!! add more..", "success"));

		} catch (Exception e) {
			session.setAttribute("message", new Message("Something Went wrong!! Try again..", "danger"));
			System.out.println("Error in saving " + e.getMessage());
		}

		return "normal/add_contact_form";
	}

	@GetMapping("/show-contacts/{page}")
	public String showContacts(@PathVariable("page") Integer page, Model model, Principal principal) {

		String name = principal.getName();
		User user = this.userRepository.findByEmail(name);
		Pageable pageRequest = PageRequest.of(page, 6);
		Page<Contact> contacts = this.contactServiceImpl.findContactByUser(user.getId(), pageRequest);
		model.addAttribute("contacts", contacts);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", contacts.getTotalPages());

		return "normal/show_contacts";
	}

	@GetMapping("/{cID}/contact")
	public String showContactDetail(@PathVariable Integer cID, Model model, Principal principal) {
		String name = principal.getName();
		User user = userRepository.findByEmail(name);
		Contact contact = contactServiceImpl.findByID(cID);
		if (user.getId() == contact.getUser().getId()) {
			model.addAttribute("contact", contact); 

		}

		return "normal/contact_detail";

	}

	@GetMapping("/delete/{cID}")
	public String deleteContact(@PathVariable Integer cID, Model model, Principal principal, HttpSession session) {
	    Contact contact = contactServiceImpl.findByID(cID);
	    User user = userRepository.findByEmail(principal.getName());

	    if (user.getId() == contact.getUser().getId()) {
	        user.getContacts().remove(contact);
	        this.userRepository.save(user);
	    }

	    session.setAttribute("message", new Message("Contact deleted successfully...", "success"));

	    return "redirect:/user/show-contacts/0";
	}


	@PostMapping("/open-contact/{cId}")
	public String updateForm(@PathVariable int cId, Model model) {
		Contact contact = contactServiceImpl.findByID(cId);
		model.addAttribute("contact", contact);
		return "normal/update_form";
	}

	// update contact handler

	@PostMapping("/process-update")
	public String updateHandler(@ModelAttribute Contact contact, Model model,
			@RequestParam("profileimage") MultipartFile file, HttpSession session, Principal principal) {
		System.out.println(contact.getcId());
		try {
			Contact contat2 = contactServiceImpl.findByID(contact.getcId());
			String image = contat2.getImage();

			if (!file.isEmpty()) {
				

				File del = new ClassPathResource("static/img").getFile();
				File delfile=new File(del,contat2.getImage());
				delfile.delete();
				
				
				
				File file2 = new ClassPathResource("static/img").getFile();

				Path path = Paths.get(file2.getAbsolutePath() + File.separator + file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				contat2.setImage(file.getOriginalFilename());
			} else {
				contat2.setImage(image);
			}

			if (!contact.getEmail().isEmpty()) {
				contat2.setEmail(contact.getEmail());
			}
			if (!contact.getDescription().isEmpty()) {
				contat2.setDescription(contact.getDescription());
			}
			if (!contact.getName().isEmpty()) {
				contat2.setName(contact.getName());
			}
			if (!contact.getPhone().isEmpty()) {
				contat2.setPhone(contact.getPhone());
			}
			if (!contact.getSecondName().isEmpty()) {
				contat2.setSecondName(contact.getSecondName());
			}
			if (!contact.getWork().isEmpty()) {
				contat2.setWork(contact.getWork());
			}
			User user = userRepository.findByEmail(principal.getName());
			contat2.setUser(user);
			contactServiceImpl.createContact(contat2);
			session.setAttribute("message", new Message("Your contact is updated...", "success"));
		} catch (Exception e) {
			// TODO: handle exception
		}
		System.out.println(contact.getName());

		return "redirect:/user/"+contact.getcId()+"/contact";
	}
	
	@GetMapping("/profile")
	public String yourProfile() {
		return "normal/profile";
	}

}

