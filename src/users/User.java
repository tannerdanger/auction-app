package users;

import java.io.Serializable;

/**
 * User class, saves relevent information to both bidders and contacts,
 * i.e. contact information
 * @author Tanner Brown
 * @version 05/07/2018
 */
public class User implements Serializable{


	private String firstName;
	private String lastName;
	private String email;
	//etc...
	public User(String theFirst, String theLast, String theEmail){
		firstName = theFirst;
		lastName = theLast;
		email = theEmail;
	}


	//getters / setters
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String theFirstName) {
		this.firstName = theFirstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String theLastName) {
		this.lastName = theLastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String theEmail) {
		this.email = theEmail;
	}


}
