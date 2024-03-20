package com.jfsd.sdp.controller;



import java.util.*;

import javax.sql.rowset.serial.SerialException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jfsd.sdp.model.Client;
import com.jfsd.sdp.model.ClientOrderMapping;
import com.jfsd.sdp.model.DeliveryBoy;
import com.jfsd.sdp.model.Items;
//import com.jfsd.sdp.model.Order;
import com.jfsd.sdp.model.Restaurants;
import com.jfsd.sdp.service.ClientService;
import com.jfsd.sdp.service.DeliveryBoyService;
import com.jfsd.sdp.service.ItemsService;
import com.jfsd.sdp.service.OrderService;
//import com.jfsd.sdp.service.OrderService;
import com.jfsd.sdp.service.RestaurantsService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Blob;
import java.sql.SQLException;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import io.github.cdimascio.dotenv.Dotenv;


@Controller
public class ClientController {
	
	@Autowired
	private ClientService clientService;
	
	@Autowired
	private ItemsService itemsService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private RestaurantsService restaurantsService;
	
	@Autowired
	private DeliveryBoyService deliveryBoyService;
	
	@GetMapping("/")
	public ModelAndView demo()
	{
		ModelAndView mv=new ModelAndView("index");
		return mv;
	}
	
	@GetMapping("/login")
	public ModelAndView login()
	{
		ModelAndView mv=new ModelAndView("login");
		return mv;
	}
	
	@GetMapping("/regprompt")
	public ModelAndView regprompt()
	{
		ModelAndView mv=new ModelAndView("regprompt");
		return mv;
	}
	@GetMapping("/userreg")
	public ModelAndView clientRegistration()
	{
		ModelAndView mv=new ModelAndView();
		mv.setViewName("userSignup");
		return mv;
	}

	@PostMapping("/addclient")
	public ModelAndView addClient(HttpServletRequest request,RedirectAttributes redirectAttributes)
	{
		ModelAndView mv=new ModelAndView();
		String fname=request.getParameter("fname");
		String lname=request.getParameter("lname");
		String email=request.getParameter("email");
		String mobile=request.getParameter("mobile");
		String gender=request.getParameter("gender");
		String dob = request.getParameter("dob");
		String location=request.getParameter("location");
		String password=request.getParameter("pwd1");
		Client c=new Client();
		c.setFname(fname);
		c.setLname(lname);
		c.setEmail(email);
		c.setMobile(mobile);
		c.setLocation(location);
		c.setPassword(password);
		c.setDob(dob);
		c.setGender(gender);
		String ack=clientService.addClient(c);
		mv.setViewName("redirect:/login");
		redirectAttributes.addFlashAttribute("ack", ack);
		return mv;
		
	}
	@PostMapping("/checkclientlogin")
	public ModelAndView checkClientLogin(HttpServletRequest request,RedirectAttributes redirectAttributes)
	{
		String email=request.getParameter("email");
		String pswd=request.getParameter("pwd");
		Client c= clientService.checkClientLogin(email, pswd);
		ModelAndView mv=new ModelAndView();
		if(email.equals("klu@gmail.com") && pswd.equals("klu"))
		{
			mv.setViewName("redirect:/adminhome");
			return mv;
		}
		if(c!=null)
		{
			mv.setViewName("redirect:/home");
			//mv.addObject("ack","Login Success !");
			//redirectAttributes.addFlashAttribute("ack", "Login Success !");
			HttpSession session =request.getSession();
			session.setAttribute("clientId",c.getId());
			session.setAttribute("success", "Login Success !!");
			return mv;
		}
		mv.setViewName("redirect:/login");
		//mv.addObject("ack","Login Failed !");
		redirectAttributes.addFlashAttribute("failure", "Login Failed !");
		return mv;
	}
	@GetMapping("/home")// change later
	public ModelAndView clientHome(HttpServletRequest request)
	{
		HttpSession session =request.getSession();
		ModelAndView mv=new ModelAndView();
		if(session.getAttribute("clientId")!=null)
		{
			//ModelAndView mv=new ModelAndView();
			mv.setViewName("home");
			List<Items> items=itemsService.ViewAllProducts();
			mv.addObject("items", items);
			return mv;
		}
		mv.setViewName("redirect:/login");
		return mv;
		
	}
	@GetMapping("/userprofile")
	public ModelAndView clientProfile(HttpServletRequest request)
	{
		ModelAndView mv=new ModelAndView();
		HttpSession session=request.getSession();
		int id=0;
		Object o=session.getAttribute("clientId");
		if(o==null)
			mv.setViewName("redirect:/login");
		else {
			id=(int)o;
			Client client=clientService.viewClientById(id);
			mv.setViewName("Profile");
			mv.addObject("client",client);
		}
		return mv;
	}
	
//	@GetMapping("/placeorder/{id}")
//	public ModelAndView addOrder(@PathVariable("id") int id,HttpServletRequest request)
//	{
//		ModelAndView mv=new ModelAndView();
//		System.out.println(id);
//		Items i=itemsService.viewItemById(id);
//		//System.out.println(i);
//		Order o=new Order();
//		HttpSession session =request.getSession();
//		Client c=(Client)session.getAttribute("client");
//		int clientId=c.getId();
//		o.setItemid(id);
//		o.setCid(clientId);
//		o.setItemname(i.getName());
//		o.setStatus(true);
//		o.setPrice(i.getPrice());
//		orderService.addOrder(o);
//		mv.setViewName("redirect:/home");
//		return mv;
//	}
	
