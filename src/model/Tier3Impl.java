package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.File;

import messagerie.Tier3;

public class Tier3Impl extends UnicastRemoteObject implements Tier3, Runnable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static Integer c_id = 0;
	
	//private List<User> listUsers = new ArrayList<User>();

	public Tier3Impl() throws RemoteException {
		
	}
	
	public static void main(String[] arg) {
		System.setProperty("java.rmi.server.hostname", "127.0.0.1");
		try {
			Registry registry = LocateRegistry.createRegistry(2000);
			Tier3Impl tier3 = new Tier3Impl();
			registry.bind("Tier3", tier3);
			Thread.sleep(99999);
		} catch (Exception e){
			e.printStackTrace();
		}
		
	}



	public void run() {
		
		ObjectInputStream fileIn = null;
		List<User> oldListUsers;
		ObjectOutputStream fileOut = null;
		/*try {
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
		}*/
	
	}

	@Override
	public boolean add_new_user(String login, String password) throws RemoteException {

		User existing_user = (User) this.find_user(login);
		if (existing_user == null){
			try {
				DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
				
				// Find or Create XML
				StreamResult result;
				File xml = new File("users.xml");
				Document doc;
				Element rootElement;
				
				if(xml.exists()){
					result = new StreamResult(xml);
					doc = docBuilder.parse(xml);
					rootElement = (Element) doc.getElementsByTagName("users").item(0);
				}
				else {
					doc = docBuilder.newDocument();
					result = new StreamResult(new File("users.xml"));
					// root elements
					rootElement = doc.createElement("users");
					doc.appendChild(rootElement);
				}
				
				Element user = doc.createElement("user");
				Element user_login = doc.createElement("login");
				Element user_password = doc.createElement("password");
				
				user_login.setTextContent(login);
				user_password.setTextContent(password);
				rootElement.appendChild(user);
				user.appendChild(user_login);
				user.appendChild(user_password);
					
				// write the content into xml file
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
				DOMSource source = new DOMSource(doc);
				
				transformer.transform(source, result);

				System.out.println("File saved!");

			  } catch (Exception e){
					e.printStackTrace();
				}
			
			return true;
		} else {
			System.out.println("L'utilisateur " + existing_user.getLogin() + " existe déjà.");
			return false;
		}
		
	}

	@Override
	public User find_user(String login) throws RemoteException {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			
			// Find or Create XML
			StreamResult result;
			File xml = new File("users.xml");
			Document doc;
			Element rootElement;
			
			if(xml.exists()){
				result = new StreamResult(xml);
				doc = docBuilder.parse(xml);
				rootElement = (Element) doc.getElementsByTagName("users").item(0);
				
				NodeList logins = doc.getElementsByTagName("login");
				for(int i = 0; i < logins.getLength(); i++){
					if(logins.item(i).getTextContent().equals(login)){						
						Element parent = (Element) logins.item(i).getParentNode();
						String password = parent.getElementsByTagName("password").item(0).getTextContent();
						User user = new User(login, password);
						return user;
					}
				}
				return null;
			}
		  } catch (Exception e){
				e.printStackTrace();
			}
		return null;
	}
	
	@Override
	// Users List = All users - friend - denied connexion.
	public Users users_list(String current_user_login) throws RemoteException {
		
		Users listUsers = new Users();
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		
		try {
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			
			// Find or Create XML
			StreamResult result;
			File user_xml = new File("users.xml");
			Document doc;
			Element rootElement;
			
			// On récupère toutes les relations déjà créée (quelque soit l'état), pour ne pas faire de doublon.
			Users already_connected_users = users_from_connexion(current_user_login);
			
			if(user_xml.exists()){
				result = new StreamResult(user_xml);
				doc = docBuilder.parse(user_xml);
				
				NodeList logins = doc.getElementsByTagName("login");
				for(int i = 0; i < logins.getLength(); i++){
					String user_login = logins.item(i).getTextContent();
					if(!user_login.equals(current_user_login)){
						if(already_connected_users == null){
							listUsers.addUser(logins.item(i).getTextContent());
						} else {
							if(!already_connected_users.liste.contains(user_login)){
								listUsers.addUser(logins.item(i).getTextContent());
							}
						}
					}
				}
				
				// TODO : Enlever les utilisateurs qui sont déjà connecté à l'utilisateur.

				return listUsers;
			}
		
		}catch (Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	public Users friends_list(String current_user_login) throws RemoteException {
		
		Users listUsers = new Users();
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		
		try {
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			
			// Find or Create XML
			StreamResult result;
			File xml = new File("connexions.xml");
			Document doc;
			Element rootElement;
			
			if(xml.exists()){
				result = new StreamResult(xml);
				doc = docBuilder.parse(xml);
				rootElement = (Element) doc.getElementsByTagName("connexions").item(0);
				
				NodeList connections_id = doc.getElementsByTagName("id");
				for(int i = 0; i < connections_id.getLength(); i++){
					Element parent = (Element) connections_id.item(i).getParentNode();
					if (parent.getElementsByTagName("sender_login").item(0).getTextContent().equals(current_user_login)){
						if(parent.getElementsByTagName("state").item(0).getTextContent().toLowerCase().equals("accepted")){
							listUsers.addUser(parent.getElementsByTagName("recipient_login").item(0).getTextContent());
						}
					}
					if (parent.getElementsByTagName("recipient_login").item(0).getTextContent().equals(current_user_login)){
						if(parent.getElementsByTagName("state").item(0).getTextContent().toLowerCase().equals("accepted")){
							listUsers.addUser(parent.getElementsByTagName("sender_login").item(0).getTextContent());
						}
					}
				}
				return listUsers;
			}
		
		} catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public Users friend_requests_list(String current_user_login) throws RemoteException {
		
		Users listUsers = new Users();
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		
		try {
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			
			// Find or Create XML
			StreamResult result;
			File xml = new File("connexions.xml");
			Document doc;
			Element rootElement;
			
			if(xml.exists()){
				result = new StreamResult(xml);
				doc = docBuilder.parse(xml);
				rootElement = (Element) doc.getElementsByTagName("connexions").item(0);
				
				NodeList connections_id = doc.getElementsByTagName("id");
				for(int i = 0; i < connections_id.getLength(); i++){
					Element parent = (Element) connections_id.item(i).getParentNode();
					if (parent.getElementsByTagName("recipient_login").item(0).getTextContent().equals(current_user_login)){
						if(parent.getElementsByTagName("state").item(0).getTextContent().toLowerCase().equals("pending")){
							listUsers.addUser(parent.getElementsByTagName("sender_login").item(0).getTextContent());
						}
					}
				}
				return listUsers;
			}
		
		} catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean addFriend(String user_login, String friend_login) throws RemoteException {

		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		try {
			docBuilder = docFactory.newDocumentBuilder();
			// Find or Create XML
			StreamResult result;
			File xml = new File("connexions.xml");
			Document doc;
			Element rootElement;
			
			int xml_last_id = 0;
			
			if(xml.exists()){
				result = new StreamResult(xml);
				doc = docBuilder.parse(xml);
				rootElement = (Element) doc.getElementsByTagName("connexions").item(0);
				NodeList id_nodes = doc.getElementsByTagName("id");
				if(id_nodes.getLength() > 0){
					xml_last_id = Integer.parseInt(id_nodes.item(id_nodes.getLength()-1).getTextContent());
					System.out.println(xml_last_id);
				}
			}
			else {
				doc = docBuilder.newDocument();
				result = new StreamResult(new File("connexions.xml"));
				// root elements
				rootElement = doc.createElement("connexions");
				doc.appendChild(rootElement);
			}
			
			Element connexion = doc.createElement("connexion");
			Element connexion_id = doc.createElement("id");
			Element sender_login = doc.createElement("sender_login");
			Element recipient_login = doc.createElement("recipient_login");
			Element state = doc.createElement("state");
			Element connexion_date = doc.createElement("date");
			
			connexion_id.setTextContent(String.valueOf(xml_last_id + 1));
			sender_login.setTextContent(user_login);
			recipient_login.setTextContent(friend_login);
			state.setTextContent("pending");
			connexion_date.setTextContent((new Date()).toString());
			rootElement.appendChild(connexion);
			
			connexion.appendChild(connexion_id);
			connexion.appendChild(sender_login);
			connexion.appendChild(recipient_login);
			connexion.appendChild(recipient_login);
			connexion.appendChild(state);
			connexion.appendChild(connexion_date);
			
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			DOMSource source = new DOMSource(doc);
			
			transformer.transform(source, result);

			System.out.println("File saved!");
			return true;
			
		} catch (Exception e){
			System.out.println("Erreur Tier 3 : Impossible d'ajouter l'utilisateur");
			e.printStackTrace();
		}
		return false;
	}
	
	
	// Lorsqu'on accepte une demande de connexion
	public boolean acceptFriend(String current_user_login, String friend_login) throws RemoteException {

		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		try {
			docBuilder = docFactory.newDocumentBuilder();
			// Find or Create XML
			StreamResult result;
			File xml = new File("connexions.xml");
			Document doc;
			Element rootElement;
			
			if(xml.exists()){
				result = new StreamResult(xml);
				doc = docBuilder.parse(xml);
				rootElement = (Element) doc.getElementsByTagName("connexions").item(0);
				NodeList current_user_requests = doc.getElementsByTagName("recipient_login");
				for(int i = 0; i < current_user_requests.getLength(); i++){
					Element parent = (Element) current_user_requests.item(i).getParentNode();
					if (parent.getElementsByTagName("recipient_login").item(0).getTextContent().equals(current_user_login)){
						if (parent.getElementsByTagName("sender_login").item(0).getTextContent().equals(friend_login)){
							parent.getElementsByTagName("state").item(0).setTextContent("accepted");
						}
					}
				}
			} else {
				System.out.println("Une erreur est survenue, aucune connexion enregistrée en base");
				return false;
			}
			
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			DOMSource source = new DOMSource(doc);
			
			transformer.transform(source, result);

			System.out.println("File saved!");
			return true;
			
		} catch (Exception e){
			System.out.println("Erreur Tier 3 : Impossible d'ajouter l'utilisateur");
			e.printStackTrace();
		}
		return false;
	}
	
	// Tous les utilisateurs connecté au current_user, quelque soit l'état de la connexion (accepted, denied, ou pending)
		public Users users_from_connexion(String current_user_login) throws RemoteException {
			Users listUsers = new Users();
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			
			try {
				DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
				
				// Find or Create XML
				StreamResult result;
				File xml = new File("connexions.xml");
				Document doc;
				Element rootElement;
				
				if(xml.exists()){
					result = new StreamResult(xml);
					doc = docBuilder.parse(xml);
					rootElement = (Element) doc.getElementsByTagName("connexions").item(0);
					
					NodeList connections_id = doc.getElementsByTagName("id");
					for(int i = 0; i < connections_id.getLength(); i++){
						Element parent = (Element) connections_id.item(i).getParentNode();
						
						if (parent.getElementsByTagName("sender_login").item(0).getTextContent().equals(current_user_login)){
							listUsers.addUser(parent.getElementsByTagName("recipient_login").item(0).getTextContent());
						}
						if (parent.getElementsByTagName("recipient_login").item(0).getTextContent().equals(current_user_login)){
							listUsers.addUser(parent.getElementsByTagName("sender_login").item(0).getTextContent());
						}
					}
					return listUsers;
				}
			
			} catch (Exception e){
				e.printStackTrace();
			}
			return null;
		}
		
	
}
