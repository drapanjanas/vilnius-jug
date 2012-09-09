package lt.jug.vilnius.osgi.example.app.services;

import java.util.List;


public interface MailService {
	String sendMessage(String inboxAddress, Long messageId, List<String> to);
}
