package messagerie;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Date;
import java.util.HashMap;
import java.util.TreeMap;

import model.User;
import model.Users;


public interface Tier3 extends Remote{
	
	// USERS 
	public User find_user(String login) throws RemoteException;
	
	public boolean add_new_user(String login, String password) throws RemoteException;
	
	// CONNEXIONS
	public Users friends_list(String current_user_login) throws RemoteException;

	public boolean addFriend(String current_user_login, String friend_login) throws RemoteException;
	
	public boolean acceptFriend(String current_user_login, String friend_login) throws RemoteException;
	
	public Users users_list(String current_user_login) throws RemoteException;

	public Users users_from_connexion(String current_user_login) throws RemoteException;

	public Users friend_requests_list(String current_user_login) throws RemoteException;
	
	// MESSAGES
	public boolean sendMessage(String current_user_login, String friend_login, String content) throws RemoteException;

	public TreeMap<Date, String> getLastTenMessages(String userLogin, String friendLogin)throws RemoteException;
}
