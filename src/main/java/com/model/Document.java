package com.model;

import java.time.LocalDateTime;

/**
 * Classe abstraite représentant un document dans la bibliothèque.
 */
public abstract class Document {

    protected String isbn;
    protected String titre;
    protected String auteur;
    protected int anneePublication;
    protected boolean disponible;
    protected LocalDateTime createdAt;
    protected LocalDateTime updatedAt;

    /**
     * Constructeur par défaut.
     */
    public Document() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Constructeur avec paramètres.
     */
    public Document(String isbn, String titre, String auteur, int anneePublication, boolean disponible) {
        this.isbn = isbn;
        this.titre = titre;
        this.auteur = auteur;
        this.anneePublication = anneePublication;
        this.disponible = disponible;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Getters et setters
    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
        this.updatedAt = LocalDateTime.now();
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
        this.updatedAt = LocalDateTime.now();
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
        this.updatedAt = LocalDateTime.now();
    }

    public int getAnneePublication() {
        return anneePublication;
    }

    public void setAnneePublication(int anneePublication) {
        this.anneePublication = anneePublication;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
        this.updatedAt = LocalDateTime.now();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public String toString() {
        return "Document{" +
                "isbn='" + isbn + '\'' +
                ", titre='" + titre + '\'' +
                ", auteur='" + auteur + '\'' +
                ", anneePublication=" + anneePublication +
                ", disponible=" + disponible +
                '}';
    }
}
