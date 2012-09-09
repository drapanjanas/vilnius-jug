package lt.jug.vilnius.osgi.example.app.controller;

import javax.validation.Valid;

import lt.jug.vilnius.osgi.example.app.model.Inbox;
import lt.jug.vilnius.osgi.example.app.services.InboxService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * Sample controller for going to the home page with a message
 */
@Controller
@SessionAttributes("currentInbox")
public class HomeController {

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	private InboxService inboxService;
	
	/**
	 * Selects the home page and populates the model with a message
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Model model) {
		logger.info("Welcome home!");
		
		model.addAttribute("controllerMessage","Please login to inbox");
		model.addAttribute("inbox", new Inbox());
		return "home";
	}
	
	@RequestMapping(value = "/selectInbox", method = RequestMethod.POST)
	public String select(@Valid @ModelAttribute("inbox") Inbox inbox, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "home";
		}
		
		logger.info("Selected inbox [address={}]", inbox.getAddress());
		inboxService.createInbox(inbox.getAddress());
		
		model.addAttribute("currentInbox",inbox);
				
		return "redirect:mail/inbox";
	}
	
	

}
