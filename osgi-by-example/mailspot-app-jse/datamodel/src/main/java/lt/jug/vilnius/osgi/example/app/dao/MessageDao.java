package lt.jug.vilnius.osgi.example.app.dao;

import java.util.List;

import lt.jug.vilnius.osgi.example.app.model.Message;
import lt.jug.vilnius.osgi.example.app.model.MessageContent;
import lt.jug.vilnius.osgi.example.app.model.RecipientMessage;
import lt.jug.vilnius.osgi.example.app.model.SenderMessage;
import lt.jug.vilnius.osgi.example.app.model.RecipientMessage.RecipientStatus;

public interface MessageDao {
	List<Message> findByInboxAndFolder(String inboxAddress, String folder);
	
	Message getMessage(String inboxAddress, Long messageId);
	
	String transmitMessage(String inboxAddress, Long messageId, List<String> destinationAddresses);
	
	SenderMessage createNewMessage(String inboxAddress, String folder, MessageContent content);
	
	SenderMessage updateMessageContent(String inboxAddress, Long messageId, MessageContent content);
	
	void updateRecipientMessageStatus(String inboxAddress, Long messageId, RecipientStatus newStatus); 
	
	void deleteMessages(String inboxAddress, List<Long> idList); 
}
