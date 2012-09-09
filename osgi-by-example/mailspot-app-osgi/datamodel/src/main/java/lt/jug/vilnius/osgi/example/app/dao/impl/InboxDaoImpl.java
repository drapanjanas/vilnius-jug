package lt.jug.vilnius.osgi.example.app.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import lt.jug.vilnius.osgi.example.app.dao.InboxDao;
import lt.jug.vilnius.osgi.example.app.model.Inbox;
import lt.jug.vilnius.osgi.example.app.model.Inbox.Status;

import org.springframework.stereotype.Repository;

@Repository("inboxDao")
public class InboxDaoImpl implements InboxDao{
			
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public Inbox findByAddress(String address) {
		return entityManager.find(Inbox.class, address);
	}

	@Override
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
}
