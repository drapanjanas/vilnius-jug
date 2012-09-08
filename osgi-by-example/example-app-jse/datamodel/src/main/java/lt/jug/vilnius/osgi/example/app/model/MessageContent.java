package lt.jug.vilnius.osgi.example.app.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "message_contents")
@SequenceGenerator(name="seq_message_content_id", initialValue=1, allocationSize=100)
public class MessageContent {
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq_message_content_id")
	private Long id;
	
	@Column(name = "subject")
	private String subject;
	
	@Column(name = "body")
	private String body;
	
	@Column(name = "from_value", nullable = false, updatable = false)
	private String from;
	
	@Column(name = "to_value")
	private String to;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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
