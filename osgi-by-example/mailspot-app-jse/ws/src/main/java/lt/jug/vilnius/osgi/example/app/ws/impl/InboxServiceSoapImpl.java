package lt.jug.vilnius.osgi.example.app.ws.impl;

import java.util.List;

import lt.jug.vilnius.osgi.example.app.model.Message;
import lt.jug.vilnius.osgi.example.app.model.RecipientMessage;
import lt.jug.vilnius.osgi.example.app.model.SenderMessage;
import lt.jug.vilnius.osgi.example.app.services.InboxService;
import lt.vilnius_jug.girenko.examples.services.inboxservice.MessageStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

@Service("inboxServiceSoap")
public class InboxServiceSoapImpl implements lt.vilnius_jug.girenko.examples.services.inboxservice.InboxService {
	
	@Autowired
	public InboxService inboxService;
	
	@Override
	public List<String> getMessageList(String inboxAddress, String folder) {
		List<Message> messages = inboxService.showFolderContents(inboxAddress, folder);
		return Lists.transform(messages, new Function<Message, String>() {
			@Override
			public String apply(Message message) {
				return message.getId().toString();
			}
			
		});
	}

	@Override
	public lt.vilnius_jug.girenko.examples.services.inboxservice.Message getMessage(String inboxAddress, String messageId) {
		Long id = Long.valueOf(messageId);
		Message message = inboxService.getMessage(inboxAddress, id);
		lt.vilnius_jug.girenko.examples.services.inboxservice.Message response = new lt.vilnius_jug.girenko.examples.services.inboxservice.Message();
		response.setFrom(message.getContent().getFrom());
		response.setTo(message.getContent().getTo());
		response.setSubject(message.getContent().getSubject());
		response.setBody(message.getContent().getBody());
		if (message instanceof SenderMessage) {
			response.setStatus(MessageStatus.valueOf(((SenderMessage) message).getSenderStatus().name()));
		}
		if (message instanceof RecipientMessage) {
			response.setStatus(MessageStatus.valueOf(((RecipientMessage) message).getRecipientStatus().name()));
		}
		response.setTransmissionUid(message.getTransmissionUid());
		return response;
	}

}
