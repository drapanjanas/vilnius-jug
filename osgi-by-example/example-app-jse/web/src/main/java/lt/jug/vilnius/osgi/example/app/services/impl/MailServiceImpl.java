package lt.jug.vilnius.osgi.example.app.services.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import lt.jug.vilnius.osgi.example.app.model.Inbox;
import lt.jug.vilnius.osgi.example.app.model.MessageContent;
import lt.jug.vilnius.osgi.example.app.model.RecipientMessage;
import lt.jug.vilnius.osgi.example.app.model.RecipientMessage.RecipientStatus;
import lt.jug.vilnius.osgi.example.app.model.SenderMessage;
import lt.jug.vilnius.osgi.example.app.model.SenderMessage.SenderStatus;
import lt.jug.vilnius.osgi.example.app.services.MailService;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("mailService")
public class MailServiceImpl implements MailService {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	@Transactional
	public void sendMessage(Long messageId, List<String> to) {
		SenderMessage senderMesage= findMessage(messageId);
		
		MessageContent content;
		if ( senderMesage.getSenderStatus() == SenderStatus.SENT) {
			content = new MessageContent();
			BeanUtils.copyProperties(senderMesage.getContent(), content);
			entityManager.persist(content);
			SenderMessage sendrMesageCopy = new SenderMessage();
			senderMesage.setInbox(senderMesage.getInbox());
			senderMesage.setContent(content);
			senderMesage.setSenderStatus(SenderStatus.DRAFT);
			senderMesage.setFolder("drafts");
			entityManager.persist(sendrMesageCopy);
			senderMesage = sendrMesageCopy;
			
		} else {
			content = senderMesage.getContent();
		}
		
		for (String recipentAddress: to) {
			Inbox recipientInbox = findInbox(recipentAddress);
			RecipientMessage recipientMessage =  new RecipientMessage();
			recipientMessage.setInbox(recipientInbox);
			recipientMessage.setContent(content);
			recipientMessage.setFolder("inbox");
			recipientMessage.setReceipientStatus(RecipientStatus.RECEIVED);
			entityManager.persist(recipientMessage);
		}
		
		senderMesage.setFolder("sent");
		senderMesage.setSenderStatus(SenderStatus.SENT);
		entityManager.merge(senderMesage);
	}

	private SenderMessage findMessage(Long messageId) {
		return entityManager.find(SenderMessage.class, messageId);
	}
	
	private Inbox findInbox(String address) {
		return entityManager.find(Inbox.class, address);
	}
}
