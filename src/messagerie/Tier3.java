package messagerie;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public interface Tier3 extends Remote{

	public void find_user(String login) throws RemoteException;
	
	public void inscrire() throws RemoteException;
	
	
}
