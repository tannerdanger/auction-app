package storage;
 

import users.User;
 
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
 
/**
 * A Serializable class that stores all users data.
 * @author Tanner Brown
 * @version 5 May 2018
 */
public class UserDB implements Serializable {
 
    private static final long serialVersionUID = 12813308004L;
 
    //email is key(username), value is the user associated with email address
    public Map<String, User> userDirectory;

 
    /**
     * Constructor.
     */
    public UserDB(){
        userDirectory = new HashMap<String, User>();

    }


    /**
     * Returns a user
     * @param emailAddress the email address of the user being returned
     * @return a user
     */
    public User getUser(String emailAddress){
 
        //If user exists in hashmap,
        if(userDirectory.containsKey(emailAddress)) {
            return userDirectory.get(emailAddress);
        }
 
        //if user doesn't exist, return null and handle non-existant user where method was called
        else
            return null;
    }
 
    /**
     * Adds a user to the userDirectory
     * @param theUser the user being added to the directory.
     */
    public void addUser(User theUser){
 
        if(userDirectory.containsKey(theUser.getEmail()))
            System.out.println("User " +theUser.getEmail() +" already exists.");
        else
            userDirectory.put(theUser.getEmail(), theUser);
    }
}