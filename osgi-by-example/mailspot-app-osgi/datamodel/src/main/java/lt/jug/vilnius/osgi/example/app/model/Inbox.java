package lt.jug.vilnius.osgi.example.app.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "inboxes")
public class Inbox {

	@Id
	@Column(name = "address", nullable = false)
	@Size(min=6)
	private String address;
	
	@Column(name = "status", nullable = false)
	@Enumerated(EnumType.STRING)
	private Status status;

	public enum Status {
		ACTIVE, INCATIVE
	}
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
}
