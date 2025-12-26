package com.library.exception;

public class LivreIndisponibleException extends Exception {
    
    private final String isbn;
    
    public LivreIndisponibleException(String isbn) {
        super("Le livre avec l'ISBN " + isbn + " n'est pas disponible.");
        this.isbn = isbn;
    }
    
    public LivreIndisponibleException(String isbn, String message) {
        super(message);
        this.isbn = isbn;
    }
    
    public String getIsbn() {
        return isbn;
    }
    
    public static LivreIndisponibleException dejaEmprunte(String isbn) {
        return new LivreIndisponibleException(isbn, 
            "Le livre avec l'ISBN " + isbn + " est déjà emprunté.");
    }
    
    public static LivreIndisponibleException inexistant(String isbn) {
        return new LivreIndisponibleException(isbn, 
            "Aucun livre trouvé avec l'ISBN " + isbn + ".");
    }
}