package lt.jug.vilnius.osgi.example.app.dao;

import lt.jug.vilnius.osgi.example.app.model.Inbox;

public interface InboxDao {
	Inbox findByAddress(String address);

	Inbox createInbox(String address);
}
