package model;

import javax.xml.bind.annotation.XmlRootElement;

import java.rmi.Remote;
import java.rmi.RemoteException;

@XmlRootElement(name = "User")
public interface User extends Remote {

	public void envoyer_new_message(Message mesage) throws RemoteException;
	
}
