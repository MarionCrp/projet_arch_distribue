package model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class ServiceInscriptionImpl extends UnicastRemoteObject implements ServiceInscription, Runnable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<User> listUsers = new ArrayList<User>();

	public ServiceInscriptionImpl() throws RemoteException {
		
	}

	public void inscrire(User user) throws RemoteException {
		
		if (!listUsers.contains(user)){
			listUsers.add(user);
			System.out.println("L'utilisateur " + user.getLogin() + " a bien été inscrit.");
		}
	}

	public void run() {
		
		ObjectInputStream fileIn = null;
		List<User> oldListUsers;
		ObjectOutputStream fileOut = null;
		try {
			fileIn = new ObjectInputStream(new FileInputStream("inscrits.txt"));
			oldListUsers = (List<User>) fileIn.readObject();
				for (User u : listUsers){
					if (!oldListUsers.contains(u)){
						oldListUsers.add(u);
					}
				}
			fileIn.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			fileOut = new ObjectOutputStream(new FileOutputStream("inscrits.txt"));
			fileOut.writeObject(listUsers);
			fileOut.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	
}
