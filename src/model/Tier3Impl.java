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
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
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
	
	private List<User> listUsers = new ArrayList<User>();

	public Tier3Impl() throws RemoteException {
		
	}
	
	public static void main(String[] arg) {
		System.setProperty("java.rmi.server.hostname", "127.0.0.1");
		try {
			Registry registry = LocateRegistry.createRegistry(2000);
			Tier3Impl tier3 = new Tier3Impl();
			registry.bind("Tier3", tier3);
			Thread.sleep(99999);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AlreadyBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

			  } catch (ParserConfigurationException pce) {
				pce.printStackTrace();
			  } catch (TransformerException tfe) {
				tfe.printStackTrace();
			  } catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
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
		  } catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		  } catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
}
