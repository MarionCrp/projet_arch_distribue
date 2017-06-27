package ClientWebServices;

//import com.sun.jersey.api.client.Client;
//import com.sun.jersey.api.client.WebResource;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.transform.stream.StreamSource;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

import java.io.StringReader;

import messagerie.Tier2;
import model.Conversation;
import model.Message;
//import model.Message;
import model.User;
import model.Users;
import ClientWebServices.messageListener;



//import model.Users;
import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

public class ClientWebService {

	// private static WebResource tier2 = null;
	private static WebResource tier2 = null;
	private static User current_user = null;

	public static void main(String args[]) throws Exception {

		// Connexion à Tier2 - service "controleur" de marion
		tier2 = Client.create().resource("http://localhost:8080/Messagerie"); // new Tier2(); // 

		// Lancement de l'application
		menu_1_SignUp_Or_SignIn();
	}

	public static void connexion() {
		// Connexion à Tier2 - service "controleur" de marion
		// tier2 = Client.create().resource("http://localhost:8080/Messagerie");
	}

	// Choix entre se connecter et s'inscrire
	public static void menu_1_SignUp_Or_SignIn() {
		Scanner scanner = new Scanner(System.in);
		int userResponse = -1;
		String userLogin = "";
		String userPassword = "";
		boolean ok = false;

		System.out
				.println("\n\n------------------------------ MENU CONNEXION / INSCRIPTION ------------------------------");
		System.out
				.println("Bienvenue DansTonChat ! Ici tout est permis, sauf être poli.\n-");
		System.out.println("Choisissez une des trois options suivantes :\n-");
		System.out.println("1 : Je suis un noob, j'ai besoin de m'inscrire.");
		System.out
				.println("2 : Pour qui tu me prends ? Je suis un habitué du chat ! Je veux me connecter.");
		System.out
				.println("3 : Finalement je suis insociable et ma vie est inintéressante, je veux quitter l'application.\n-");

		// saisie utilisateur
		while (!ok) {
			// On vérifie que l'utilisateur entre bien un integer
			try {
				System.out.println("Mon choix : ");
				userResponse = scanner.nextInt();

				// On vérifie que l'integer est compris entre les options
				// possibles
				if (userResponse < 1 || userResponse > 3) {
					System.out.println("Choix invalide!");
				} else {
					ok = true;
				}
			} catch (InputMismatchException e) {
				System.out
						.println("Option inexistante, merci de choisir entre 1, 2 ou 3.");

				scanner.nextLine();
			}
		}

		// On redirige vers la bonne fonction selon la réponse
		switch (userResponse) {
		case 1:
			form_1_SignUp();
			break;
		case 2:
			form_2_SignIn(userLogin, userPassword);
			break;
		case 3:
			System.out
					.println("L'application s'est correctement arrêtée, à bientôt DansTonChat !");
			System.exit(0);
		}
	}

