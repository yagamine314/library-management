package com.library.dao;

import java.sql.SQLException;
import java.util.List;

import com.library.model.Emprunt;

/**
 * Interface DAO pour la gestion des emprunts
 */
public interface EmpruntDAO {

    /**
     * Enregistre un nouvel emprunt
     */
    void save(Emprunt emprunt) throws SQLException;

    /**
     * Recherche un emprunt par son ID
     */
    Emprunt findById(int id) throws SQLException;

    /**
     * Récupère tous les emprunts
     */
    List<Emprunt> findAll() throws SQLException;

    /**
     * Récupère les emprunts d'un membre
     */
    List<Emprunt> findByMembreId(int membreId) throws SQLException;

    /**
     * Récupère les emprunts d'un livre
     */
    List<Emprunt> findByLivreId(String id) throws SQLException;

    /**
     * Récupère les emprunts non retournés
     */
    List<Emprunt> findEmpruntsEnCours() throws SQLException;

    /**
     * Met à jour un emprunt
     */
    void update(Emprunt emprunt) throws SQLException;

    /**
     * Supprime un emprunt par son ID
     */
    void delete(int id) throws SQLException;

    /**
     * Enregistre le retour d'un livre
     */
    void retournerLivre(int empruntId, java.sql.Date dateRetourEffective) throws SQLException;

    /**
     * Compte le nombre d'emprunts en cours pour un membre
     */
    int countEmpruntsEnCoursByMembre(int membreId) throws SQLException;

    /**
     * Vérifie si un livre existe
     */
    boolean livreExiste(String isbn) throws SQLException;

    /**
     * Vérifie si un membre existe
     */
    boolean membreExiste(int membreId) throws SQLException;

    /**
     * Vérifie si un livre est disponible
     */
    boolean isLivreDisponible(String isbn) throws SQLException;

    /**
     * Marque un livre comme indisponible
     */
    void marquerLivreIndisponible(String isbn) throws SQLException;

    /**
     * Marque un livre comme disponible
     */
    void marquerLivreDisponible(String isbn) throws SQLException;

    /**
     * Vérifie si un membre est actif
     */
    boolean isMembreActif(int membreId) throws SQLException;
}
