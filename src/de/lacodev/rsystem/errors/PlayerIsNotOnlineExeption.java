package de.lacodev.rsystem.errors;

/**
 * @author ViaEnder
 * @version 1.0.0
 * */
public class PlayerIsNotOnlineExeption extends Exception{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PlayerIsNotOnlineExeption(String username) {
        super("The Player '" + username + "' is not online!");
    }
}
