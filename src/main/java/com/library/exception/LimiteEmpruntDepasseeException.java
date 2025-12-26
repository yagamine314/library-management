package com.library.exception;

public class LimiteEmpruntDepasseeException extends Exception {
    
    private final int membreId;
    private final int nombreEmpruntsEnCours;
    private static final int LIMITE_MAX = 3;
    
    public LimiteEmpruntDepasseeException(int membreId, int nombreEmpruntsEnCours) {
        super(String.format(
            "Le membre avec l'ID %d a déjà %d emprunt(s) en cours. Limite max: %d.",
            membreId, nombreEmpruntsEnCours, LIMITE_MAX
        ));
        this.membreId = membreId;
        this.nombreEmpruntsEnCours = nombreEmpruntsEnCours;
    }
    
    public int getMembreId() {
        return membreId;
    }
    
    public int getNombreEmpruntsEnCours() {
        return nombreEmpruntsEnCours;
    }
    
    public static int getLimiteMax() {
        return LIMITE_MAX;
    }
}