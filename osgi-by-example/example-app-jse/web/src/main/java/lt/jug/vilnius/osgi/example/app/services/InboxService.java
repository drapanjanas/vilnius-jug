package lt.jug.vilnius.osgi.example.app.services;

import java.util.List;

import lt.jug.vilnius.osgi.example.app.dto.MessageDraft;
import lt.jug.vilnius.osgi.example.app.model.Inbox;
import lt.jug.vilnius.osgi.example.app.model.Message;

public interface InboxService {
	Inbox createInbox(String address);
	
	List<Message> showFolderContents(String address, String folder);

	Long  saveMessage(String address, MessageDraft message, String folder);

	Message getMessage(String address, Long messageId);
	
	void markAsRead(String address, Long messageId);

	void deleteMessages(String address, String folder, List<Long> selected);
}
