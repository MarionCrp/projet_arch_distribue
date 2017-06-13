package ClientMarionServiceWeb;

import java.io.StringReader;



import java.util.Scanner;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import model.User;
import model.UserImpl;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class ClientMessagerie {
	private static WebResource serviceMessagerie = null;

	private static void inscription(String login, String password) throws Exception 
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
		reponse = serviceMessagerie.path("inscription/" + login + "/" + password).get(String.class);

		/*
		 ** Création du user (normalement récupéré par Scan)
		 */
		System.out.println(reponse);
	}
	
	private static User connecter(String login, String password) throws Exception {

		String reponse;
		StringBuffer xmlStr;
		JAXBContext context;       
		JAXBElement<UserImpl> root;       
		Unmarshaller unmarshaller;
		System.out.println(serviceMessagerie.path("connecter/" + login + "/" + password));
		reponse = serviceMessagerie.path("connecter/" + login + "/" + password).get(String.class);
		System.out.println(reponse);
		context = JAXBContext.newInstance(User.class); 
		unmarshaller = context.createUnmarshaller();
		
		xmlStr = new StringBuffer(reponse);
		root = unmarshaller.unmarshal(new StreamSource(new StringReader(xmlStr.toString())), UserImpl.class);

		return root.getValue();
	}

	public static void main(String args[]) throws Exception 
	{

		User current_user;
		serviceMessagerie = Client.create().resource("http://localhost:8080/Messagerie");
		
		Scanner stdin = new Scanner(System.in);
		boolean fin = false;
		while (!fin)
		{
			try
			{
				System.out.println("Que voulez vous ? (se connecter, s'inscrire, quitter)");
				System.out.print("> ");
				String demande = stdin.nextLine();
				if (demande.equals("se connecter"))
				{
					System.out.println("Login");
					System.out.print("> ");
					String login = stdin.nextLine();
					System.out.println("Password");
					System.out.print("> ");
					String password = stdin.nextLine();
					System.out.println("Connexion...");
					
					current_user = connecter(login, password);
					System.out.println(current_user);
					fin = true;
				} else if (demande.equals("s'inscrire")) {
					System.out.println("Login");
					System.out.print("> ");
					String login = stdin.nextLine();
					System.out.println("Password");
					System.out.print("> ");
					String password = stdin.nextLine();
					System.out.println("Inscription...");
					
					inscription(login, password);
					fin = true;
				} else {
					fin = true;
				}

			} catch (Exception e) {
				
			}
		}
		menu();
	}
	
	public static void menu(){

		
	}
}
