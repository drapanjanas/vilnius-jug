package lt.jug.vilnius.osgi.example.app.services;

import lt.jug.vilnius.osgi.example.app.dto.NewMessage;

public interface MailService {
	void sendMessage(String fromAddress, NewMessage message);
}
