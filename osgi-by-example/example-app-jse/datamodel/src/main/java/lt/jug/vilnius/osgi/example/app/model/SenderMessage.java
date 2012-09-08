package lt.jug.vilnius.osgi.example.app.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

/**
 * @author arturas
 *
 */
@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorValue(value="SENDER")
public class SenderMessage extends Message {
	
	@Enumerated(EnumType.STRING)
	@Column(name = "sender_status", nullable = false)
	private SenderStatus senderStatus;
		
	public enum SenderStatus {
		DRAFT, SENT 
	}

	public SenderStatus getSenderStatus() {
		return senderStatus;
	}

	public void setSenderStatus(SenderStatus senderStatus) {
		this.senderStatus = senderStatus;
	}
} 
