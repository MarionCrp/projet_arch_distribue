package messagerie;

import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.xml.bind.JAXBElement;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import model.User;
import model.UserImpl;

@Path("/")
public class Tier2 {
	private ArrayList<User> online_users = new ArrayList();

	@GET
	@Path("connecter/{login}/{password}")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON } )
	public UserImpl connecter (@PathParam("login") String login, @PathParam("password") String password)
	{
		synchronized(online_users)
		{
			UserImpl connecting_user;
			try {
				connecting_user = new UserImpl(this, login, password);
				if (!online_users.contains(connecting_user))
				{
					System.out.println("Un utilisateur s'est connecté.");
					online_users.add(connecting_user);
				}
				return connecting_user;
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
	}
	
	
}
