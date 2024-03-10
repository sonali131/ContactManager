package com.smart.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
<<<<<<< HEAD
import java.util.Map;
import java.util.Optional;

import org.apache.tomcat.util.log.UserDataHelper.Mode;
import org.json.JSONObject;
=======
import java.util.Optional;

import org.apache.tomcat.util.log.UserDataHelper.Mode;
>>>>>>> 35a05ef976c3c85ceaca1706f64da238955d23d6
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
<<<<<<< HEAD
import org.springframework.http.ResponseEntity;
=======
>>>>>>> 35a05ef976c3c85ceaca1706f64da238955d23d6
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
<<<<<<< HEAD
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.smart.dao.ContactRepositiry;
import com.smart.dao.MyOrderRepositiry;
import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.MyOrder;
=======
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smart.dao.ContactRepositiry;
import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
>>>>>>> 35a05ef976c3c85ceaca1706f64da238955d23d6
import com.smart.entities.User;
import com.smart.helper.Message;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
<<<<<<< HEAD
import com.razorpay.*;
=======
>>>>>>> 35a05ef976c3c85ceaca1706f64da238955d23d6

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private UserRepository userRepositry;
	
	@Autowired
	private ContactRepositiry contactRepositiry;
<<<<<<< HEAD
	
	@Autowired
	private MyOrderRepositiry myOrderRepositiry;
=======
>>>>>>> 35a05ef976c3c85ceaca1706f64da238955d23d6

	// adding common data
	@ModelAttribute
	public void addCommonData(Model m, Principal principal) {
		String userName = principal.getName();
		System.out.println("USERNAME  " + userName);

		// get the user using username(Email)
		User user = userRepositry.getUserByUserName(userName);

		System.out.println("USER " + user);

		m.addAttribute("user", user);

	}

	// dashboard home
	@RequestMapping("/index")
	public String dashboard(Model m, Principal principal) {
		m.addAttribute("title", "User dashboard");
//		m.addAttribute("contact", new Contact());

		return "Normal/user_dashboard";
	}

	// open add form handler

	@RequestMapping("/addcontact")
	public String openAddContactForm(Model m) {

		m.addAttribute("title", "Add Contact");
		m.addAttribute("contact", new Contact());

		return "Normal/addContact";
	}

	// processing add contact form

	@PostMapping(value = "/process-contact", consumes = MULTIPART_FORM_DATA_VALUE)
	public String processContact(@Valid @ModelAttribute Contact contact,
			@RequestParam("profileImage") MultipartFile file, BindingResult bindingResult, Principal principal,
			HttpSession session) {

		try {
			String name = principal.getName();

			User user = this.userRepositry.getUserByUserName(name);

			// processing and uploading file
			if (file.isEmpty()) {
				// if the file is empty then try your message
				System.out.println("File is empty");
				contact.setImage("contact.jpg");
				
			} else {
				// upload the file to folder and update the name to contact
				contact.setImage(file.getOriginalFilename());
				File file2 = new ClassPathResource("/static/img").getFile();
				Path path = Paths.get(file2.getAbsolutePath() + File.separator + file.getOriginalFilename());

				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				System.out.println("Image is uploaded ");

			}

			user.getContacts().add(contact);
			contact.setUser(user);

			this.userRepositry.save(user);

			System.out.println("DATA " + contact);
			System.out.println("Added to data base");

			// message success....
			session.setAttribute("message", new Message("Your Contact is added !! Add more...", "success"));

		} catch (Exception e) {
			System.out.println("ERROR " + e.getMessage());
			e.printStackTrace();
			// error message....
			session.setAttribute("message", new Message("Something went wrong !! Try again...", "danger"));
		}
		return "Normal/addContact";
	}

