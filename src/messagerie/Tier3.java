package messagerie;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import model.User;
import model.Users;


public interface Tier3 extends Remote{

	public User find_user(String login) throws RemoteException;
	
	public boolean add_new_user(String login, String password) throws RemoteException;
	
	public Users friends_list(String current_user_login) throws RemoteException;

	Users users_list(String current_user_login) throws RemoteException;

}