	@GetMapping("/vieworders/{id}")
	public ModelAndView viewOrders(@PathVariable("id") int id)
	{
		List<ClientOrderMapping> lst=orderService.getClientOrdersById(id);
		ModelAndView mv=new ModelAndView("vieworders");
		mv.addObject("ordlist", lst);
		return mv;
	}
	
	@PostMapping("/placeorder/{id}")
	@ResponseBody
	public String placeOrder(@PathVariable("id") int id,HttpServletRequest request)
	{
		HttpSession session=request.getSession();
		
		int cid=(int)session.getAttribute("clientId");
		System.out.println("client Id"+cid);
		Items i=itemsService.ViewProductByID(id);
		//System.out.println("Item "+i);
		long n=orderService.checkOrderMapping(i, clientService.viewClientById(cid));
		if(n>0) {
			System.out.println("Already Ordered !!!");
		}
		else {
			ClientOrderMapping obj=new ClientOrderMapping();
			obj.setClient(clientService.viewClientById(cid));
			System.out.println("client "+clientService.viewClientById(cid));
			obj.setItem(i);
			obj.setPrice(i.getPrice());
			obj.setQuantity(1);
			obj.setName(i.getName());
			obj.setRestaurantName("");
			orderService.placeClientsOrder(obj);
		}
		return "added";
	}
	
	@PostMapping("/updatedetails")
	public ModelAndView updateProfile(HttpServletRequest request)
	{
		String fname=request.getParameter("fname");
		String lname=request.getParameter("lname");
		String email=request.getParameter("email");
		String pswd=request.getParameter("pswd");
		String mobile=request.getParameter("mobile");
		String loc=request.getParameter("location");
		Client c=new Client();
		HttpSession session=request.getSession();
		Client sessionClient=(Client)session.getAttribute("client");
		c.setId(sessionClient.getId());
		c.setFname(fname);
		c.setLname(lname);
		c.setEmail(email);
		c.setPassword(pswd);
		c.setLocation(loc);
		c.setMobile(mobile);
		System.out.println(c);
		clientService.updateClient(c);
		ModelAndView mv=new ModelAndView();
		mv.setViewName("redirect:/login");
		return mv;
	}
	@GetMapping("/adminhome")
	public ModelAndView adminHome()
	{
		ModelAndView mv=new ModelAndView("admin");
		long restCount=restaurantsService.count();
		long orderCount=orderService.orderCount();
		long totalSale=orderService.totalSale();
		mv.addObject("restcount",restCount);
		mv.addObject("ordcount", orderCount);
		mv.addObject("sale", totalSale);
		return mv;
	}
	
