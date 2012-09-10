package lt.jug.vilnius.osgi.example.app.dao.impl;

import lt.jug.vilnius.osgi.example.app.dao.InboxDao;
import lt.jug.vilnius.osgi.example.app.model.Inbox;
import lt.jug.vilnius.osgi.example.app.model.Inbox.Status;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("inboxDao")
public class InboxDaoImpl implements InboxDao{
			
	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public Inbox findByAddress(String address) {
		return (Inbox) sessionFactory.getCurrentSession().get(Inbox.class, address);
	}

	@Override
	public Inbox createInbox(String address) {
		Inbox inbox = findByAddress(address);
		if (inbox != null) {
			return inbox;
		}
		inbox = new Inbox();
		inbox.setAddress(address);
		inbox.setStatus(Status.ACTIVE);
		sessionFactory.getCurrentSession().save(inbox);
		return inbox;
	}
}
