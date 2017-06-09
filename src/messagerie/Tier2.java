package messagerie;

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
         Tier3 tier_3 = (Tier3) Naming.lookup("rmi://localhost:2000/Messagerie");
		
         tier_3.search_user(login);
         new_user = tier_3.add_new_user(login, password);
         if(new_user){
        	return true; 
         }
	} 

	@PUT
	@Path("donnerTemperature")
	@Consumes(MediaType.APPLICATION_XML)  
	@Produces("text/plain")
	public String donnerTemperature (JAXBElement<Ville> v) 
	{
		int temperature = 0;
		Ville ville = v.getValue();

		if (temperatures.containsKey(ville.getNom())) 
		{
			temperature = temperatures.get(ville.getNom());
		}

		return "" + temperature;
	}

	@GET
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON } )
	public Villes listerVilles () 
	{
		return villes;
	}

	static
	{
		villes = new Villes();
		villes.liste.add(new Ville("Grenoble",38));
		villes.liste.add(new Ville("Lyon_Best_Ville_Ever",69));
		villes.liste.add(new Ville("Lille",59));
		villes.liste.add(new Ville("Paris",75));
		villes.liste.add(new Ville("Brest",29));

		conditions = new HashMap<String,String>();
		conditions.put("Grenoble","soleil");
		conditions.put("Lille","pluie");
		conditions.put("Paris","nuages");
		conditions.put("Brest","vent");
		conditions.put("Lyon_Best_Ville_Ever","eclaircies");

		temperatures = new HashMap<String,Integer>();
		temperatures.put("Grenoble", 25);
		temperatures.put("Lille", 12);
		temperatures.put("Paris", 20);
		temperatures.put("Brest", 18);
		temperatures.put("Lyon_Best_Ville_Ever", 22);  
	} 
}
