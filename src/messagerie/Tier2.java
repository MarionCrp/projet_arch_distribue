package messagerie;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.HashMap;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBElement;

import model.Conversation;
import model.Message;
import model.Tier3Impl;
import model.User;
import model.Users;


@Path("/")
public class Tier2 {
	private static User current_user;
	//private static Users users;

	@GET
	@Path("inscription/{login}/{password}")
	@Produces("text/plain")
	public String inscription (@PathParam("login") String login, @PathParam("password") String password)
	{
		// Vérification des données en base
        // Appel RMI
		// localhost chez nous sinon ip du serveur. Messagerie = Interface de la bdd
		try {
			
	         Tier3 tier_3 = (Tier3) Naming.lookup("rmi://localhost:2000/Tier3");
	         
	         User searched_user = tier_3.find_user(login);

	         
	         if(searched_user != null){
	        	 return "false";
	 		} else {
	 			// Ajout de l'utilisateur dans le txt.
	 	        if(tier_3.add_new_user(login, password)){
	 	        	return "true";
	 	        } else {
	 	        	return "false";
	 		    }
	 		}
		} catch (Exception e){
			System.out.println(e);
			return "false";
		}
	}
	
	@GET
	//@Resource WebServiceContext wsContext;
	@Path("connexion/{login}/{password}")
	@Produces( { "text/plain" } )
	public String connexion (@PathParam("login") String login, @PathParam("password") String password)
	{
		// Find the HttpSession
		// Vérification des données en base
        // Appel RMI
		// localhost chez nous sinon ip du serveur. Messagerie = Interface de la bdd
		try {
	        Tier3 tier_3 = (Tier3) Naming.lookup("rmi://localhost:2000/Tier3");
			User searched_user = tier_3.find_user(login);
			System.out.println(searched_user);
	 		if(searched_user == null){
	 			System.out.println("Cet utilisateur n'existe pas");
	 			return "false";
	 		} else {
	 	        if(searched_user.getPassword().equals(password)){
	 	        	System.out.println("Connecté avec succès");
	 	        	tier_3.sign_in(searched_user);
	 	        	return "true";
	 	        } else {
	 	        	System.out.println("Le mot de passe est incorrect");
	 	        	return "false";
	 		    }
	 		}
		} catch (Exception e){
			e.printStackTrace();
			return "false";
		}
		
	}
	
	// Affiche la liste des amis (des connexions dont l'état est "accepted")
	@GET
	@Path("friends_list/{current_user_login}")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON } )
	public Users friends_list (@PathParam("current_user_login") String current_user_login) 
	{
		try {
			Tier3 tier3 = (Tier3) Naming.lookup("rmi://localhost:2000/Tier3");
			Users users = tier3.friends_list(current_user_login);
			return users;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	// Affiche la liste des utilisateurs (aucune connexion envoyée/ demandée ou annulée).
	@GET
	@Path("users/{current_user_login}")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON } )
	public Users users_list (@PathParam("current_user_login") String current_user_login) 
	{
		try {
			Tier3 tier3 = (Tier3) Naming.lookup("rmi://localhost:2000/Tier3");
			Users users = tier3.users_list(current_user_login);
			return users;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	// Envoie d'une demande de connexion à un utilisateur
	@GET
	@Path("addFriend/{current_user_login}/{friendLogin}")
	@Produces("text/plain")
	public String addFriend(@PathParam("current_user_login") String current_user_login, @PathParam("friendLogin") String friendLogin) {
		try {
			Tier3 tier3 = (Tier3) Naming.lookup("rmi://localhost:2000/Tier3");
			return String.valueOf(tier3.addFriend(current_user_login, friendLogin));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "false";
		}
	}
	
	// Liste des demandes de connexion
	@GET
	@Path("friend_requests_list/{current_user_login}")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON } )
	public Users friend_requests_list (@PathParam("current_user_login") String current_user_login) 
	{
		try {
			Tier3 tier3 = (Tier3) Naming.lookup("rmi://localhost:2000/Tier3");
			Users users = tier3.friend_requests_list(current_user_login);
			return users;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}
	
	
	// Validation d'une demande de connexion
	@GET
	@Path("acceptFriendRequest/{current_user_login}/{friendLogin}")
	@Produces("text/plain")
	public String acceptFriendRequest(@PathParam("current_user_login") String current_user_login, @PathParam("friendLogin") String friendLogin) {
		try {
			Tier3 tier3 = (Tier3) Naming.lookup("rmi://localhost:2000/Tier3");
			boolean has_been_accepted = tier3.acceptFriend(current_user_login, friendLogin);
			return String.valueOf(has_been_accepted);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "false";
		}
	}
	
	// Envoie d'un message à un utilisateur
	@GET
	@Path("sendMessage/{current_user_login}/{friendLogin}/{content}")
	@Produces("text/plain")
	public String sendMessage(@PathParam("current_user_login") String current_user_login, @PathParam("friendLogin") String friend_login, @PathParam("content") String content){
		try {
			Tier3 tier3 = (Tier3) Naming.lookup("rmi://localhost:2000/Tier3");
			if (! content.equals("QUITTER")){
				boolean has_been_send = tier3.sendMessage(current_user_login, friend_login, content);
				if (has_been_send){
					return "envoyé";
				}
			}
			else {
				return "quitter";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	// Affichage des 10 derniers messages d'une relation entre deux utilisateurs
	@GET
	@Path("lastTenMessages/{current_user_login}/{friendLogin}")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON } )
	public Conversation lastTenMessages(@PathParam("current_user_login") String userLogin, @PathParam("friendLogin") String friendLogin){
		Conversation last_ten_messages = new Conversation();
				
		try {
			Tier3 tier3 = (Tier3) Naming.lookup("rmi://localhost:2000/Tier3");
			last_ten_messages = tier3.getLastTenMessages(userLogin, friendLogin);

		} catch (Exception e) {
			e.printStackTrace();
		}		
		return last_ten_messages;
	}
	
	// Le dernier message d'une conversation entre deux utilisateurs (utilisé dans le messageListener fait par Kévin).
	@GET
	@Path("lastMessage/{current_user_login}/{friendLogin}")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON } )
	public Message lastMessage(@PathParam("current_user_login") String current_user_login, @PathParam("friendLogin") String friendLogin){
		Conversation conv = lastTenMessages(current_user_login, friendLogin);
		
		return conv.getLastMessage();
	}
	
	// Déconnexion
	@GET
	@Path("sign_out/{current_user_login}/{friendLogin}")
	@Produces("text/plain")
	public String sign_out(@PathParam("current_user") String current_user){
		try {
			Tier3 tier3 = (Tier3) Naming.lookup("rmi://localhost:2000/Tier3");
			User user = tier3.find_user(current_user);
			if(user != null){
				tier3.sign_out(user);
				return "L'application s'est correctement arrêtée, à bientôt DansTonChat !";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "Une erreur est survenue";
	}

}
