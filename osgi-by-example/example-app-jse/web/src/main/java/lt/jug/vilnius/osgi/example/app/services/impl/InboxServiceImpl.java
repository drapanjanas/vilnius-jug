package lt.jug.vilnius.osgi.example.app.services.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import lt.jug.vilnius.osgi.example.app.dto.NewMessage;
import lt.jug.vilnius.osgi.example.app.model.Inbox;
import lt.jug.vilnius.osgi.example.app.model.Inbox.Status;
import lt.jug.vilnius.osgi.example.app.model.Message;
import lt.jug.vilnius.osgi.example.app.model.MessageContent;
import lt.jug.vilnius.osgi.example.app.model.SenderMessage;
import lt.jug.vilnius.osgi.example.app.model.SenderMessage.SenderStatus;
import lt.jug.vilnius.osgi.example.app.services.InboxService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("inboxService")
public class InboxServiceImpl implements InboxService {

	@PersistenceContext
	private EntityManager entityManager; 
	
	
	@Override
	@Transactional
	public Inbox createInbox(String address) {
		Inbox inbox = entityManager.find(Inbox.class, address);
		if (inbox != null) {
			return inbox;
		}
		inbox = new Inbox();
		inbox.setAddress(address);
		inbox.setStatus(Status.INCATIVE);
		entityManager.persist(inbox);
		return inbox;
	}

	@Override
	@Transactional
	public void activateInbox(String address) {
		Inbox inbox = entityManager.find(Inbox.class, address);
		inbox.setStatus(Status.ACTIVE);
		entityManager.merge(inbox);
	}

	@Override
	@Transactional
	public List<Message> showFolderContents(String address, String folder) {
		return entityManager.createQuery("from Message where inbox.id=:address and folder=:folder", Message.class)
			.setParameter("address", address)
			.setParameter("folder", folder).getResultList();
	}

	@Override
	@Transactional
	public void saveMessage(String address, NewMessage messageDto, String folder) {
		Inbox inbox = entityManager.find(Inbox.class, address);
		
		MessageContent content = new MessageContent();
		content.setFrom(address);
		content.setTo(messageDto.getTo());
		content.setSubject(messageDto.getSubject());
		content.setBody(messageDto.getBody());
		entityManager.persist(content);
		
		SenderMessage message = new SenderMessage();
		message.setContent(content);
		message.setFolder(folder);
		message.setInbox(inbox);
		message.setSenderStatus(SenderStatus.DRAFT);
		entityManager.persist(message);
	}

}
