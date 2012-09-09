package lt.jug.vilnius.osgi.example.app.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @author arturas
 *
 */
@Entity
@Table(name = "messages")
@DiscriminatorColumn(name="type")
public abstract class Message {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@OneToOne
	@JoinColumn(name = "inbox", nullable=false)
	private Inbox inbox;
	
	@Column(name = "folder", nullable = false)
	private String folder;
	
	@Embedded
	private MessageContent content;
	
	@Column(name="transmission_uid")
	private String transmissionUid;

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
		
	public String getTransmissionUid() {
		return transmissionUid;
	}

	public void setTransmissionUid(String transmissionUid) {
		this.transmissionUid = transmissionUid;
	}

	public abstract String getDisplayAddress();
	
	public abstract boolean isUnread();
	
} 