	// Inscription
	public static void form_1_SignUp() {
		Scanner scanner = new Scanner(System.in);
		String userLogin = "";
		String userPassword = "";
		boolean ok1 = false;
		boolean ok2 = false;

		System.out
				.println("\n\n------------------------------ FORMULAIRE D'INSCRIPTION ------------------------------");
		System.out
				.println("Pour s'inscrire, merci de choisir un nom d'utilisateur entre 4 et 20 caractères. ");

		while (!ok1) {
			// On vérifie que la taille du login est correcte
			System.out.println("Mon nom d'utilisateur : ");
			userLogin = scanner.next();

			// On vérifie que l'integer est compris entre les options possibles
			if (userLogin.length() < 4 || userLogin.length() > 20) {
				System.out
						.println("La taille du nom d'utilisateur est incorrecte.");
			} else {
				ok1 = true;
			}
		}

		System.out
				.println("Merci de choisir un mot de passe entre 6 et 30 caractères. ");

		while (!ok2) {
			// On vérifie que la taille du mdp est correcte
			System.out.println("Mon mot de passe : ");
			userPassword = scanner.next();

			// On vérifie que l'integer est compris entre les options possibles
			if (userPassword.length() < 6 || userPassword.length() > 30) {
				System.out.println("La taille du mot de passe est incorrecte.");
			} else {
				ok2 = true;
			}
		}

		String reponse;
		StringBuffer xmlStr;
		JAXBContext context;
		JAXBElement<User> root;
		Unmarshaller unmarshaller;

		reponse = tier2.path("inscription/" + userLogin + "/" + userPassword).get(String.class); //tier2.inscription(userLogin, userPassword); 
		Tier2 tier2 = new Tier2();
		if (Boolean.parseBoolean(reponse)) {
			System.out.println("Inscripton réussie avec succès ! ");
			System.out.println("Voici vos identifiants : ");
			System.out.println("Nom d'utilisateur : " + userLogin);
			System.out.println("Mot de passe : " + userPassword);
			System.out
					.println("Notez les bien, nous ne vous les redonnerons pas.");
			System.out
					.println("Connexion automatique au chat dans quelques secondes ...");

			// pause de 5 secondes le temps de les noter puis connexion
			// automatique
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
			}

			form_2_SignIn(userLogin, userPassword);
		} else {
			System.out
					.println("Erreur lors de l'inscription, veuillez rééssayer ");
			form_1_SignUp();
		}

	}

	// Connexion
	public static void form_2_SignIn(String userLogin, String userPassword) {
		Scanner scanner = new Scanner(System.in);
		boolean ok1 = false;
		boolean ok2 = false;

		System.out
				.println("\n\n------------------------------ Interface de connexion ------------------------------");

		// Si le login/mdp sont valides, on connecte l'utilisateur directement
		if (userLogin == "" || userPassword == "") {

			// saisie user login
			System.out
					.println("Pour vous connecter, merci de saisir vos identifiants.");
			while (!ok1) {
				// On vérifie que la taille du login est correcte
				System.out.println("Nom d'utilisateur : ");
				userLogin = scanner.next();

				// On vérifie que l'integer est compris entre les options
				// possibles
				if (userLogin.length() < 4 || userLogin.length() > 20) {
					System.out
							.println("La taille du nom d'utilisateur entré n'est pas correcte. Veuillez réssayer.");
				} else {
					ok1 = true;
				}
			}

			// saisie user password
			while (!ok2) {
				// On vérifie que la taille du login est correcte
				System.out.println("Mot de passe : ");
				userPassword = scanner.next();

				// On vérifie que l'integer est compris entre les options
				// possibles
				if (userPassword.length() < 6 || userPassword.length() > 30) {
					System.out
							.println("La taille du mot de passe entré n'est pas correcte. Veuillez réssayer.");
				} else {
					ok2 = true;
				}
			}

		}

		// Zone de connexion
		System.out.println("Connexion en cours ...");
		try {
			String reponse;
			StringBuffer xmlStr;
			JAXBContext context;
			JAXBElement<User> root;
			Unmarshaller unmarshaller;

			System.out.println("Toto sign in");
			
			reponse = tier2.path("connexion/" + userLogin + "/" + userPassword).get(String.class);
			//Tier2 tier2bis = new Tier2();
			//reponse = tier2bis.connexion(userLogin, userPassword);
			System.out.println(tier2.path("connexion/" + userLogin + "/" + userPassword).get(String.class));
			if (Boolean.parseBoolean(reponse)) {
				System.out.println("Connexion réussie");
				current_user = new User(userLogin, userPassword);
				menu_2_MenuPrincipal(userLogin);
			} else {
				System.out.println("La connexion a échoué");
				menu_1_SignUp_Or_SignIn();
			}

		} catch (Exception e) {
			System.out
					.println("Erreur de connexion - CODE 666. Application arrêtée.");
			System.exit(0);
		}
	}

	// Menu principal, une fois connecté
	public static void menu_2_MenuPrincipal(String userLogin) {

		Scanner scanner = new Scanner(System.in);
		int userResponse = -1;
		boolean ok = false;

		System.out
				.println("\n\n------------------------------ MENU PRINCIPAL  ------------------------------");
		System.out.println("Bienvenue " + userLogin + " !");
		System.out.println("Choisissez une des quatre options suivantes :\n-");
		System.out.println("0 : Quitter l'application.\n-");
		System.out.println("1 : Choisir un ami et chatter avec."); // BONUS - On
																	// peut
																	// préciser
																	// le nombre
																	// d'amis
		System.out.println("2 : Ajouter un ami.");
		System.out.println("3 : Accepter une demande d'amitié.\n-"); // BONUS -
																		// On
																		// peut
																		// préciser
																		// le
																		// nombre
																		// de
																		// demandes
																		// en
																		// cours

		while (!ok) {
			// On vérifie que l'utilisateur entre bien un integer
			try {
				System.out.println("Mon choix : ");
				userResponse = scanner.nextInt();

				// On vérifie que l'integer est compris entre les options
				// possibles
				if (userResponse < 0 || userResponse > 3) {
					System.out.println("Choix invalide!");
				} else {
					ok = true;
				}
			} catch (InputMismatchException e) {
				System.out
						.println("Option inexistante, merci de choisir entre 0, 1, 2 ou 3.");
				scanner.nextLine();
			}
		}

		// On redirige vers la bonne fonction selon la réponse
		switch (userResponse) {
		case 0:
			System.out
					.println("L'application s'est correctement arrêtée, à bientôt DansTonChat !");
			System.exit(0);
		case 1:
			menu_3_FriendsList(userLogin);
			break;
		case 2:
			form_3_AddFriend(userLogin);
			break;
		case 3:
			menu_4_AcceptFriendRequest(userLogin);
			break;
		}
	}

	// affichage de la liste d'amis / proposition de tchat avec eux
	public static void menu_3_FriendsList(String userLogin) {
		Scanner scanner = new Scanner(System.in);
		int userResponse = -1;
		boolean ok = false;

		System.out
				.println("\n\n------------------------------ MENU - LISTE D'AMIS  ------------------------------\n-");
		System.out.println("Choisissez une des options suivantes :\n-");
		System.out.println("0 : Quitter l'application.");
		System.out.println("1 : Revenir au menu principal.\n-");
		Map<Integer, String> friendsList = new HashMap<Integer, String>();

		ArrayList<String> friendsUserNames = new ArrayList<String>();
		Users friends = new Users();

		String reponse;
		StringBuffer xmlStr;
		JAXBContext context;
		JAXBElement<Users> root;
		Unmarshaller unmarshaller;

		/*
		 * * Instanciation du convertiseur XML => Objet Java
		 */
		try {

			// On récupère les amis
			context = JAXBContext.newInstance(Users.class);
			unmarshaller = context.createUnmarshaller();

			reponse = tier2.path("friends_list/" + current_user.getLogin()).get(String.class);

			/*
			 * * Traitement de la reponse XML : transformation en une instance
			 * de la classe Villes
			 */
			 xmlStr = new StringBuffer(reponse);
			 root = unmarshaller.unmarshal(new StreamSource(new
			 StringReader(xmlStr.toString())), Users.class);

			friends = root.getValue();
			//friends = reponse; // Version sans RMI / Serveur

			int optionNumber = 1;
			int listKey = -1;
			for (int i = 0; i < friends.liste.size(); i++) {
				optionNumber++;// pour l'affichage d'option
				listKey++;// clé du tableau associatif
				System.out.println(optionNumber + " : Chatter avec --- "
						+ friends.liste.get(i));
				friendsList.put(optionNumber, friends.liste.get(i));

			}
			// vérification de l'option
			while (!ok) {
				// On vérifie que l'utilisateur entre bien un integer
				try {
					System.out.println("Mon choix : ");
					userResponse = scanner.nextInt();

					// On vérifie que l'integer est compris entre les options
					// possibles
					if (userResponse < 0 || userResponse > optionNumber) {
						System.out.println("Choix invalide!");
					} else {
						ok = true;
					}
				} catch (InputMismatchException e) {
					System.out
							.println("Option inexistante, merci de choisir entre les options précédentes.");
					scanner.nextLine();
				}
			}

			if (userResponse == 0) {
				System.out
						.println("L'application s'est correctement arrêtée, à bientôt DansTonChat !");
				System.exit(0);
			} else if (userResponse == 1) {
				menu_2_MenuPrincipal(userLogin);
			} else {
				// on récupère le nom d'utilisateur dans le hasmap associatif
				// (peut-être des conditions test à faire ici)
				String friend = friendsList.get(userResponse);
				menu_5_TchatWithFriend(userLogin, friend);
			}
		} catch (JAXBException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	// formulaire d'ajout d'ami : Affiche tous les utilisateurs qui ne sont pas relié à une connexion avec le current_user
	public static void form_3_AddFriend(String userLogin) {
		Scanner scanner = new Scanner(System.in);
		String friendLogin = "";
		Users users;
		boolean ok1 = false;
		boolean ok2 = false;
		
		String reponse;
		StringBuffer xmlStr;
		JAXBContext context;
		JAXBElement<Users> root;
		Unmarshaller unmarshaller;

		//Users users = tier2.users_list(current_user.getLogin());
		reponse = tier2.path("users_list/" + current_user.getLogin()).get(String.class);
		
		 xmlStr = new StringBuffer(reponse);

		 try {
			context = JAXBContext.newInstance(Users.class);
			unmarshaller = context.createUnmarshaller();
			root = unmarshaller.unmarshal(new StreamSource(new
			StringReader(xmlStr.toString())), Users.class);
		
			 
			users = root.getValue();

			System.out
					.println("\n\n-------------------------------- Liste des utilisateurs ----------------------------------\n-");
			for (int i = 0; i < users.liste.size(); i++) {
				System.out.println(users.liste.get(i) + "\n");
			}

			System.out
					.println("\n\n------------------------------ FORMULAIRE - AJOUTER UN AMI  ------------------------------\n-");
			System.out
					.println("Pour ajouter un utilisateur en tant qu'ami, merci de saisir son nom d'utilisateur.");

			while (!ok2) {
				while (!ok1) {
					// On vérifie que la taille du login est correcte
					System.out.println("Ajouter le nom d'utilisateur : ");
					System.out.println("Ou '0' pour retour au menu ");
					friendLogin = scanner.next();
					if(friendLogin.equals("0")){
						menu_2_MenuPrincipal(userLogin);
						break;
					}

					// On vérifie que l'utilisateur fait bien parti de la liste
					else if (!users.liste.contains(friendLogin)) {
						System.out
								.println("L'utilisateur n'est pas dans la liste !");
					} else {
						ok1 = true;
					}
				}

				try {
					reponse = tier2.path("addFriend/" + current_user.getLogin() + "/" + friendLogin).get(String.class);
					if (Boolean.parseBoolean(reponse)) {
						System.out.println("Demande d'ami envoyé à " + friendLogin);
					} else {
						System.out
								.println("Erreur lors de l'envoi de demande de connexion");
					}

					// si connexion réussie, go menu 2
					menu_2_MenuPrincipal(userLogin);
					ok2 = true;
				} catch (Exception e) {
					System.out
							.println("Utilisateur inexistant, veuillez réssayer.\n-");
					form_3_AddFriend(userLogin);
				}
			}
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	// formulaire d'acceptation de demandes d'ajout ami
	public static void menu_4_AcceptFriendRequest(String userLogin) {

		Scanner scanner = new Scanner(System.in);
		int userResponse = -1;
		boolean ok = false;
		System.out
				.println("\n\n------------------------------ MENU - DEMANDES D'AMITIÉ  ------------------------------\n-");
		System.out
				.println("Pour accepter une demande d'ajout en liste d'amis, merci de rentrer le numéro corresponsant à la demande.\n-");
		System.out.println("0 : Quitter l'application.");
		System.out.println("1 : Revenir au menu principal.\n-");

		int optionNumber = 1;
		int listKey = -1;
		Map<Integer, String> requestList = new HashMap<Integer, String>();
		Users friend_requests = new Users();

		String reponse; // String reponse;
		StringBuffer xmlStr;
		JAXBContext context;
		JAXBElement<Users> root;
		Unmarshaller unmarshaller;

		/*
		 * * Instanciation du convertiseur XML => Objet Java
		 */
		try {

			// On récupère les amis
			context = JAXBContext.newInstance(Users.class);
			unmarshaller = context.createUnmarshaller();

			reponse = tier2.path("friend_requests/" + current_user.getLogin()).get(String.class);

			/*
			 * * Traitement de la reponse XML : transformation en une instance
			 * de la classe User
			 */
			 xmlStr = new StringBuffer(reponse);
			 root = unmarshaller.unmarshal(new StreamSource(new
			 StringReader(xmlStr.toString())), Users.class);

			 friend_requests = root.getValue();
			 
				if(friend_requests.liste.isEmpty()){
					System.out.println("PERSONNE NE VEUT ETRE TON AMI !");
				}
			//friend_requests = reponse; // Version sans RMI / Serveur

			for (int i = 0; i < friend_requests.liste.size(); i++) {
				optionNumber++;// pour l'affichage d'option
				listKey++;// clé du tableau associatif
				System.out.println(optionNumber + " : Accepter la demande de --- "
						+ friend_requests.liste.get(i));
				requestList.put(optionNumber, friend_requests.liste.get(i));
			}
		// vérification de l'option
		while (!ok) {
			// On vérifie que l'utilisateur entre bien un integer
			try {
				System.out.println("Mon choix : ");
				userResponse = scanner.nextInt();

				// On vérifie que l'integer est compris entre les options
				// possibles
				if (userResponse < 0 || userResponse > optionNumber) {
					System.out.println("Choix invalide!");
				} else {
					ok = true;
				}
			} catch (InputMismatchException e) {
				System.out
						.println("Option inexistante, merci de choisir entre les options précédentes.");
				scanner.nextLine();
			}
		}

		if (userResponse == 0) {
			System.out
					.println("L'application s'est correctement arrêtée, à bientôt DansTonChat !");
			System.exit(0);
		} else if (userResponse == 1) {
			menu_2_MenuPrincipal(userLogin);
		} else {
			// on récupère le nom d'utilisateur dans le hasmap associatif
			// (peut-être des conditions test à faire ici)
			String friend = requestList.get(userResponse);
			

			try {
				reponse = tier2.path("acceptFriendRequest/" + current_user.getLogin() + "/" + friend).get(String.class);
				//boolean is_accepted = tier2.acceptFriendRequest(current_user.getLogin(), friend);
				if(Boolean.parseBoolean(reponse)){
					System.out.println(friend
							+ " a été ajouté(e) à votre liste d'amis.");
					menu_2_MenuPrincipal(userLogin);
				} else {
					System.out.println("Une erreur est survenue lors de l'acceptation de la demande d'ami");
					menu_4_AcceptFriendRequest(current_user.getLogin());
				}

			} catch (Exception e) {
				System.out
						.println("Erreur - Demande d'ami - CODE 777. Application arrêtée.");
				System.exit(0);
			}
		}
		} catch(Exception e){
			System.out
			.println("Erreur - Demande d'ami formulaire - CODE 777. Application arrêtée.");
			System.exit(0);
		}
		

	}

	// affichage des derniers messages avec cet utilisateur + appel à la
	// fonction d'écriture/lecture des messages
	public static void menu_5_TchatWithFriend(String userLogin,
			String friendUserName) {
		System.out.println("\n\n------------------------------ CHAT AVEC "
				+ friendUserName + "  ------------------------------\n-");
		System.out
				.println("\n-----\n(Pour revenir au menu principal, entrez ''QUITTER'')");

		String lastMessage = "";
		Conversation last_messages;
		String reponse; // String reponse;
		StringBuffer xmlStr;
		JAXBContext context;
		JAXBElement<Conversation> root;
		Unmarshaller unmarshaller;

		// MARION METTRE LA FONCTION, il faut qu'elle affiche les 10 derniers
		// message et qu'elle retourne le dernier message en string
		// résultat du type
		// System.out.println(messageTime+" - "+authorUserName+" : "+lastMessageInBase);

		reponse = tier2.path("lastTenMessages/" + current_user.getLogin() + "/" + friendUserName).get(String.class);
		
		xmlStr = new StringBuffer(reponse);
	    try {
			
			context = JAXBContext.newInstance(Users.class);
			unmarshaller = context.createUnmarshaller();

			root = unmarshaller.unmarshal(new StreamSource(new
			StringReader(xmlStr.toString())), Conversation.class);
			
			last_messages = root.getValue();
			for(Message message : last_messages.getMessages()){
				System.out.println(message.getDatetime() + " - " + message.getSender().getLogin() + " : " + message.getContent());
			}

			form_4_SendMessage(userLogin, friendUserName, lastMessage);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	    
	}

	public static void form_4_SendMessage(String userLogin, String friendLogin,
			String lastMessage) {
		Scanner scanner = new Scanner(System.in);
		String userMessage = "";

		// création d'une instance du thread
		messageListener thread = new messageListener();
		thread.setUser1(userLogin);
		thread.setUser2(friendLogin);

		// on envoi le dernier message envoyé au thread
		if (lastMessage != "") {
			thread.setLastMessageGiven(lastMessage);
		}

		// Activation du thread
		thread.start();

		while (!userMessage.equals("QUITTER")){
			// On vérifie que la taille du login est correcte
			System.out.println("Votre message : ");
			userMessage = scanner.nextLine();
			
			String reponse; // String reponse;
			StringBuffer xmlStr;
			JAXBContext context;
			JAXBElement<Users> root;
			Unmarshaller unmarshaller;

			reponse = tier2.path("sendMessage/" + userLogin + "/" + friendLogin + "/" + userMessage).get(String.class);

			if(reponse.equals("envoyé")){
				System.out.println("Message envoyé");
			} else if(reponse.equals("quitter")){
				System.out.println("Retour au menu principal");
			}
			else {
				System.out.println("Une erreur est survenue, le message n'a pas été enregistré");
			}
		}
		
		if (userMessage.equals("QUITTER")) {
			thread.quitter();
			menu_2_MenuPrincipal(userLogin);
		}
	}
}
