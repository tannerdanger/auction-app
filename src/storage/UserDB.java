package storage;

import users.User;
import java.util.HashMap;

/**
 * A serializable class that stores all users data
 */
public class UserDB {

	//email is key(username), value is the user associated with email address
	private static HashMap<String, User> userDirectory = new HashMap<>();


	public User getUser(String emailAddress){

		//If user exists in hashmap,
		if(userDirectory.containsKey(emailAddress))
			return userDirectory.get(emailAddress);

		//if user doesn't exist, return null and handle non-existant user where method was called
		else
			return null;
	}
}