	@GetMapping("/restaurants")
	public ModelAndView restaurants()
	{
		List<Restaurants> rlist=restaurantsService.viewAllRestaurants();
		ModelAndView mv=new ModelAndView("restaurants");
		mv.addObject("rlist", rlist);
		return mv;
	}
	@GetMapping("/customers")
	public ModelAndView viewAllCustomers()
	{
		List<Client> lst=clientService.viewAllClients();
		ModelAndView mv=new ModelAndView("customers");
		mv.addObject("lst", lst);
		return mv;
	}
	
	
	@DeleteMapping("/remove/{oid}")
	@ResponseBody
	public String removeOrder(@PathVariable("oid") int id)
	{
		orderService.deleteOrder(id);
		ModelAndView mv=new ModelAndView();
		return "Removed !!!";
		
	}
	@GetMapping("/aboutus")
	public String aboutUs()
	{
		return "aboutus";
	}
	
	
	
	@GetMapping("/checkout-session")
	public String paynow(HttpServletRequest request,HttpServletResponse response) throws StripeException
	{
		HttpSession session =request.getSession();
		int cid=(int)session.getAttribute("clientId");
		Client c=clientService.viewClientById(cid);
		Dotenv dotenv=Dotenv.load();
		String email=c.getEmail();
		String name=c.getFname()+" "+c.getLname();
		List<ClientOrderMapping> lst=orderService.getClientOrdersById(cid);
		Stripe.apiKey=dotenv.get("STRIPE_KEY");
		 String baseUrl = "http://localhost:8080";
		List<SessionCreateParams.LineItem> lineItems = new ArrayList<>();
		for (ClientOrderMapping item : lst) {
		    SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()
		        .setPriceData(
		            SessionCreateParams.LineItem.PriceData.builder()
		                .setCurrency("inr")
		                .setProductData(
		                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
		                        .setName(item.getName())
		                        .build()
		                )
		                .setUnitAmount((long) (item.getPrice() * 100)) // Assuming 'getPrice()' returns the price in the smallest currency unit (e.g., cents).
		                .build()
		        )
		        .setQuantity((long) item.getQuantity()) // Assuming 'getQuantity()' returns the quantity of the item.
		        .build();
		    lineItems.add(lineItem);
		}

		SessionCreateParams params = SessionCreateParams.builder()
		    .addAllLineItem(lineItems)
		    .setMode(SessionCreateParams.Mode.PAYMENT)
		    .setSuccessUrl(baseUrl+"/home")
		    .setCancelUrl(baseUrl+"/home")
		    .build();

		Session paymentSession = Session.create(params);
		String redirectURL = paymentSession.getUrl();
		return "redirect:" + redirectURL;

	}
	
	
	@GetMapping("/deliverylogin")
	public String deliveryLogin()
	{
		return "deliverylogin";
	}
	
	@GetMapping("/deliverysignup")
	public String deliverySignup()
	{
		return "deliverysignup";
	}
	
