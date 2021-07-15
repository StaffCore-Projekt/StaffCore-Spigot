package de.lacodev.staffcore.errors;

public class ReasonIDNotExistsExeption extends Exception {

    private static final long serialVersionUID = 1L;

    public ReasonIDNotExistsExeption(int id, String ban) {
        super("The " + ban + " Reason ID: '" + id + "' does not exists!");
    }
}
