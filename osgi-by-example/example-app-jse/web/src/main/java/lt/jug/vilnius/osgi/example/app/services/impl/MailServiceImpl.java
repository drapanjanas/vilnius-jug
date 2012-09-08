package lt.jug.vilnius.osgi.example.app.services.impl;

import org.springframework.stereotype.Service;

import lt.jug.vilnius.osgi.example.app.dto.NewMessage;
import lt.jug.vilnius.osgi.example.app.services.MailService;

@Service("mailService")
public class MailServiceImpl implements MailService {

	@Override
	public void sendMessage(String fromAddress, NewMessage message) {

	}

}
