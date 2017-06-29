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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TreeMap;

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

import org.w3c.dom.DOMException;
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
	
	private List<User> ListUsers = new ArrayList<User>();
	private List<Message> ListMessages = new ArrayList<Message>();
	
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
	
	}
	
	public void sign_in(User user) throws RemoteException
	{
		synchronized(ListUsers)
		{
			if (!ListUsers.contains(user))
			{
				System.out.println("Un utilisateur s'est connecté.");
				ListUsers.add(user);
			}
		}
	}
	
	public void sign_out(User user) throws RemoteException
	{
		synchronized(ListUsers)
		{
			System.out.println("Un utilisateur s'est connecté.");
			ListUsers.remove(user);
		}
	}
	
	@Override
	public boolean add_new_user(String login, String password) throws RemoteException {
		synchronized(ListUsers){
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
	}

	@Override
	public User find_user(String login) throws RemoteException {
		synchronized(ListUsers){
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
	}
	
	@Override
	// Users List = All users - friend - denied connexion.
	public Users users_list(String current_user_login) throws RemoteException {
		synchronized(ListUsers){
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
	}
	
	@Override
	public Users friends_list(String current_user_login) throws RemoteException {
		synchronized(ListUsers){
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
	}
	
	@Override
	public Users friend_requests_list(String current_user_login) throws RemoteException {
		synchronized(ListUsers){
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
	}
	
	public boolean addFriend(String user_login, String friend_login) throws RemoteException {
		synchronized(ListUsers) {
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
	}
	
	
	// Lorsqu'on accepte une demande de connexion
	public boolean acceptFriend(String current_user_login, String friend_login) throws RemoteException {
		synchronized(ListUsers){

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
	}
	
	// Tous les utilisateurs connecté au current_user, quelque soit l'état de la connexion (accepted, denied, ou pending)
		public Users users_from_connexion(String current_user_login) throws RemoteException {
			synchronized(ListUsers){
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
		
		
		// MESSAGES
		
		public boolean sendMessage(String user_login, String friend_login, String content) throws RemoteException {
			synchronized(ListUsers){
				DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder;
				try {
					docBuilder = docFactory.newDocumentBuilder();
					// Find or Create XML
					StreamResult result;
					File xml = new File("conversations.xml");
					Document doc;
					Element rootElement;
					
					int xml_last_id = 0;
					
					if(xml.exists()){
						result = new StreamResult(xml);
						doc = docBuilder.parse(xml);
						rootElement = (Element) doc.getElementsByTagName("conversations").item(0);
						NodeList id_nodes = doc.getElementsByTagName("id");
						if(id_nodes.getLength() > 0){
							xml_last_id = Integer.parseInt(id_nodes.item(id_nodes.getLength()-1).getTextContent());
							//System.out.println(xml_last_id);
						}
					}
					else {
						doc = docBuilder.newDocument();
						result = new StreamResult(new File("conversations.xml"));
						// root elements
						rootElement = doc.createElement("conversations");
						doc.appendChild(rootElement);
					}
					
					//On récupère tous les noeuds "user"
					NodeList users_nodes = doc.getElementsByTagName("user");
					Element conversation = null;
					Element messages = null;
					for(int i = 0; i < users_nodes.getLength(); i++){
						Element parent = (Element) users_nodes.item(i).getParentNode();
						
						// On check si l'un des user est le current_user.
						// Si oui, on verifie si le deuxième user est le destinataire du message.
						// Si oui, on assigne la conversation, sinon on en créé une.
						if (parent.getElementsByTagName("user").item(0).getTextContent().equals(user_login)){
							if (parent.getElementsByTagName("user").item(1).getTextContent().equals(friend_login)){
								conversation = (Element) parent.getParentNode();
								messages = (Element) conversation.getElementsByTagName("messages").item(0);
							}
						} else if (parent.getElementsByTagName("user").item(1).getTextContent().equals(user_login)){
							if (parent.getElementsByTagName("user").item(0).getTextContent().equals(friend_login)){
								conversation = (Element) parent.getParentNode();
								messages = (Element) conversation.getElementsByTagName("messages").item(0);
	
							}
						}
					}
					
					// On créé la conversation si elle n'existe pas
					if(conversation == null){
						conversation = doc.createElement("conversation");
						
						Element conversation_id = doc.createElement("id");
						conversation_id.setTextContent(String.valueOf(xml_last_id + 1));
						
						Element users = doc.createElement("users");
						Element user_1 = doc.createElement("user");
						user_1.setTextContent(user_login);
						Element user_2 = doc.createElement("user");
						user_2.setTextContent(friend_login);
						
						Element start_date = doc.createElement("start_date");
						start_date.setTextContent((new Date()).toString());
						
						messages = doc.createElement("messages");
						
						rootElement.appendChild(conversation);
						
						users.appendChild(user_1);
						users.appendChild(user_2);
						
						conversation.appendChild(conversation_id);
						conversation.appendChild(start_date);
						conversation.appendChild(users);
						conversation.appendChild(messages);
	
					}
	
					Element message = doc.createElement("message");
	
					Element message_content = doc.createElement("content");
					message_content.setTextContent(content);
					Element author = doc.createElement("author");
					author.setTextContent(user_login);
					Element created_at = doc.createElement("created_at");
					created_at.setTextContent((new Date()).toString());
					Element read = doc.createElement("read");
					read.setTextContent("false");
	
					
					messages.appendChild(message);
					
					message.appendChild(created_at);
					message.appendChild(author);
					message.appendChild(message_content);
					message.appendChild(read);
					
					// write the content into xml file
					TransformerFactory transformerFactory = TransformerFactory.newInstance();
					Transformer transformer = transformerFactory.newTransformer();
					transformer.setOutputProperty(OutputKeys.INDENT, "yes");
					transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
					DOMSource source = new DOMSource(doc);
					
					transformer.transform(source, result);
	
					return true;
					
				} catch (Exception e){
					System.out.println("Erreur Tier 3 : Impossible d'ajouter l'utilisateur");
					e.printStackTrace();
				}
				return false;
			}
		}
		
		public Conversation getLastTenMessages(String userLogin, String friendLogin) throws RemoteException {
			synchronized(ListUsers){
				Conversation last_ten_messages = new Conversation();
				last_ten_messages.setUser1(new User(userLogin, ""));
				last_ten_messages.setUser1(new User(friendLogin, ""));
				
				DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder;
				
				try {
					docBuilder = docFactory.newDocumentBuilder();
					// Find or Create XML
					StreamResult result;
					File xml = new File("conversations.xml");
					Document doc;
					Element rootElement;
					
					if(xml.exists()){
						result = new StreamResult(xml);
						doc = docBuilder.parse(xml);
						rootElement = (Element) doc.getElementsByTagName("conversations").item(0);
						
						/*------ RECUPERATION DE TOUS LES MESSAGES D'UNE CONVERSATION -------*/
						//On récupère tous les noeuds "user"
						NodeList user_nodes = rootElement.getElementsByTagName("user");
						Element conversation = null;
						Element messages = null;
						for(int i = 0; i < user_nodes.getLength(); i++){
							Element users = (Element) user_nodes.item(i).getParentNode();
							// On check si l'un des user est le current_user.
							// Si oui, on verifie si le deuxième user est le destinataire du message.
							if (users.getElementsByTagName("user").item(0).getTextContent().equals(userLogin)){
								if (users.getElementsByTagName("user").item(1).getTextContent().equals(friendLogin)){
									conversation = (Element) users.getParentNode();
									messages = (Element) conversation.getElementsByTagName("messages").item(0);
								}
							} else if (users.getElementsByTagName("user").item(1).getTextContent().equals(userLogin)){
								if (users.getElementsByTagName("user").item(0).getTextContent().equals(friendLogin)){
									conversation = (Element) users.getParentNode();
									messages = (Element) conversation.getElementsByTagName("messages").item(0);
								}
							}
						}
						
						// On récupère les noeuds "message"
						NodeList message_nodes = messages.getElementsByTagName("message");
						
						// On initialise une variable pour la valeur initiale de la variable d'incrémentation
						// S'il y a moins de 10 messages, on l'initialise à 0 : on récupère tous les messages
						// S'il y a plus de 10 messages, on initialise au nb de messages -10, pour récupérer que les 10 derniers
						int loop_origin = 0;
						if (message_nodes.getLength() > 10){
							loop_origin = message_nodes.getLength()-10;
						}
						for (int j = loop_origin ; j < message_nodes.getLength(); j++){
							Element message_elt = (Element) message_nodes.item(j);
							DateFormat df = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy", Locale.ENGLISH);
							Date datetime = df.parse(message_elt.getElementsByTagName("created_at").item(0).getTextContent());
							String content = message_elt.getElementsByTagName("content").item(0).getTextContent();
							User author = new User(message_elt.getElementsByTagName("author").item(0).getTextContent(), "");
							Message message = new Message(null, author, content, datetime);
							last_ten_messages.addMessage(message);
						}
					}
					
				} catch (Exception e){
					System.out.println("Erreur Tier 3 : Impossible de récupérer la conversation");
					e.printStackTrace();
				}
				return last_ten_messages;
			}
		}
}
