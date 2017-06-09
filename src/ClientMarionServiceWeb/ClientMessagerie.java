package ClientMarionServiceWeb;

import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import model.User;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class ClientMessagerie {
	private static WebResource serviceMessagerie = null;

	private static String inscription(String login, String password) throws Exception 
	{
		return serviceMessagerie.path("inscription/" + login + "/" + password).get(String.class);
	}

	private static void submit_subscription() throws Exception 
	{
		String reponse;
//		StringBuffer xmlStr;
//		JAXBContext context;       
//		JAXBElement<User> root;       
//		Unmarshaller unmarshaller;

		/*
		 ** Instanciation du convertiseur XML => Objet Java
		 */
//		context = JAXBContext.newInstance(User.class); 
//		unmarshaller = context.createUnmarshaller();
		
		reponse = serviceMessagerie.path("inscription/" + "Marion" + "/" + "password").get(String.class);    

		/*
		 ** Création du user (normalement récupéré par Scan)
		 */
		System.out.println(reponse);
	}

	public static void main(String args[]) throws Exception 
	{
		User new_user;
		serviceMessagerie = Client.create().resource("http://localhost:8080/Messagerie");

		submit_subscription();
	}    
}