	@PostMapping("/insertdboy")
	public ModelAndView deliveryReg(HttpServletRequest request,RedirectAttributes attributes)
	{
		String mssg = null;
		ModelAndView mv = new ModelAndView();		
		
		try {
			String fullname = request.getParameter("fullname");
			String nickname = request.getParameter("nickname");
			String userid  = request.getParameter("userid");
			String language = request.getParameter("language");
			String password = request.getParameter("password");
			String repeatpassword = request.getParameter("repeatpassword");
			String additionalinfo = request.getParameter("additionalinfo");
			String street = request.getParameter("street");
			String city = request.getParameter("city");	
			String country = request.getParameter("country");
			String zipcode = request.getParameter("zipcode");
			String address = request.getParameter("address");
			String phonenumber = request.getParameter("phonenumber");
			String email = request.getParameter("email");
			
			if(!password.equals(repeatpassword))
			{
				mv.setViewName("redirect:/insertdboy");
				attributes.addFlashAttribute("alerterror","Passwords Are Not Same");
				return mv;
			}
			
			DeliveryBoy db = new DeliveryBoy();
			db.setDelAddress(address);
			db.setDelCity(city);
			db.setDelCountry(country);
			db.setDelemail(email);
			db.setDelfullname(fullname);
			db.setDelId(userid);
			db.setDelnickname(nickname);
			db.setDelstreet(street);
			db.setDellanguage(language);
			db.setDelzipCode(zipcode);
			db.setDelpassword(password);
			db.setDelphone(phonenumber);
			mssg = deliveryBoyService.addUser(db);
			
			mv.setViewName("redirect:/insertdboy");
			attributes.addFlashAttribute("alertnow",mssg);
			 return mv;
			
		} catch (Exception e) {
			
			mssg = e.getMessage();
			mv.setViewName("deliveryhome");
			System.out.println("Not Registerd");	
		}
		return mv;
	}
	
	@GetMapping("/deliveryhome")
	public String deliveryHome()
	{
		return "deliveryhome";
	}
	
	@PostMapping("/deliveryloginvalidate")
	  public ModelAndView loginvalidate(HttpServletRequest request,RedirectAttributes attributes)
	  {
	    String mssg = null;
	    ModelAndView mv = new ModelAndView("/deliveryhome");
	    try {
	      String userid = request.getParameter("uname");
	      String password = request.getParameter("pwd");
	      System.out.println(userid);
	      System.out.println(password);
	      DeliveryBoy d =  deliveryBoyService.loginvalidate(userid);
	      
	      if(d==null) 
	      {
	        mssg = "User Not Found";
	        mv.setViewName("redirect:/deliverylogin");
	        attributes.addFlashAttribute("userstatus",mssg);
	        return mv;
	      }
	      
	      if(d!=null)
	      {
	        if(!d.getDelpassword().equals(password)) {
	          mssg = "Passwords Are Not Matched";
	        mv.setViewName("redirect:/delivery");
	        attributes.addFlashAttribute("userstatus",mssg);
	        return mv;
	        }
	      }
	      
	      HttpSession session = request.getSession();
	      session.setAttribute("deliveryobj", d);
	      mv.setViewName("redirect:/deliveryhome");    
	      
	    } catch (Exception e) {
	      mssg = e.getMessage();
	    }
	    return mv;
	  }
	
	// Restaurant Begins
	  @GetMapping("/restaurant")
	  
	     public String indexdemo()
	     {
	       return "restlogin";
	     }
	     @GetMapping("/restsignup")
	     public String signup()
	     {
	       return "restsignup";
	     }
	     
	     @PostMapping("/addrestaurant")
	 	public ModelAndView addRestaurant(HttpServletRequest request)
	 	{
	 		String name=request.getParameter("name");
	 		System.out.println(name);
	 		String location =request.getParameter("location");
	 		System.out.println(location);
	 		String contact=request.getParameter("contact");
	 		System.out.println(contact);
	 		String email=request.getParameter("email");
	 		String password=request.getParameter("pswd");
	 		Restaurants r=new Restaurants();
	 		r.setName(name);
	 		r.setEmail(email);
	 		r.setLocation(location);
	 		r.setPassword(password);
	 		r.toString();
	 		restaurantsService.addRestaurant(r);
	 		ModelAndView mv=new ModelAndView("redirect:/restaurant");
	 		return mv;
	 	}
	    
	     @PostMapping("/validaterestlogin")
	     public ModelAndView validateRestLogin(HttpServletRequest request)
	     {
	    	 String email=request.getParameter("email");
		 	 String password=request.getParameter("pswd");
		 	 Restaurants r= restaurantsService.restaurantLogin(email,password);
		 	 ModelAndView mv=new ModelAndView();
		 	 if(r!=null)
		 		 mv.setViewName("redirect:/resthome");
		 	 else
		 		 mv.setViewName("redirect:/restaurant");
		 	return mv;
	     }
	     
