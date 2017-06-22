package model;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


// NOTE : Comme nous n'avons pas besoin du mot de passe lorsqu'on liste des Users.
// Cette liste ne contiendra donc que les "login".

@XmlRootElement(name = "Users")
public class Users 
{
	@XmlElement
	public ArrayList<String> liste = new ArrayList<String>();
} 