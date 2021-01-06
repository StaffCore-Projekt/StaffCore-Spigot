package de.lacodev.rsystem.errors;

/**
 * @author ViaEnder
 * @version 1.0.0
 * */
public class ReportNotOpenExeption extends Exception{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ReportNotOpenExeption(String username) {
        super("There is no open Report of " + username + " !");
    }
}
