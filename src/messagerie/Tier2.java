package messagerie;

import java.rmi.Naming;
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

import model.User;

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
	         Tier3 tier_3 = (Tier3) Naming.lookup("rmi://localhost:2000/Messagerie");
		} catch (Exception e){

		}
//         User user = tier_3.search_user(login);
//		if(user){
//			return "Un utilisateur existe déjà avec ce login";
//		} else {
//			new_user = tier_3.add_new_user(login, password);
//	        if(new_user){
//	      	 	return "Votre inscription a bien été prise en compte";
//	        } else {
//				return "Une erreur est survenue lors de l'inscription";
//		    }
//		}
		System.out.println("Toto printn in Tier2");
		return "Toto";
	}
	
	@GET
	//@Resource WebServiceContext wsContext;
	@Path("connexion/{login}/{password}")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON } )
	public User connexion (@PathParam("login") String login, @PathParam("password") String password)
	{
		// Find the HttpSession
		// Vérification des données en base
        // Appel RMI
		// localhost chez nous sinon ip du serveur. Messagerie = Interface de la bdd
		try {
	         Tier3 tier_3 = (Tier3) Naming.lookup("rmi://localhost:2000/Messagerie");
		} catch (Exception e){

		}
//         User current_user = tier_3.search_user(login);
		User searched_user = new User("marion", "password");
		if(searched_user == null){
			// TODO Envoyer message : "Cet utilisateur n'existe pas";
			return null;
		} else {
	        if(searched_user.getPassword().equals(password)){
	        	// TODO Envoyer message : "Connecté avec succès";
	        	return current_user;
	        } else {
	        	// TODO Envoyer message : "Connexion impossible";
	        	return null;
		    }
		}
	}

	@GET
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON } )
	public String coucou ()
	{
		return "coucou";
	}

}