	     @GetMapping("/resthome")
	     public String home()
	     {
	       return "resthome";
	     }
	  
	  
	   @GetMapping("additems")
	   public ModelAndView addproductdemo()
	   {
	     ModelAndView mv = new ModelAndView("additems");
	     return mv;
	   }
	   //MultipartFile to get images 
	   @PostMapping("insertitem")
	   public ModelAndView insertproductdemo(HttpServletRequest request,@RequestParam("productimage") MultipartFile file) throws IOException, SerialException, SQLException
	   {
	     String msg = null;
	     
	     ModelAndView mv = new ModelAndView();
	     
	     try
	     {
	     String category = request.getParameter("category");
	     String name = request.getParameter("name");
	     String description = request.getParameter("description");
	     Double price = Double.valueOf(request.getParameter("price"));
	     
	     
	      byte[] bytes = file.getBytes();
	      Blob blob = new javax.sql.rowset.serial.SerialBlob(bytes);
	      
	      Items p = new Items();
	      p.setCategory(category);
	      p.setName(name);
	      p.setDescription(description);
	      p.setPrice(price);
	      
	      p.setProductimage(blob);
	      
	      msg=itemsService.AddProduct(p);
	      System.out.println(msg);
	      mv.setViewName("resthome");
	      //mv.addObject("message",msg);
	     }    
	     catch(Exception e)
	     {
	       msg = e.getMessage();
	       System.out.println(msg.toString());
	       mv.setViewName("producterror");
	       mv.addObject("message",msg);
	     }
	     return mv;
	   }
	   @GetMapping("viewallitems")
	   public ModelAndView viewallprodsdemo()
	   {
	     List<Items> productlist = itemsService.ViewAllProducts();
	     
	     ModelAndView mv = new ModelAndView("viewallitems");
	     
	     mv.addObject("productlist", productlist);
	     
	     return mv;
	   }
	   
	@GetMapping("displayprodimage")
	public ResponseEntity<byte[]> displayprodimagedemo(@RequestParam("id") int id) throws IOException, SQLException
	{
	  Items items =  itemsService.ViewProductByID(id);
	  byte [] imageBytes = null;
	  imageBytes = items.getProductimage().getBytes(1,(int) items.getProductimage().length());

	  return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageBytes);
	}
	
	
	@GetMapping("viewproductbyid")
	public ModelAndView  viewproductbyiddemo()
	{
	     List<Items> productlist=itemsService.ViewAllProducts();
	     ModelAndView mv=new ModelAndView("viewproductbyid");
	     mv.addObject("productlist",productlist);
	     return mv;
	}
	   
	   @PostMapping("displayproduct")
	   public ModelAndView displayproductdemo(HttpServletRequest request)
	   {
	     int pid=Integer.parseInt(request.getParameter("pid"));
	     Items items=itemsService.ViewProductByID(pid);
	     ModelAndView mv=new ModelAndView("displayproduct");
	     mv.addObject("product",items);
	     return mv;
	   }
	   @GetMapping("viewproductbycategory")
	   public ModelAndView viewProductsByCategory()
	   {
	     ModelAndView mv=new ModelAndView("viewproductsbycategory");
	     return mv;
	   }
	   @PostMapping("displayproductsbycategory")
	   public ModelAndView viewProductsByCategory(HttpServletRequest request)
	   {
	     String category=request.getParameter("category");
	     List<Items> productlist=itemsService.viewAllProductsByCategory(category);
	     ModelAndView mv=new ModelAndView("displayproductsbycategory");
	     mv.addObject("productlist",productlist);
	     return mv;
	   }
	   
	   @GetMapping("/availableitems")
	   public ModelAndView availableItems()
	   {
		   ModelAndView mv=new ModelAndView("availableitems");
		   List<Items> i=itemsService.ViewAllProducts();
		   mv.addObject("items", i);
		   return mv;
	   } 
	
}