//show contacts handler
	//per page =5[n]
	//current page=0[page]
	
	
	@GetMapping("/show-contacts/{page}")
	public String showContacts(@PathVariable("page") Integer page,Model m,Principal principal) {
		m.addAttribute("title","Show User Contacts");
		//contact list send...
//		String userName = principal.getName();
//		User user = this.userRepositry.getUserByUserName(userName);
//		List<Contact> contacts = user.getContacts();
		
		String userName = principal.getName();
		User user = this.userRepositry.getUserByUserName(userName);
		
		//current page
		//current per page 5[n]
		
		Pageable pageable = PageRequest.of(page, 5);
		
		Page<Contact> contacts = this.contactRepositiry.findContactsByUser(user.getId(),pageable);
		
		m.addAttribute("contacts", contacts);
		m.addAttribute("currentPage",page);
		m.addAttribute("totalsPages", contacts.getTotalPages());
		
		return "Normal/show_contacts";
		
	}
	
	//showing particular contact details.
	
	@RequestMapping("/{cId}/contact/")
	public String showContactDetail(@PathVariable("cId")Integer cId,Model model,Principal principal) {
		System.out.println("CID " +cId);
		Optional<Contact> contactOptional = this.contactRepositiry.findById(cId);
		Contact contact = contactOptional.get();
		
		String userName = principal.getName();
		User user = this.userRepositry.getUserByUserName(userName);
		
		if(user.getId()==contact.getUser().getId()) {
			model.addAttribute("contact",contact);
			
		}
		
		return "Normal/contact_detail";
	}
	
	//delete form handler
	
	@GetMapping("/delete/{cid}")
	public String deleteContact(@PathVariable("cid") Integer cId, HttpSession session,Principal principal) {
	    Optional<Contact> contactOptional = this.contactRepositiry.findById(cId);
	    
	    
	    if (contactOptional.isPresent()) {
	        Contact contact = contactOptional.get();
	        //contact.setUser(null);

	        // Check..
	        //this.contactRepositiry.delete(contact);
	        
	        User user=this.userRepositry.getUserByUserName(principal.getName());
	        user.getContacts().remove(contact);
	        this.userRepositry.save(user);
	        
	        System.out.println("DELETED");
	        
	        session.setAttribute("message", new Message("Contact deleted successfully", "success"));
	    } else {
	        // Handle the case where the contact with the given ID is not found
	        session.setAttribute("message", new Message("Contact not found", "danger"));
	    }

	    return "redirect:/user/show-contacts/0";
	}
	
	//update form handler
	
	@PostMapping("/update-contact/{cid}")
	public String updateForm(@PathVariable ("cid") Integer cid,Model m) {
		
		m.addAttribute("title", "Update Contact");
		Contact contact=this.contactRepositiry.findById(cid).get();
		m.addAttribute("contact",contact);
		
		return "Normal/update_form";
		
	}
	
	//update contact handler
	
	@RequestMapping(value="/process-update",method=RequestMethod.POST)
	public String updateHandler(@ModelAttribute Contact contact ,@RequestParam("profileImage") MultipartFile file,Model m,HttpSession session,Principal principal) {
		
		try {
			
			//old contact details
			Contact oldcontactDetail = this.contactRepositiry.findById(contact.getcId()).get();
			
			//image..
			if(!file.isEmpty()) {
				
				
				
				//file work..
				//rewrite
				
				//delete old photo
				File deleteFile = new ClassPathResource("/static/img").getFile();
				File file1=new File(deleteFile,oldcontactDetail.getImage());
				file1.delete();
				
				//update new photo
				File file2 = new ClassPathResource("/static/img").getFile();
				Path path = Paths.get(file2.getAbsolutePath() + File.separator + file.getOriginalFilename());

				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				contact.setImage(file.getOriginalFilename());
				
				
				
				
			}
			else {
				contact.setImage(oldcontactDetail.getImage());
			}
			
			User user=this.userRepositry.getUserByUserName(principal.getName());
			
			contact.setUser(user);
			
			this.contactRepositiry.save(contact);
			session.setAttribute("message", new Message("Your contact is updated..","success"));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("CONTACT NAME "+contact.getName());
		System.out.println("CONTACT ID "+contact.getcId());
		
		
		return "redirect:/user/"+contact.getcId()+"/contact";
	}
	
	//your profile handler setting
	
	@GetMapping("/profile")
	public String yourProfile(Model model) {
		model.addAttribute("title","Profile Page");
		return "Normal/profile";
	}
	
	//open setting handler
	@GetMapping("settings")
	public String openSettings() {
		return "normal/settings";
	}
	
	//change password ...handler
	@PostMapping("/change-password")
	public String changePassword(@RequestParam("oldPassword") String oldPassword,@RequestParam("newPassword") String newPassword,Principal principal,HttpSession session) {
		System.out.println("OLD PASSWORD"+oldPassword);
		System.out.println("NEW PASSWORD"+newPassword);
		
		String userName=principal.getName();
		User currentUser = this.userRepositry.getUserByUserName(userName);
		System.out.println(currentUser.getPassword());
		
		if(this.bCryptPasswordEncoder.matches(oldPassword, currentUser.getPassword())) {
			//change the password
			currentUser.setPassword(this.bCryptPasswordEncoder.encode(newPassword));
			this.userRepositry.save(currentUser);
			session.setAttribute("message", new Message("Your password is successfuly changed...","success"));
		}else {
			//error...
			session.setAttribute("message", new Message("Your old password is incorrect...","danger"));
			return "redirect:/user/settings";
		}
		
		return "redirect:/user/index";
	}
<<<<<<< HEAD
	
	//creating order for payment
	@PostMapping("/create_order")
	@ResponseBody
	public String createOrder(@RequestBody Map<String, Object> data,Principal principal) throws RazorpayException {
		System.out.println("Hii order created.."+data);
		int amt=Integer.parseInt(data.get("amount").toString());
		var client=new RazorpayClient("rzp_test_eYHbxIlEE9T9Ut","MXY9MntCOwo3cBmuJ9pPWdHB");
		
		JSONObject ob=new JSONObject();
		ob.put("amount", amt*100);
		ob.put("currency", "INR");
		ob.put("receipt", "txn_235425");
		
		//creating new order
		
		Order order = client.orders.create(ob);
		System.out.println(order);
		
		//save the order in database
		
		MyOrder myOrder = new MyOrder();
		myOrder.setAmount(order.get("amount")+"");
		myOrder.setOrderId(order.get("id"));
		myOrder.setPaymentId(null);
		myOrder.setStatus("created");
		myOrder.setUser(this.userRepositry.getUserByUserName(principal.getName()));
	    myOrder.setRecepit(order.get("recepit"));
	    
	    this.myOrderRepositiry.save(myOrder);
	    
		
		//if you want to save data in your database
		
		return order.toString();
	}

	@PostMapping("/update_order")
	public ResponseEntity<?> updateOrder(@RequestBody Map<String, Object> data){
		
		MyOrder myOrder = this.myOrderRepositiry.findByOrderId(data.get("order_id").toString());
		myOrder.setPaymentId(data.get("payment_id").toString());
		myOrder.setStatus(data.get("status").toString());
		
		this.myOrderRepositiry.save(myOrder);
		
		System.out.println(data);
		return ResponseEntity.ok(Map.of("msg","updated"));
	}
=======

>>>>>>> 35a05ef976c3c85ceaca1706f64da238955d23d6
	
}
