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

@Path("/")
public class Tier2 {
	//private static final HashMap<String,String> conditions;
	
	//private static final HashMap<String,Integer> temperatures;
	
	//private static final Villes villes;
	
	//private static User current_user;
	//private static Users users;

	@GET
	@Path("inscription")
	@Produces("text/plain")
	public boolean inscription (@PathParam("login") String login, @PathParam("password") String password) 
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
         return true;
	}



}
