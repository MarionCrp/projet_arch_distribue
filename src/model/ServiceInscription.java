package model;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface ServiceInscription extends Remote{

	public void inscrire(User user) throws RemoteException;
}
