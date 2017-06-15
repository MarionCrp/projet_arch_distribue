package ClientWebServices;

//import com.sun.jersey.api.client.Client;
//import com.sun.jersey.api.client.WebResource;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import model.Message;
import model.User;
//import model.Users;
import java.util.Scanner; 
import java.util.InputMismatchException;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

public class Client {
	
	//activer les deux premiers import avec "Referenced Librairies" si utile (voir prof)
	//private static WebResource Tier2 = null;
	
	
	public static void main(String args[]) throws Exception 
	{
		//Connexion � Tier2 - service "controleur" de marion
		//serviceMeteo = Client.create().resource("http://localhost:8080/meteo");

		//Lancement de l'application
		menu_1_SignUp_Or_SignIn();	
	}
	
	
	
	//Choix entre se connecter et s'inscrire
	public static void menu_1_SignUp_Or_SignIn(){
		Scanner scanner = new Scanner (System.in);
		int userResponse=-1;
		String userLogin="";
		String userPassword="";
		boolean ok=false;
		
		System.out.println("\n\n------------------------------ MENU CONNEXION / INSCRIPTION ------------------------------");
		System.out.println("Bienvenue DansTonChat ! Ici tout est permis, sauf �tre poli.\n-");
		System.out.println("Choisissez une des trois options suivantes :\n-");
		System.out.println("1 : Je suis un noob, j'ai besoin de m'inscrire.");
		System.out.println("2 : Pour qui tu me prends ? Je suis un habitu� du chat ! Je veux me connecter.");
		System.out.println("3 : Finalement je suis insociable et ma vie est inint�ressante, je veux quitter l'application.\n-");
		
		//saisie utilisateur
		while(!ok)
        {
			//On v�rifie que l'utilisateur entre bien un integer
            try
            {
            	System.out.println("Mon choix : ");
            	userResponse=scanner.nextInt();
                 
            	//On v�rifie que l'integer est compris entre les options possibles
                if(userResponse < 1 || userResponse > 3)
                {
                    System.out.println("Choix invalide!");
                }
                else
                {
                	ok = true;
                }
            }
            catch(InputMismatchException e)
            {
                System.out.println("Option inexistante, merci de choisir entre 1, 2 ou 3.");
                
                scanner.nextLine();
            }
        }
		
		//On redirige vers la bonne fonction selon la r�ponse
		switch (userResponse) {
        case 1: 
        	form_1_SignUp();
             break;
        case 2:
        	form_2_SignIn(userLogin, userPassword);
        	break;
        case 3:
        	System.out.println("L'application s'est correctement arr�t�e, � bient�t DansTonChat !");
        	System.exit(0);
		}
	}
	
	//Inscription
	public static void form_1_SignUp(){
		Scanner scanner = new Scanner (System.in);
		String userLogin="";
		String userPassword="";
		boolean ok=false;
		
		System.out.println("\n\n------------------------------ FORMULAIRE D'INSCRIPTION ------------------------------");
		System.out.println("Pour s'inscrire, merci de choisir un nom d'utilisateur entre 4 et 20 caract�res. ");
		
		while(!ok)
        {
			//On v�rifie que la taille du login est correcte
			System.out.println("Mon nom d'utilisateur : ");
            	userLogin = scanner.next();
                 
            	//On v�rifie que l'integer est compris entre les options possibles
                if(userLogin.length() < 4 || userLogin.length() > 20)
                {
                    System.out.println("La taille du nom d'utilisateur est incorrecte.");
                }
                else
                {
                	ok = true;
                }
        }
		
		System.out.println("Merci de choisir un mot de passe entre 6 et 30 caract�res. ");
		
		while(!ok)
        {
			//On v�rifie que la taille du mdp est correcte
            	System.out.println("Mon mot de passe : ");
            	userPassword = scanner.next();
                 
            	//On v�rifie que l'integer est compris entre les options possibles
                if(userPassword.length() < 6 || userPassword.length() > 30)
                {
                    System.out.println("La taille du mot de passe est incorrecte.");
                }
                else
                {
                	ok = true;
                }
        }
		
		System.out.println("Voici vos identifiants : "+userLogin);
		System.out.println("Nom d'utilisateur : "+userLogin);
		System.out.println("Mot de passe : "+userPassword);
		System.out.println("Notez les bien, nous ne vous les redonnerons pas.");
		System.out.println("Connexion automatique au chat dans quelques secondes ...");

		//MARION - INSCRIPTION ICI

		//pause de 5 secondes le temps de les noter puis connexion automatique
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {}
		
		form_2_SignIn(userLogin, userPassword);
		
	}
	
