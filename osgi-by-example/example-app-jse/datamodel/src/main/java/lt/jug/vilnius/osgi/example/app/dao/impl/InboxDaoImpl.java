package lt.jug.vilnius.osgi.example.app.dao.impl;

import java.util.List;

import lt.jug.vilnius.osgi.example.app.dao.InboxDao;
import lt.jug.vilnius.osgi.example.app.model.Inbox;
import lt.jug.vilnius.osgi.example.app.model.Inbox.Status;

import org.springframework.stereotype.Repository;

@Repository("inboxDao")
public class InboxDaoImpl extends BaseDaoImpl<Inbox, String> implements InboxDao {
	
	@Override
	public List<Inbox> findByStatus(Status status) {
		return em.createQuery("from Inbox i where i.status = :status",Inbox.class).setParameter("status", status).getResultList();
	}

	@Override
	protected Class<Inbox> getEntityClass() {
		return Inbox.class;
	}
}
