package ClientWebServices;

import java.util.Date;

import messagerie.Tier2;
import model.Message;

public class messageListener extends Thread{
	boolean ok=false;
	String lastMessageGiven="";
	String user1="";
	String user2="";
	
	
	public void run() {
		Message lastMessageInBase;
		String lastMessageInBaseContent;
		String authorUserName;
		Date messageTime;
		Tier2 tier2 = new Tier2();
		
		while(!ok){
			try {
		    	  
				if(user1!="" && user2!=""){
					//ON RECUPERE LE DERNIER MESSAGE EN BASE pour cette conversation
					//user1 et user2 te permettent d'avoir les noms d'utilisateur pour savoir dans quelle conversation regarder
					lastMessageInBase = tier2.lastMessage(user1, user2); //on récupère le dernier message de la conversation
					lastMessageInBaseContent = lastMessageInBase.getContent();
					messageTime = lastMessageInBase.getDatetime(); //on récupère le string de l'heure du dernier message
					authorUserName = lastMessageInBase.getSender().getLogin(); //nom de l'auteur du message

					if(lastMessageGiven!=""){
						if(!lastMessageGiven.equals(lastMessageInBaseContent)){
							
							System.out.println(messageTime + " - " + authorUserName + " : " + lastMessageInBaseContent);
							
							//on met à jour le dernier message pour le prochain message
							lastMessageGiven = lastMessageInBaseContent;
						}				
					}
				}
				
				// pause
		        Thread.sleep(1000);
		      }
		      catch (InterruptedException ex) {
		    	  ex.printStackTrace();
		      }
		} 
	}
	
	
	
	public void quitter(){
		this.ok=true;
	}

	public void setLastMessageGiven(String lastMessage){
		this.lastMessageGiven=lastMessage;
	}
	
	public void setUser1(String user1){
		this.user1=user1;
	}
	
	public void setUser2(String user2){
		this.user2=user2;
	}
}
