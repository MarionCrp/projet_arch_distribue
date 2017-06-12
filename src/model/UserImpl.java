package model;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Scanner;

import messagerie.Tier2;

public class UserImpl  extends UnicastRemoteObject implements User, Runnable
{
	private static final long serialVersionUID = 1L;

	public Tier2 tier2;
	
	private String login;
	private String password;
	
	public ArrayList<Message> nouveaux_messages = new ArrayList();
	
	public UserImpl(Tier2 tier2, String login, String password) throws RemoteException
	{
		this.tier2 = tier2;
		this.login = login;
		this.password = password;
	}
	
	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	@Override
	public void envoyer_new_message(Message message) throws RemoteException
	{
		synchronized (nouveaux_messages)
		{
			nouveaux_messages.add(message);
		}
	}

	@Override
	public void run() 
	{
		Scanner stdin = new Scanner(System.in);
		boolean fin = false;
		while (!fin)
		{
			try
			{
				System.out.println("Que voulez vous ? (se connecter)");
				System.out.print("> ");
				String demande = stdin.nextLine();
				if (demande.equals("se connecter"))
				{
					System.out.println("Login");
					System.out.print("> ");
					String login = stdin.nextLine();
					System.out.println("Password");
					System.out.print("> ");
					String password = stdin.nextLine();
					System.out.println("Connexion.");
					
					tier2.connecter(login, password);
				}
				else if (demande.equals("se déconnecter"))
				{
					System.out.println("Vous avez été deconnecté.");
					tier2.deconnecter(this);
				}
				else if (demande.equals("voir nouveaux messages"))
				{
					synchronized (nouveaux_messages)
					{
						if (nouveaux_messages.isEmpty())
						{
							System.out.println("Pas de nouveau message");
						}
						else
						{
							System.out.println("Affichage des nouveaux messages : ");
							for (Message message : nouveaux_messages)
							{
								System.out.println(message.getContent());
							}
							nouveaux_messages.clear();
						}
					}
				}
				else if (demande.equals("quitter"))
				{
					fin = true;
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		stdin.close();
	}

}