	//Connexion
	public static void form_2_SignIn(String userLogin, String userPassword){
		Scanner scanner = new Scanner (System.in);
		boolean ok=false;
		
		System.out.println("\n\n------------------------------ Interface de connexion ------------------------------");
		
		//Si le login/mdp sont valides, on connecte l'utilisateur directement
		if(userLogin=="" || userPassword==""){

			//saisie user login
			System.out.println("Pour vous connecter, merci de saisir vos identifiants.");
			while(!ok)
	        {
				//On v�rifie que la taille du login est correcte
	            	System.out.println("Nom d'utilisateur : ");
	            	userLogin = scanner.next();
	                 
	            	//On v�rifie que l'integer est compris entre les options possibles
	                if(userLogin.length() < 4 || userLogin.length() > 20)
	                {
	                    System.out.println("La taille du nom d'utilisateur entr� n'est pas correcte. Veuillez r�ssayer.");
	                }
	                else
	                {
	                	ok = true;
	                }
	        }

	        //saisie user password
			while(!ok)
	        {
				//On v�rifie que la taille du login est correcte
	            	System.out.println("Mot de passe : ");
	            	userPassword = scanner.next();
	                 
	            	//On v�rifie que l'integer est compris entre les options possibles
	                if(userPassword.length() < 6 || userPassword.length() > 30)
	                {
	                    System.out.println("La taille du mot de passe entr� n'est pas correcte. Veuillez r�ssayer.");
	                }
	                else
	                {
	                	ok = true;
	                }
	        }

		}
		
		//Zone de connexion
		System.out.println("Connexion en cours ...");
		try{
			//MARION - CONNEXION ICI

			//si connexion r�ussie, go menu 2
			menu_2_MenuPrincipal(userLogin);
		}catch(Exception e){
			System.out.println("Erreur de connexion - CODE 666. Application arr�t�e.");
		}
	}
	
	
	
	//Menu principal, une fois connect�
	public static void menu_2_MenuPrincipal(String userLogin){
		
		Scanner scanner = new Scanner (System.in);
		int userResponse=-1;
		boolean ok=false;
		
		System.out.println("\n\n------------------------------ MENU PRINCIPAL  ------------------------------");
		System.out.println("Bienvenue "+userLogin+" !");
		System.out.println("Choisissez une des quatre options suivantes :\n-");
		System.out.println("0 : Quitter l'application.\n-");
		System.out.println("1 : Choisir un ami et chatter avec."); // BONUS - On peut pr�ciser le nombre d'amis
		System.out.println("2 : Ajouter un ami.");
		System.out.println("3 : Accepter une demande d'amiti�.\n-"); // BONUS - On peut pr�ciser le nombre de demandes en cours 

		while(!ok)
        {
			//On v�rifie que l'utilisateur entre bien un integer
            try
            {
            	System.out.println("Mon choix : ");
            	userResponse=scanner.nextInt();
                 
            	//On v�rifie que l'integer est compris entre les options possibles
                if(userResponse < 0 || userResponse > 3)
                {
                    System.out.println("Choix invalide!");
                }
                else
                {
                	ok = true;
                }
            }
            catch(InputMismatchException e)
            {
                System.out.println("Option inexistante, merci de choisir entre 0, 1, 2 ou 3.");
                scanner.nextLine();
            }
        }
		
		//On redirige vers la bonne fonction selon la r�ponse
		switch (userResponse) {
        case 0: 
        	System.out.println("L'application s'est correctement arr�t�e, � bient�t DansTonChat !");
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




	public static void menu_3_FriendsList(String userLogin){
		Scanner scanner = new Scanner (System.in);
		int userResponse=-1;
		boolean ok=false;
		
		System.out.println("\n\n------------------------------ MENU - LISTE D'AMIS  ------------------------------\n-");
		System.out.println("Choisissez une des options suivantes :\n-");
		System.out.println("0 : Quitter l'application.");
		System.out.println("1 : Revenir au menu principal.\n-");
		
		Map<Integer, String> friendsList = new HashMap<Integer, String>();
		
		ArrayList<String> friendsUserNames = new ArrayList<String>();
		//MARION - ON RECUPERE LES AMIS ICI
		//friends = 
		
		int optionNumber=1;
		int listKey=-1;
		for(String friendUserName: friendsUserNames){
			optionNumber++;//pour l'affichage d'option
			listKey++;//cl� du tableau associatif
			System.out.println(optionNumber+" : Chatter avec --- "+friendUserName);
			friendsList.put(listKey, friendUserName);
			
		}
		
		//v�rification de l'option
		while(!ok)
        {
			//On v�rifie que l'utilisateur entre bien un integer
            try
            {
            	System.out.println("Mon choix : ");
            	userResponse=scanner.nextInt();
                 
            	//On v�rifie que l'integer est compris entre les options possibles
                if(userResponse < 0 || userResponse > optionNumber)
                {
                    System.out.println("Choix invalide!");
                }
                else
                {
                	ok = true;
                }
            }
            catch(InputMismatchException e)
            {
                System.out.println("Option inexistante, merci de choisir entre les options pr�c�dentes.");
                scanner.nextLine();
            }
        }
		
		if(userResponse==0){
			System.out.println("L'application s'est correctement arr�t�e, � bient�t DansTonChat !");
        	System.exit(0);
		}else if(userResponse==1){
			menu_2_MenuPrincipal(userLogin);
		}else{
			//on r�cup�re le nom d'utilisateur dans le hasmap associatif
			//(peut-�tre des conditions test � faire ici)
			String friend = friendsList.get(listKey);
			menu_5_TchatWithFriend(userLogin, friend);
		}
	}

	
	
	public static void form_3_AddFriend(String userLogin){
		System.out.println("\n\n------------------------------ FORMULAIRE - AJOUTER UN AMI  ------------------------------\n-");

	}

	public static void menu_4_AcceptFriendRequest(String userLogin){
		System.out.println("\n\n------------------------------ MENU - DEMANDES D'AMITI�  ------------------------------\n-");

	}
	
	public static void menu_5_TchatWithFriend(String userLogin, String friendUserName){
		System.out.println("\n\n------------------------------ CHAT AVEC "+friendUserName+"  ------------------------------\n-");

	}

	public static void form_4_SendMessage(String userLogin, String friendLogin){
		

	}
	
}
