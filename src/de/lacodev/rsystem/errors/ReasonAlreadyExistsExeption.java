package de.lacodev.rsystem.errors;

/**
 * @author ViaEnder
 * @version 1.0.0
 * */
public class ReasonAlreadyExistsExeption extends Exception{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ReasonAlreadyExistsExeption(String reason) {
        super("The Reason: " + reason + " already exists!");
    }
}
