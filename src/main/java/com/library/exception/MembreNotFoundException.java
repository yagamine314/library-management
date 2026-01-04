package com.library.exception;


public class MembreNotFoundException extends RuntimeException {

    private Long membreId;
    private String email;


    public MembreNotFoundException(String message) {
        super(message);
    }

    
    public MembreNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    
    public MembreNotFoundException(String message, Long membreId) {
        super(message);
        this.membreId = membreId;
    }

    
    public MembreNotFoundException(String message, String email) {
        super(message);
        this.email = email;
    }

    
    public static MembreNotFoundException parId(Long id) {
        return new MembreNotFoundException(
            "Aucun membre trouvé avec l'ID: " + id, 
            id
        );
    }

   
    public static MembreNotFoundException parEmail(String email) {
        return new MembreNotFoundException(
            "Aucun membre trouvé avec l'email: " + email, 
            email
        );
    }

   
    public Long getMembreId() {
        return membreId;
    }

    
    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("MembreNotFoundException: ");
        sb.append(getMessage());
        if (membreId != null) {
            sb.append(" [ID: ").append(membreId).append("]");
        }
        if (email != null) {
            sb.append(" [Email: ").append(email).append("]");
        }
        return sb.toString();
    }
}