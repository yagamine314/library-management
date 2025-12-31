
package com.library.model;

import com.library.exception.LivreIndisponibleException;

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
     * @throws LivreIndisponibleException 
     */
    void emprunter() throws LivreIndisponibleException;

    /**
     * Marque l'objet comme retourné.
     */
    void retourner();
}
