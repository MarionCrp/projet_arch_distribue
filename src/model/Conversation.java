package model;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Conversation")
public class Conversation {

	private User user_1;
	private User user_2;
	private ArrayList<Message> messages = new ArrayList<Message>();
 
	public Conversation() 
	{
		
	}
 
	public Conversation(User user_1, User user_2) 
	{
		this.user_1 = user_1;
		this.user_2 = user_2;
	}

	public User getUser1() {
		return user_1;
	}
	
	public User getUser2() {
		return user_2;
	}

	public void setUser1(User user_1) {
		this.user_1 = user_1;
	}

	public void setUser2(User user_2) {
		this.user_2 = user_2;
	}
	
	public void addMessage(Message message){
		this.messages.add(message);
	}

}
