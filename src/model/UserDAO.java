package model;

public class UserDAO {
	public static User find_user(String login){
		// TODO : Elodie : Trouver un utilisateur dans le txt qui correspond au login. Sinon null;
		return new User(login, "password");
	}
}
