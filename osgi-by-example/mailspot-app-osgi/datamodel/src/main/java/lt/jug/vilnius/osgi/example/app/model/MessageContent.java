package lt.jug.vilnius.osgi.example.app.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class MessageContent {
	
	@Column(name = "subject")
	private String subject;
	
	@Column(name = "body")
	private String body;
	
	@Column(name = "from_value", nullable = false, updatable = false)
	private String from;
	
	@Column(name = "to_value")
	private String to;
	
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}
} 
