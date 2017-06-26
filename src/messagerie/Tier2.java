package messagerie;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.HashMap;

import javax.annotation.Resource;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBElement;

import model.Tier3Impl;
import model.User;
import model.Users;


@Path("/")
public class Tier2 {
	//private static final HashMap<String,String> conditions;

	//private static final HashMap<String,Integer> temperatures;

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
			
	         Tier3 tier_3 = new Tier3Impl(); //(Tier3) Naming.lookup("rmi://localhost:2000/Tier3");
	         
	         User searched_user = tier_3.find_user(login);

	         
	         if(searched_user != null){
	 			//return "Un utilisateur existe déjà avec ce login";
	        	 return "false";
	 		} else {
	 			// Elodie : Ajout de l'utilisateur dans le txt.
	 	        if(tier_3.add_new_user(login, password)){
	 	      	 	// return "Votre inscription a bien été prise en compte";
	 	        	return "true";
	 	        } else {
	 				//return "Une erreur est survenue lors de l'inscription";
	 	        	return "false";
	 		    }
	 		}
		} catch (Exception e){
			System.out.println(e);
			//return "Une erreur est survenue";
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
	        //Tier3 tier_3 = (Tier3) Naming.lookup("rmi://localhost:2000/Tier3");
	        Tier3 tier_3 = new Tier3Impl();
			User searched_user = tier_3.find_user(login);
	 		if(searched_user == null){
	 			//return "Cet utilisateur n'existe pas";
	 			return "false";
	 		} else {
	 	        if(searched_user.getPassword().equals(password)){
	 	        	//return "Connecté avec succès";
	 	        	return "true";
	 	        } else {
	 	        	// TODO Envoyer message : "Connexion impossible";
	 	        	//return "Le mot de passe est incorrect";
	 	        	return "false";
	 		    }
	 		}
		} catch (Exception e){
			//return "Une erreur est survenue";
			return "false";
		}
		
	}
	
	@GET
	@Path("friends_list/{current_user_login}")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON } )
	public Users friends_list (@PathParam("current_user_login") String current_user_login) 
	{
		Tier3 tier3;
		try {
			tier3 = new Tier3Impl();
			Users users = tier3.friends_list(current_user_login);
			return users;
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@GET
	@Path("users/{current_user_login}")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON } )
	public Users users_list (@PathParam("current_user_login") String current_user_login) 
	{
		Tier3 tier3;
		try {
			tier3 = new Tier3Impl();
			Users users = tier3.users_list(current_user_login);
			return users;
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	public boolean addFriend(String current_user_login, String friendLogin) {
		try {
			Tier3 tier3 = new Tier3Impl();
			return tier3.addFriend(current_user_login, friendLogin);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	@GET
	@Path("friend_requests_list/{current_user_login}")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON } )
	public Users friend_requests_list (@PathParam("current_user_login") String current_user_login) 
	{
		Tier3 tier3;
		try {
			tier3 = new Tier3Impl();
			Users users = tier3.friend_requests_list(current_user_login);
			return users;
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	public Boolean acceptFriendRequest(String current_user_login, String friendLogin) {
		try {
			Tier3 tier3 = new Tier3Impl();
			boolean has_been_accepted = tier3.acceptFriend(current_user_login, friendLogin);
			return has_been_accepted;
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	public Boolean sendMessage(String current_user_login, String friend_login, String content){
		try {
			Tier3 tier3 = new Tier3Impl();
			boolean has_been_send = tier3.sendMessage(current_user_login, friend_login, content);
			return has_been_send;
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

}
