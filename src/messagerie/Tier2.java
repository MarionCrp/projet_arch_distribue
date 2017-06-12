package messagerie;

import java.rmi.Naming;
import java.rmi.Remote;
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

import model.User;
import model.UserImpl;

@Path("/")
public interface Tier2 extends Remote{
	
	public void deconnecter(User user) throws RemoteException;
	public String inscription(String login, String password) throws RemoteException;
	public void connecter(String login, String password) throws RemoteException;
	
//
//	@GET
//	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON } )
//	public String coucou ()
//	{
//		return "coucou";
//	}

}
