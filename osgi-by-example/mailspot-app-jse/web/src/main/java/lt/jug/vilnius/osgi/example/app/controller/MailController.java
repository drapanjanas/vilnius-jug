package lt.jug.vilnius.osgi.example.app.controller;

import java.util.List;

import lt.jug.vilnius.osgi.example.app.dto.IdSelection;
import lt.jug.vilnius.osgi.example.app.dto.MessageDraft;
import lt.jug.vilnius.osgi.example.app.model.Inbox;
import lt.jug.vilnius.osgi.example.app.model.Message;
import lt.jug.vilnius.osgi.example.app.model.SenderMessage;
import lt.jug.vilnius.osgi.example.app.model.SenderMessage.SenderStatus;
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

import com.google.common.collect.Lists;

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
		model.addAttribute("messageSelection", new IdSelection());
		return "mail";
	}
	
	@RequestMapping(value = "/mail/{folder}", params="action=delete", method = RequestMethod.POST)
	public String deleteSelected(@ModelAttribute("currentInbox") Inbox inbox, 
			@ModelAttribute IdSelection selection,  
			@PathVariable String folder ) {
		logger.info("Deleting " + selection.getSelected());
		inboxService.deleteMessages(inbox.getAddress(), folder, selection.getSelected());
		
		return "redirect:" + folder;
	}

	
	@RequestMapping(value = "/mail/{folder}/{messageId}", method = RequestMethod.GET)
	public String viewMessage(@ModelAttribute("currentInbox") Inbox inbox, 
			@PathVariable String folder, @PathVariable Long messageId, Model model) {
		Message message = inboxService.getMessage(inbox.getAddress(), messageId);
		
		model.addAttribute("folder", folder);
		
		if (message instanceof SenderMessage) {
			SenderMessage senderMessage = (SenderMessage) message;
			if (senderMessage.getSenderStatus() == SenderStatus.DRAFT) {
				MessageDraft newMessage = new MessageDraft();
				newMessage.setId(senderMessage.getId());
				newMessage.setTo(senderMessage.getContent().getTo());
				newMessage.setSubject(senderMessage.getContent().getSubject());
				newMessage.setBody(senderMessage.getContent().getBody());
				model.addAttribute("newMessage", newMessage);
				return "compose";
			}
		}
		model.addAttribute("message", message);
		inboxService.markAsRead(inbox.getAddress(), message.getId());
		return "message";
	}
		
	@RequestMapping(value = "/mail/compose", method = RequestMethod.GET)
	public String compose(Model model) {
		model.addAttribute("newMessage", new MessageDraft());
		return "compose";
	}
	
	@RequestMapping(value = "/mail/compose", params="action=send", method = RequestMethod.POST)
	public String send(@ModelAttribute("currentInbox") Inbox inbox, 
			@ModelAttribute("newMessage") MessageDraft message) {
		logger.info("Sending message");
		Long messageId = inboxService.saveMessage(inbox.getAddress(), message, "drafts");
		mailService.sendMessage(inbox.getAddress(), messageId, Lists.newArrayList(message.getTo().replace(" ","").split(",")));
		return "redirect:sent";
	}
	
	@RequestMapping(value = "/mail/compose", params="action=save", method = RequestMethod.POST)
	public String save (@ModelAttribute("currentInbox") Inbox inbox, 
			@ModelAttribute("newMessage") MessageDraft message) {
		logger.info("Saving message");
		inboxService.saveMessage(inbox.getAddress(), message, "drafts");
		return "redirect:drafts";
	}
}
