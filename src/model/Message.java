package model;

import java.time.LocalDateTime;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Message")
public class Message {

	private UserImpl recipient;
	private UserImpl sender;
	private String content;
	private LocalDateTime datetime;

	public Message() 
	{
		
	}
 
	public Message(UserImpl recipient, UserImpl sender, String content) 
	{
		this.recipient = recipient;
		this.sender = sender;
		this.content = content;
		this.datetime = LocalDateTime.now();
	}

	public User getRecipient() {
		return recipient;
	}

	public void setRecipient(UserImpl recipient) {
		this.recipient = recipient;
	}

	public User getSender() {
		return sender;
	}

	public void setSender(UserImpl sender) {
		this.sender = sender;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public LocalDateTime getDatetime() {
		return datetime;
	}

	public void setDatetime(LocalDateTime datetime) {
		this.datetime = datetime;
	}
	
	

}
