package com.Contact.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.Contact.Model.EmailRequest;
import com.Contact.Model.User;
import com.Contact.ServiceImpl.EmailSender;
import com.Contact.ServiceImpl.UserServiceImpl;
import com.Contact.helper.Message;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class HomeController {
	
	@Autowired
	private UserServiceImpl userServiceImpl;
	
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private EmailSender emailSender;
	
	
	@GetMapping("/")
	public String home() {
		return "home";
	}
	
	@GetMapping("/about")
	public String about() {
		return "about";
	}
	
	@GetMapping("/signin")
	public String login() {
		return "login";
	}
	
	@GetMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("tittle","register - smart contact manager");
		model.addAttribute("user",new User());
		return "signup";
	}
	
	@PostMapping("/do_register")
	public String registerUser(@Valid @ModelAttribute User user, BindingResult result,@RequestParam(value = "agreement", defaultValue = "false")boolean agreement,Model model,HttpSession session) {
		
		try {
			
			if(!agreement) {
				System.out.println("you have not agreed to terms and condition");
				throw new Exception("you have not agreed to terms and condition");
			}
			if(result.hasErrors()) {
				System.out.println("ERROR"+result.toString());
				model.addAttribute("user",user);
				return "signup";
			}
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			System.out.println(user);
			System.out.println(agreement);
			String password = user.getPassword();
			String hashPassword=bCryptPasswordEncoder.encode(password);
			user.setPassword(hashPassword);
			
			User user2 = userServiceImpl.createUser(user);
			
			
			model.addAttribute("user",new User());
			
			session.setAttribute("message", new Message("Successfully registered","alert-success"));
			
			String email = user2.getEmail();
			String subject="registeration success";
			String message="You are successfully registered!! Visit this link to see your profile http://localhost:8080/user/profile ";
			EmailRequest req=new EmailRequest(email,subject,message);
			
			emailSender.sendEmail(req);
			
			
			return "signup";
			
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			model.addAttribute("user",user);
			session.setAttribute("message", new Message("something went wrong"+e.getMessage(),"alert-danger"));
			return "signup";
		}
		
		
	}

}
