package com.library.exception;

public class ValidationException extends Exception {
    
    public ValidationException(String message) {
        super(message);
    }
    
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public static ValidationException champManquant(String fieldName) {
        return new ValidationException("Le champ '" + fieldName + "' est obligatoire.");
    }
    
    public static ValidationException formatInvalide(String fieldName, String expectedFormat) {
        return new ValidationException(
            "Le champ '" + fieldName + "' a un format invalide. Format attendu: " + expectedFormat
        );
    }
}