package messagerie;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import model.ServiceInscriptionImpl;

public class Tier3 {

	public static void main (String arg[]) throws Exception{
		
		System.setProperty("java.rmi.server.hostname", "127.0.0.1");
		
		Registry registry = LocateRegistry.createRegistry(2000);
		
		ServiceInscriptionImpl serviceInscription = new ServiceInscriptionImpl();
		
		registry.bind("ServiceInscription", serviceInscription);
	}
}
