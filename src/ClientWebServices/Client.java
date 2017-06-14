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

public class Client {
	
	//activer les deux premiers import avec "Referenced Librairies" si utile (voir prof)
	//private static WebResource Tier2 = null;
	
	
	public static void main(String args[]) throws Exception 
	{
		
		String userLogin="";
		String userPassword="";
		
		//Connexion à Tier2 - service "controleur" de marion
		//serviceMeteo = Client.create().resource("http://localhost:8080/meteo");

		//comment récupérer le string d'un user en console ?
		//y mettre dans userResponse;
		
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
		System.out.println("Bienvenue DansTonChat ! Ici tout est permis, sauf être poli.\n-");
		System.out.println("Choisis une des deux options : (ça sert à rien de tester d'autres options, on a tout prévu ...)\n-");
		System.out.println("1 : Je suis un noob, j'ai besoin de m'inscrire.");
		System.out.println("2 : Pour qui tu me prends ? Je suis un vétéran ! Je veux me connecter.");
		System.out.println("3 : Finalement je suis timide, je veux quitter l'application.\n-");

		//saisie utilisateur
		while(!ok)
        {
			//On vérifie que l'utilisateur entre bien un integer
            try
            {
            	System.out.println("Mon choix : ");
            	userResponse=scanner.nextInt();
                 
            	//On vérifie que l'integer est compris entre les options possibles
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
		
		//On redirige vers la bonne fonction selon la réponse
		switch (userResponse) {
        case 1: 
        	form_1_SignUp();
             break;
        case 2:
        	form_2_SignIn(userLogin, userPassword);
        	break;
        case 3:
        	System.out.println("L'application s'est correctement arrêtée, à bientôt DansTonChat !");
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
		System.out.println("Pour s'inscrire, merci de choisir un nom d'utilisateur entre 4 et 20 caractères. ");
		
		while(!ok)
        {
			//On vérifie que la taille du login est correcte
            	System.out.println("Mon login : ");
            	userLogin = scanner.next();
                 
            	//On vérifie que l'integer est compris entre les options possibles
                if(userLogin.length() < 4 || userLogin.length() > 20)
                {
                    System.out.println("La taille du nom d'utilisateur est incorrecte.");
                }
                else
                {
                	ok = true;
                }
        }
		
		System.out.println("Merci de choisir un mot de passe entre 6 et 30 caractères. ");
		
		while(!ok)
        {
			//On vérifie que la taille du mdp est correcte
            	System.out.println("Mon mot de passe : ");
            	userPassword = scanner.next();
                 
            	//On vérifie que l'integer est compris entre les options possibles
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
		System.out.println("Connexion automatique au tchat dans 10 secondes.");
		//pause de 5 secondes le temps de les noter puis connexion automatique
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {}
		
		form_2_SignIn(userLogin, userPassword);
		
	}
	
	//Connexion
	public static void form_2_SignIn(String userLogin, String userPassword){
		//Si le login/mdp sont valides, on connecte l'utilisateur directement
		//FONCTION DE CONNEXION
		//si connexion réussie, go menu 2
		menu_2_MenuPrincipal();
		
		//Si login et password nuls, on les demande à l'user
		if(userLogin=="" || userPassword==""){
			
		}
		
		
	}
	
	
	
	//Menu principal, une fois connecté
	public static void menu_2_MenuPrincipal(){
		
	}
	
}
