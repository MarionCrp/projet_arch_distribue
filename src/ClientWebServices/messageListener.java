package ClientWebServices;

public class messageListener extends Thread{
	boolean ok=false;
	String lastMessageGiven="";
	String user1="";
	String user2="";
	
	
	public void run() {
		String lastMessageInBase = "";
		
		while(!ok){
			try {
		    	  
				if(user1!="" && user2!=""){
					//MARION -- ON RECUPERE LE DERNIER MESSAGE EN BASE pour cette conversation
					//user1 et user2 te permettent d'avoir les noms d'utilisateur pour savoir dans quelle conversation regarder
					//messageTime= on récupère le string de l'heure du dernier message
					//authorUserName = nom de l'auteur du message
					//lastMessageInBase = on récupère le string du dernier message de la conversation
					
					if(lastMessageGiven!=""){
						if(lastMessageGiven!=lastMessageInBase){
							
							System.out.println(messageTime+" - "+authorUserName+" : "+lastMessageInBase);
							
							//on met à jour le dernier message pour le prochain message
							lastMessageGiven=lastMessageInBase;
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
