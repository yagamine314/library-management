package com.library.dao;

import com.library.model.Livre;
import java.sql.SQLException;
import java.util.List;

/**
 * Interface DAO spécifique pour les livres.
 * Étend l'interface générique DAO pour les opérations CRUD sur les livres.
 */
public interface LivreDAO extends DAO<Livre> {

    /**
     * Recherche des livres par titre (recherche partielle).
     */
    List<Livre> findByTitre(String titre) throws SQLException;

    /**
     * Recherche des livres par auteur (recherche partielle).
     */
    List<Livre> findByAuteur(String auteur) throws SQLException;

    /**
     * Recherche des livres disponibles.
     */
    List<Livre> findDisponibles() throws SQLException;

    /**
     * Recherche des livres indisponibles.
     */
    List<Livre> findIndisponibles() throws SQLException;

    /**
     * Recherche un livre par ISBN.
     */
    Livre findByIsbn(String isbn) throws SQLException;
}
