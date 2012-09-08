package lt.jug.vilnius.osgi.example.app.dao;

import java.util.List;

import lt.jug.vilnius.osgi.example.app.model.Inbox;

public interface InboxDao extends BaseDao<Inbox, String> {
	List<Inbox> findByStatus(Inbox.Status status);
}
