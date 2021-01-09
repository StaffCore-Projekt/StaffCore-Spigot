package de.lacodev.staffcore.errors;

public class ReasonNotExistsExeption extends Exception{
    private static final long serialVersionUID = 1L;

    public ReasonNotExistsExeption(String reason) {
        super("The Reason " + reason + " does not exists!");
    }
}
