package lt.jug.vilnius.osgi.example.app.controller;

import java.util.List;

import lt.jug.vilnius.osgi.example.app.dto.NewMessage;
import lt.jug.vilnius.osgi.example.app.model.Inbox;
import lt.jug.vilnius.osgi.example.app.model.Message;
import lt.jug.vilnius.osgi.example.app.services.InboxService;
import lt.jug.vilnius.osgi.example.app.services.MailService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes("currentInbox")
public class MailController {
	private static final Logger logger = LoggerFactory.getLogger(MailController.class);

	@Autowired
	private InboxService inboxService;
	
	@Autowired
	private MailService mailService;
	
	@RequestMapping(value = "/mail/{folder}", method = RequestMethod.GET)
	public String inbox(@ModelAttribute("currentInbox") Inbox inbox, 
			@PathVariable String folder, Model model) {
		List<Message> messages = inboxService.showFolderContents(inbox.getAddress(), folder);
		model.addAttribute("folder", folder);
		model.addAttribute("messages", messages);
		return "mail";
	}
	
	@RequestMapping(value = "/mail/compose", method = RequestMethod.GET)
	public String compose(Model model) {
		model.addAttribute("newMessage", new NewMessage());
		return "compose";
	}
	
	@RequestMapping(value = "/mail/compose", params="action=send", method = RequestMethod.POST)
	public String send(@ModelAttribute("currentInbox") Inbox inbox, 
			@ModelAttribute("newMessage") NewMessage message) {
		logger.info("Sending message");
		mailService.sendMessage(inbox.getAddress(), message);
		return "redirect:sent";
	}
	
	@RequestMapping(value = "/mail/compose", params="action=save", method = RequestMethod.POST)
	public String save (@ModelAttribute("currentInbox") Inbox inbox, 
			@ModelAttribute("newMessage") NewMessage message) {
		logger.info("Saving message");
		inboxService.saveMessage(inbox.getAddress(), message, "drafts");		
		return "redirect:drafts";
	}
}
