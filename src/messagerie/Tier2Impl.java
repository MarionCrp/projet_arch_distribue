package messagerie;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Random;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import model.Message;
import model.User;
import model.UserImpl;

public class Tier2Impl extends UnicastRemoteObject implements Tier2, Runnable
	{
		private static final long serialVersionUID = 1L;
		
		private ArrayList<User> online_users = new ArrayList();
		
		public Tier2Impl() throws RemoteException 
		{

		}

		@GET
		@Path("connexion/{login}/{password}")
		@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON } )
		public void connecter (@PathParam("login") String login, @PathParam("password") String password) throws RemoteException
		{
			synchronized(online_users)
			{
				User connecting_user;
				try {
					connecting_user = new UserImpl(this, login, password);
					if (!online_users.contains(connecting_user))
					{
						System.out.println("Un utilisateur s'est connecté.");
						online_users.add(connecting_user);
					}
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		@Override
		public void deconnecter(User user) throws RemoteException {
			synchronized(online_users)
			{
				System.out.println("Un utilisateur s'est déconnecté.");
				online_users.remove(user);
			}
		}
		
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
//	         User user = tier_3.search_user(login);
//			if(user){
//				return "Un utilisateur existe déjà avec ce login";
//			} else {
//				new_user = tier_3.add_new_user(login, password);
//		        if(new_user){
//		      	 	return "Votre inscription a bien été prise en compte";
//		        } else {
//					return "Une erreur est survenue lors de l'inscription";
//			    }
//			}

			return "Toto";
		}
		
//		@GET
//		@Path("connexion/{login}/{password}")
//		@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON } )
//		public void connexion (@PathParam("login") String login, @PathParam("password") String password)
//		{
//			// Find the HttpSession
//			// Vérification des données en base
//	        // Appel RMI
//			// localhost chez nous sinon ip du serveur. Messagerie = Interface de la bdd
//			try {
//		         Tier3 tier_3 = (Tier3) Naming.lookup("rmi://localhost:2000/Messagerie");
//			} catch (Exception e){
//
//			}
//			//User current_user = tier_3.search_user(login);
//			// On utilise cette méthode en attendant de pouvoir récupérer un utilisateur dans la base
//			//User searched_user = new User("marion", "password");
//			if(searched_user == null){
//				// TODO Envoyer message : "Cet utilisateur n'existe pas";
//			} else {
//		        if(searched_user.getPassword().equals(password)){
//		        	// TODO Envoyer message : "Connecté avec succès";
//		        	online_users.add(searched_user);
//		        } else {
//		        	// TODO Envoyer message : "Connexion impossible";
//			    }
//			}
//		}
		
		
		public void run()
		{
			boolean fin = false;
			while (!fin)
			{
				try 
				{
					Message new_message = new Message(new UserImpl(this, "Toto1", "password"), new UserImpl(this, "Toto2","password"), "Message 1");
					
					synchronized (online_users)
					{
						System.out.println("Nouveau message : " + new_message.getContent());
						for (User user : online_users)
						{
							user.envoyer_new_message(new_message);
						}
					}

					Thread.sleep(new Random().nextInt(7000) + 3000);
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
		}

}
