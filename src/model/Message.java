package model;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Message")
public class Message {

	private User recipient;
	private User sender;
	private String content;
	private Date datetime;

	public Message() 
	{
		
	}
 
	public Message(User recipient, User sender, String content, Date datetime) 
	{
		this.recipient = recipient;
		this.sender = sender;
		this.content = content;
		this.datetime = datetime;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getDatetime() {
		return datetime;
	}

	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}
	
	

}
