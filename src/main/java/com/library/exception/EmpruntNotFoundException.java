package com.library.exception;

public class EmpruntNotFoundException extends Exception {

    private final int empruntId;

    public EmpruntNotFoundException(int empruntId) {
        super("L'emprunt avec l'ID " + empruntId + " n'existe pas.");
        this.empruntId = empruntId;
    }

    public EmpruntNotFoundException(int empruntId, String message) {
        super(message);
        this.empruntId = empruntId;
    }

    public int getEmpruntId() {
        return empruntId;
    }
}
