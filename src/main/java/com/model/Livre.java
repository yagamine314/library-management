package com.model;

/**
 * Classe représentant un livre dans la bibliothèque.
 * Hérite de Document et implémente l'interface Empruntable.
 */
public class Livre extends Document implements Empruntable {

    /**
     * Constructeur par défaut.
     */
    public Livre() {
        super();
    }

    /**
     * Constructeur avec paramètres.
     */
    public Livre(String isbn, String titre, String auteur, int anneePublication, boolean disponible) {
        super(isbn, titre, auteur, anneePublication, disponible);
    }

    @Override
    public boolean isDisponible() {
        return disponible;
    }

    @Override
    public void emprunter() {
        if (disponible) {
            this.disponible = false;
            this.updatedAt = java.time.LocalDateTime.now();
        } else {
            throw new IllegalStateException("Le livre n'est pas disponible pour emprunt.");
        }
    }

    @Override
    public void retourner() {
        this.disponible = true;
        this.updatedAt = java.time.LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Livre{" +
                "isbn='" + isbn + '\'' +
                ", titre='" + titre + '\'' +
                ", auteur='" + auteur + '\'' +
                ", anneePublication=" + anneePublication +
                ", disponible=" + disponible +
                '}';
    }
}
