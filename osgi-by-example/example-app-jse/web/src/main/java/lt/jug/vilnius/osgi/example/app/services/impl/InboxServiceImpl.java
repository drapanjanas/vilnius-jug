package lt.jug.vilnius.osgi.example.app.services.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import lt.jug.vilnius.osgi.example.app.dto.MessageDraft;
import lt.jug.vilnius.osgi.example.app.model.Inbox;
import lt.jug.vilnius.osgi.example.app.model.Inbox.Status;
import lt.jug.vilnius.osgi.example.app.model.Message;
import lt.jug.vilnius.osgi.example.app.model.MessageContent;
import lt.jug.vilnius.osgi.example.app.model.SenderMessage;
import lt.jug.vilnius.osgi.example.app.model.SenderMessage.SenderStatus;
import lt.jug.vilnius.osgi.example.app.services.InboxService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Preconditions;

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
		inbox.setStatus(Status.ACTIVE);
		entityManager.persist(inbox);
		return inbox;
	}

	@Override
	@Transactional
	public List<Message> showFolderContents(String address, String folder) {
		return entityManager
				.createQuery(
						"from Message where inbox.id=:address and folder=:folder",
						Message.class).setParameter("address", address)
				.setParameter("folder", folder).getResultList();
	}

	@Override
	@Transactional
	public Long saveMessage(String address, MessageDraft messageDto,
			String folder) {
		Inbox inbox = entityManager.find(Inbox.class, address);
		if (messageDto.getId() != null) {
			SenderMessage message = entityManager
					.createQuery(
							"from SenderMessage where inbox.id=:address and id=:id",
							SenderMessage.class)
					.setParameter("address", address)
					.setParameter("id", messageDto.getId()).getSingleResult();
			Preconditions.checkState(
					message.getSenderStatus() != SenderStatus.SENT,
					"Can not modify sent messages");

			MessageContent content = message.getContent();
			copyContent(messageDto, content);
			content = entityManager.merge(content);
			message.setFolder(folder);
			message = entityManager.merge(message);
			return message.getId();

		} else {
			MessageContent content = new MessageContent();
			content.setFrom(address);
			copyContent(messageDto, content);
			entityManager.persist(content);

			SenderMessage message = new SenderMessage();
			message.setContent(content);
			message.setFolder(folder);
			message.setInbox(inbox);
			message.setSenderStatus(SenderStatus.DRAFT);
			entityManager.persist(message);
			return message.getId();
		}

	}

	private void copyContent(MessageDraft messageDto, MessageContent content) {
		content.setTo(messageDto.getTo());
		content.setSubject(messageDto.getSubject());
		content.setBody(messageDto.getBody());
	}

	@Override
	@Transactional
	public Message getMessage(String address, Long messageId) {
		return entityManager
				.createQuery(
						"from Message where inbox.id=:address and  id=:messageId",
						Message.class).setParameter("address", address)
				.setParameter("messageId", messageId).getSingleResult();
	}

	@Override
	@Transactional
	public void deleteMessages(String address, String folder,
			List<Long> selected) {
		entityManager
				.createQuery(
						"delete from Message where id IN (:selection) and inbox.id = :address and folder=:folder")
						.setParameter("selection", selected)
				.setParameter("address", address)
				.setParameter("folder", folder).executeUpdate();
	}

}
