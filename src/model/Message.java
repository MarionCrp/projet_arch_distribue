package model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Message")
public class Message {

	private User recipient;
	private User sender;
	private 
	
	public Message() 
	{
		
	}
 
	public Message(User recipient, User sender) 
	{
		this.recipient = recipient;
		this.sender = sender;
	}

	public User getRecipient() {
		return recipient;
	}

	public void setRecipient(User recipient) {
		this.recipient = recipient;
	}

	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

}
