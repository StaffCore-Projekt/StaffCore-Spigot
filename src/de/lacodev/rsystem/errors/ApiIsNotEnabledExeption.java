package de.lacodev.rsystem.errors;

/**
 * @author ViaEnder
 * @version 1.0.0
 * */
public class ApiIsNotEnabledExeption extends Exception{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ApiIsNotEnabledExeption() {
        super("API Is Not Enabled, Please look in the Staffcore Config.yml");
    }
}
