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

import model.ServiceInscription;
import model.User;
import model.UserDAO;

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
	         ServiceInscription service_inscription = (ServiceInscription) Naming.lookup("rmi://localhost:2000/Messagerie");
	         
	         // TODO : ELODIE :: Me trouver mon User :p !
//	         User user = new User(service_inscription);
//	         Thread thread_user = new Thread(user);
	         
	         User searched_user = (User) UserDAO.find_user(login);
	         if(searched_user != null){
	 			return "Un utilisateur existe déjà avec ce login";
	 		} else {
	 			// Elodie : Ajout de l'utilisateur dans le txt.
//	 			new_user = tier_3.add_new_user(login, password);
//	 	        if(new_user){
//	 	      	 	return "Votre inscription a bien été prise en compte";
//	 	        } else {
//	 				return "Une erreur est survenue lors de l'inscription";
//	 		    }
	 			return "Votre inscription a bien été prise en compte";
	 		}
		} catch (Exception e){
			System.out.println(e);
			return "Une erreur est survenue";
		}
	}
	
	@GET
	//@Resource WebServiceContext wsContext;
	@Path("connexion/{login}/{password}")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON } )
	public String connexion (@PathParam("login") String login, @PathParam("password") String password)
	{
		// Find the HttpSession
		// Vérification des données en base
        // Appel RMI
		// localhost chez nous sinon ip du serveur. Messagerie = Interface de la bdd
//		try {
//	         Tier3 tier_3 = (Tier3) Naming.lookup("rmi://localhost:2000/Messagerie");
//		} catch (Exception e){
//
//		}
		User searched_user = UserDAO.find_user(login);
		if(searched_user == null){
			return "Cet utilisateur n'existe pas";
		} else {
	        if(searched_user.getPassword().equals(password)){
	        	return "Connecté avec succès";
	        } else {
	        	// TODO Envoyer message : "Connexion impossible";
	        	return "Le mot de passe est incorrect";
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
