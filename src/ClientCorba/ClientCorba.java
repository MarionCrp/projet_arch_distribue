package ClientCorba;

import java.util.Properties;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

//import communication.ServiceCommunication;

public class ClientCorba {

	//private ServiceCommunication tier2 = null;
	
	public static void main(String[] args) throws Exception {
		connexion();
	}
	
	public static void connexion() {
		//Connexion à Tier2 - service "controleur" de marion
		Properties props = new Properties();
		props.put("org.omg.CORBA.ORBInitialPort", "2000");
		props.put("org.omg.CORBA.ORBInitialHost", "127.0.0.1");
		ORB requestBroker = ORB.init((String[]) null, props);
		
		//org.omg.CORBA.Object namingServiceRef = requestBroker.resolve_initial_references("NameService");
		//NamingContextExt namingContext = NamingContextExtHelper.narrow(namingServiceRef);

		//ServiceCommunication tier2 = ServiceCommunicationHelper.narrow(namingContext.resolve_str("ServiceCommunication"));
	}
}
