package model;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


// NOTE : Comme nous n'avons pas besoin du mot de passe lorsqu'on liste des Users.
// Cette liste ne contiendra donc que les "login".

@XmlRootElement(name = "Connexions")
public class Connexions 
{
	@XmlElement
	public ArrayList<Connexion> liste = new ArrayList<Connexion>();
	
	public Connexions(){
		
	}
	
	public void addConnexion(Connexion connexion){
		this.liste.add(connexion);
	}
	
	
} 