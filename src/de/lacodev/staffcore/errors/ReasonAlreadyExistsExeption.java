package de.lacodev.staffcore.errors;

public class ReasonAlreadyExistsExeption extends Exception{
    private static final long serialVersionUID = 1L;

    public ReasonAlreadyExistsExeption(String reason) {
        super("The Reason '" + reason + "' already exists!");
    }
}
