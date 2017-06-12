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

public class Client {
	
	//activer les deux premiers import avec "Referenced Librairies" si utile (voir prof)
	//private static WebResource Tier2 = null;
	
	
	public static void main(String args[]) throws Exception 
	{
		String userResponse="empty";
		String userLogin="";
		String userPassword="";
		
		//Connexion à Tier2 - service "controleur" de marion
		//serviceMeteo = Client.create().resource("http://localhost:8080/meteo");

		//comment récupérer le string d'un user en console ?
		//y mettre dans userResponse;
		
	}
	
	//Choix entre se connecter et s'inscrire
	public String menu_1_SignInOrSignUp(){
		return "toto";
	}
	
	//Inscription
	public String form_1_SignUp(){
		return "toto";
	}
	
	//Connexion
	public String form_1_SignIn(String userLogin, String userPassword){
		return "toto";
	}
}
