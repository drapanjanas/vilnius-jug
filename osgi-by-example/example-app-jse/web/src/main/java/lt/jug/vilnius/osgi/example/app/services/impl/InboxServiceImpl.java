package lt.jug.vilnius.osgi.example.app.services.impl;

import java.util.List;

import lt.jug.vilnius.osgi.example.app.dao.InboxDao;
import lt.jug.vilnius.osgi.example.app.dao.MessageDao;
import lt.jug.vilnius.osgi.example.app.dto.MessageDraft;
import lt.jug.vilnius.osgi.example.app.model.Inbox;
import lt.jug.vilnius.osgi.example.app.model.Message;
import lt.jug.vilnius.osgi.example.app.model.MessageContent;
import lt.jug.vilnius.osgi.example.app.model.RecipientMessage.RecipientStatus;
import lt.jug.vilnius.osgi.example.app.model.SenderMessage;
import lt.jug.vilnius.osgi.example.app.model.SenderMessage.SenderStatus;
import lt.jug.vilnius.osgi.example.app.services.InboxService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Preconditions;

@Service("inboxService")
public class InboxServiceImpl implements InboxService {

	@Autowired
	private InboxDao inboxDao;
	
	@Autowired
	private MessageDao messageDao;

	@Override
	@Transactional
	public Inbox createInbox(String address) {
		return inboxDao.createInbox(address);
	}

	@Override
	@Transactional
	public List<Message> showFolderContents(String address, String folder) {
		return messageDao.findByInboxAndFolder(address, folder);
	}

	@Override
	@Transactional
	public Long saveMessage(String address, MessageDraft messageDto,
			String folder) {
		if (messageDto.getId() != null) {
			SenderMessage message = (SenderMessage) messageDao.getMessage(address, messageDto.getId());
			Preconditions.checkState(
					message.getSenderStatus() != SenderStatus.SENT,
					"Can not modify sent messages");
			MessageContent content = new MessageContent();
			content.setFrom(address);
			copyContent(messageDto, content);
			message = messageDao.updateMessageContent(address, messageDto.getId(), content);
			return message.getId();

		} else {
			MessageContent content = new MessageContent();
			content.setFrom(address);
			copyContent(messageDto, content);
			SenderMessage message = messageDao.createNewMessage(address, "drafts", content);
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
		return messageDao.getMessage(address, messageId);
	}

	@Override
	@Transactional
	public void deleteMessages(String address, String folder,
			List<Long> selected) {
		messageDao.deleteMessages(address, selected);
	}

	@Override
	@Transactional
	public void markAsRead(String address, Long messageId) {
		messageDao.updateRecipientMessageStatus(address, messageId, RecipientStatus.READ);
	}

}
