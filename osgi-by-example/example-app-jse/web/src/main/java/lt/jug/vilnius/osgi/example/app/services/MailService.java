package lt.jug.vilnius.osgi.example.app.services;

import java.util.List;


public interface MailService {
	void sendMessage(Long messageId, List<String> to);
}
