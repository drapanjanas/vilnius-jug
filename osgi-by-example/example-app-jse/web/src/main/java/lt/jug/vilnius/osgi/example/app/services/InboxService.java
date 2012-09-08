package lt.jug.vilnius.osgi.example.app.services;

import java.util.List;

import lt.jug.vilnius.osgi.example.app.dto.NewMessage;
import lt.jug.vilnius.osgi.example.app.model.Inbox;
import lt.jug.vilnius.osgi.example.app.model.Message;

public interface InboxService {
	Inbox createInbox(String address);
	
	void activateInbox(String address);

	List<Message> showFolderContents(String address, String folder);

	void saveMessage(String address, NewMessage message, String string);
}
