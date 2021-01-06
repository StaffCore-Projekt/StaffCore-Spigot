package de.lacodev.rsystem.errors;

/**
 * @author ViaEnder
 * @version 1.0.0
 * */
public class ReasonNotFoundExeption extends Exception{


    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ReasonNotFoundExeption(String reason) {
        super("The Reason " + reason + "was not found!");
    }
}
