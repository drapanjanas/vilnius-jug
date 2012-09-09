package lt.jug.vilnius.osgi.example.app.dao.impl;

import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import lt.jug.vilnius.osgi.example.app.dao.InboxDao;
import lt.jug.vilnius.osgi.example.app.dao.MessageDao;
import lt.jug.vilnius.osgi.example.app.model.Inbox;
import lt.jug.vilnius.osgi.example.app.model.Message;
import lt.jug.vilnius.osgi.example.app.model.MessageContent;
import lt.jug.vilnius.osgi.example.app.model.RecipientMessage;
import lt.jug.vilnius.osgi.example.app.model.RecipientMessage.RecipientStatus;
import lt.jug.vilnius.osgi.example.app.model.SenderMessage;
import lt.jug.vilnius.osgi.example.app.model.SenderMessage.SenderStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.common.base.Preconditions;

@Repository("messageDao")
public class MessageDaoImpl implements MessageDao {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private InboxDao inboxDao;
	
	@Override
	public List<Message> findByInboxAndFolder(String inboxAddress, String folder) {
		return entityManager.createQuery("from Message where inbox.id=:address and folder=:folder", Message.class)
				.setParameter("address", inboxAddress)
				.setParameter("folder", folder).getResultList();
	}

	@Override
	public Message getMessage(String inboxAddress, Long messageId) {
		return entityManager.createQuery(
				"from Message where inbox.id=:address and id=:id", Message.class)
				.setParameter("address", inboxAddress)
				.setParameter("id", messageId).getSingleResult();
	}

	@Override
	public SenderMessage createNewMessage(String inboxAddress, String folder, MessageContent content) {
		Inbox inbox = inboxDao.findByAddress(inboxAddress);
		SenderMessage message = new SenderMessage();
		message.setContent(content);
		message.setInbox(inbox);
		message.setFolder(folder);
		message.setSenderStatus(SenderStatus.DRAFT);
		entityManager.persist(message);
		return message;
	}
	
	@Override
	public String transmitMessage(String inboxAddress, Long messageId, List<String> destinationAddresses) {
		String transmissionUid = UUID.randomUUID().toString();
		SenderMessage source = (SenderMessage) getMessage(inboxAddress, messageId);
		Preconditions.checkArgument(source.getSenderStatus() != SenderStatus.SENT, "This message already was sent");
		
		source.setSenderStatus(SenderStatus.SENT);
		source.setFolder("sent");
		source.setTransmissionUid(transmissionUid);
		source = entityManager.merge(source);
		for (String destinationAddress : destinationAddresses) {
			RecipientMessage recipientMessage = new RecipientMessage();
			recipientMessage.setContent(source.getContent());
			recipientMessage.setFolder("inbox");
			recipientMessage.setReceipientStatus(RecipientStatus.RECEIVED);
			recipientMessage.setInbox(inboxDao.findByAddress(destinationAddress));
			recipientMessage.setTransmissionUid(transmissionUid);
			entityManager.persist(recipientMessage);
		}
		return transmissionUid;
	}
	
	@Override
	public SenderMessage updateMessageContent(String inboxAddress, Long messageId, MessageContent content) {
		Message message = getMessage(inboxAddress, messageId);
		Preconditions.checkArgument(message instanceof SenderMessage, "Can modify only sender messages");
		
		SenderMessage senderMessage = (SenderMessage) message;
		senderMessage.setContent(content);
		senderMessage = entityManager.merge(senderMessage);
		return senderMessage;
	}

	@Override
	public void deleteMessages(String inboxAddress, List<Long> idList) {
		// delete deleted permanently
		entityManager.createQuery(
				"delete from Message where id IN (:selection) and inbox.id = :address and folder = 'deleted'")
				.setParameter("selection", idList)
		.setParameter("address", inboxAddress).executeUpdate();
		// move other to deleted folder
		entityManager.createQuery(
				"update Message set folder= 'deleted' where id IN (:selection) and inbox.id = :address and folder != 'deleted'")
				.setParameter("selection", idList)
		.setParameter("address", inboxAddress).executeUpdate();
	}

	@Override
	public void updateRecipientMessageStatus(String inboxAddress, Long messageId, RecipientStatus newStatus ) {
		entityManager.createQuery(
				"update RecipientMessage set recipientStatus = :newStatus where id = :messageId and inbox.id = :address")
				.setParameter("messageId", messageId)
				.setParameter("newStatus", newStatus)
				.setParameter("address", inboxAddress).executeUpdate();
	}

}
