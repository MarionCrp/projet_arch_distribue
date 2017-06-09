package messagerie;

import java.rmi.Naming;
import java.util.HashMap;

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

	//private static User current_user;
	//private static Users users;

	@GET
	@Path("inscription/{login}")
	@Produces("text/plain")
	//TODOOOOO :: Passer plusieurs paramètres.
	public String inscription (@PathParam("login") String login)
	{
		// Vérification des données en base
        // Appel RMI
		// localhost chez nous sinon ip du serveur. Messagerie = Interface de la bdd
		try {
	         Tier3 tier_3 = (Tier3) Naming.lookup("rmi://localhost:2000/Messagerie");
		} catch (Exception e){

		}
//         tier_3.search_user(login);
//         new_user = tier_3.add_new_user(login, password);
//         if(new_user){
//        	return true;
//         }
		 User new_user = new User(login,"pssword");
         return new_user.getLogin();
	}

	@GET
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON } )
	public String coucou ()
	{
		return "coucou";
	}

}
