package model;

import java.util.Date;

public class Connexion {

	private int connexion_id;
	private String sender_login;
	private String recipient_login;
	private Date creating_date;
	private String state;
	
	private static int last_id;
	
	public Connexion(){
		
	}
	
	public int getConnexion_id() {
		return connexion_id;
	}
	public void setConnexion_id(int connexion_id) {
		this.connexion_id = connexion_id;
	}
	public String getSender_login() {
		return sender_login;
	}
	public void setSender_login(String sender_login) {
		this.sender_login = sender_login;
	}
	public String getRecipient_login() {
		return recipient_login;
	}
	public void setRecipient_login(String recipient_login) {
		this.recipient_login = recipient_login;
	}
	public Date getCreating_date() {
		return creating_date;
	}
	public void setCreating_date(Date creating_date) {
		this.creating_date = creating_date;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	public static int getLastId(){
		return last_id;
	}
	
	public static void incrementLastId(){
		last_id = last_id++;
	}
}
