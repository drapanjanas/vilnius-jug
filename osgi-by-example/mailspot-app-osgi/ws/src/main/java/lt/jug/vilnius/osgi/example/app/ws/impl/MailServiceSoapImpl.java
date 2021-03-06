package lt.jug.vilnius.osgi.example.app.ws.impl;

import lt.jug.vilnius.osgi.example.app.dto.MessageDraft;
import lt.jug.vilnius.osgi.example.app.services.InboxService;
import lt.jug.vilnius.osgi.example.app.services.MailService;
import lt.vilnius_jug.girenko.examples.services.mailservice.RecipientList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Joiner;

public class MailServiceSoapImpl implements lt.vilnius_jug.girenko.examples.services.mailservice.MailService {

	private MailService mailService;
	
	private InboxService inboxService;
		
	@Override
	public String sendMessage(String from, RecipientList to, String subject, String body) {
		MessageDraft messageDraft = new MessageDraft();
		messageDraft.setTo(Joiner.on(",").join(to.getRecipient()));
		messageDraft.setSubject(subject);
		messageDraft.setBody(body);
		Long messageId = inboxService.saveMessage(from, messageDraft, "drafts");
		return mailService.sendMessage(from, messageId, to.getRecipient());
	}

	public void setMailService(MailService mailService) {
		this.mailService = mailService;
	}

	public void setInboxService(InboxService inboxService) {
		this.inboxService = inboxService;
	}
}
