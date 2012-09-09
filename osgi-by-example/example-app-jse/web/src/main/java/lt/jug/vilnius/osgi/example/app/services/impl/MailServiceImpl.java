package lt.jug.vilnius.osgi.example.app.services.impl;

import java.util.List;

import lt.jug.vilnius.osgi.example.app.dao.MessageDao;
import lt.jug.vilnius.osgi.example.app.services.MailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("mailService")
public class MailServiceImpl implements MailService {

	@Autowired
	private MessageDao messageDao;
	
	@Override
	@Transactional
	public void sendMessage(String inboxAddress, Long messageId, List<String> to) {
		messageDao.transmitMessage(inboxAddress, messageId, to);
	}
}
