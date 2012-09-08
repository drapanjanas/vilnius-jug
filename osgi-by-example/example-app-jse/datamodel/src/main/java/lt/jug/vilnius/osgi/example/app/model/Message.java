package lt.jug.vilnius.osgi.example.app.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "messages")
@SequenceGenerator(name="seq_message_id", initialValue=1, allocationSize=100)
@DiscriminatorColumn(name="type")
public abstract class Message {
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq_message_id")
	private Long id;
	
	@OneToOne
	@JoinColumn(name = "inbox", nullable=false)
	private Inbox inbox;
	
	@Column(name = "folder", nullable = false)
	private String folder;
	
	@ManyToOne
	@JoinColumn(name = "content_id", nullable = false)
	private MessageContent content;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Inbox getInbox() {
		return inbox;
	}

	public void setInbox(Inbox inbox) {
		this.inbox = inbox;
	}

	public String getFolder() {
		return folder;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}

	public MessageContent getContent() {
		return content;
	}

	public void setContent(MessageContent content) {
		this.content = content;
	}
	
} 
