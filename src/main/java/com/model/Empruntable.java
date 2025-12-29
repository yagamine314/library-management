package com.model;

/**
 * Interface pour les objets empruntables dans la bibliothèque.
 */
public interface Empruntable {

    /**
     * Vérifie si l'objet est disponible pour emprunt.
     * @return true si disponible, false sinon
     */
    boolean isDisponible();

    /**
     * Marque l'objet comme emprunté.
     */
    void emprunter();

    /**
     * Marque l'objet comme retourné.
     */
    void retourner();
}
