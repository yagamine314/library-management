package com.library.util;

import com.library.exception.ValidationException;
import java.util.regex.Pattern;

/**
 * Classe utilitaire pour valider les chaînes de caractères
 */
public class StringValidator {
    
    // Pattern pour validation email
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
    
    // Pattern pour validation ISBN (978 ou 979 + 10 chiffres avec tirets)
    private static final Pattern ISBN_PATTERN = Pattern.compile(
        "^97[89]-\\d-\\d{3}-\\d{5}-\\d$"
    );
    
    /**
     * Valide un email
     */
    public static void validateEmail(String email) throws ValidationException {
        if (email == null || email.trim().isEmpty()) {
            throw ValidationException.champManquant("email");
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw ValidationException.formatInvalide("email", "exemple@domaine.com");
        }
    }
    
    /**
     * Valide un ISBN
     */
    public static void validateISBN(String isbn) throws ValidationException {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw ValidationException.champManquant("ISBN");
        }
        if (!ISBN_PATTERN.matcher(isbn).matches()) {
            throw ValidationException.formatInvalide("ISBN", "978-X-XXX-XXXXX-X");
        }
    }
    
    /**
     * Vérifie qu'un champ n'est pas vide
     */
    public static void validateNotEmpty(String value, String fieldName) throws ValidationException {
        if (value == null || value.trim().isEmpty()) {
            throw ValidationException.champManquant(fieldName);
        }
    }
    
    /**
     * Valide la longueur min/max
     */
    public static void validateLength(String value, String fieldName, int minLength, int maxLength) 
            throws ValidationException {
        if (value == null) {
            throw ValidationException.champManquant(fieldName);
        }
        if (value.length() < minLength || value.length() > maxLength) {
            throw new ValidationException(
                fieldName + " doit avoir entre " + minLength + " et " + maxLength + " caractères."
            );
        }
    }
}