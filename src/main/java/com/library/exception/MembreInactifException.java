package com.library.exception;

public class MembreInactifException extends Exception {
    
    private final int membreId;
    
    public MembreInactifException(int membreId) {
        super("Le membre avec l'ID " + membreId + " est inactif et ne peut pas emprunter.");
        this.membreId = membreId;
    }
    
    public MembreInactifException(int membreId, String message) {
        super(message);
        this.membreId = membreId;
    }
    
    public int getMembreId() {
        return membreId;
    }
}