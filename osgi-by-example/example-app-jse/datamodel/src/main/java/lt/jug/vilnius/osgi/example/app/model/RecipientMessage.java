package lt.jug.vilnius.osgi.example.app.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorValue(value="RECIPIENT")
public class RecipientMessage extends Message{
		
	@Enumerated(EnumType.STRING)
	@Column(name = "recipient_status")
	private RecipientStatus recipientStatus;
		
	public enum RecipientStatus {
		RECEIVED, READ, ACKNOWLEDGED 
	}

	public RecipientStatus getRecipientStatus() {
		return recipientStatus;
	}

	public void setReceipientStatus(RecipientStatus recipientStatus) {
		this.recipientStatus = recipientStatus;
	}

	@Override
	public String getDisplayAddress() {
		return this.getContent().getFrom();
	}
} 
