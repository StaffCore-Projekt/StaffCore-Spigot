package de.lacodev.rsystem.errors;

/**
 * @author ViaEnder
 * @version 1.0.0
 * */
public class PlayerIsProtectedExeption extends Exception{


    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PlayerIsProtectedExeption(String playerName) {
        super("Player : " + playerName + " is Protected!");
    }
}
