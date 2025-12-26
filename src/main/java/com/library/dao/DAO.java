package com.library.dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Interface générique DAO définissant les opérations CRUD.
 * @param <T> le type d'entité géré par ce DAO
 */
public interface DAO<T> {
    
    
    //Sauvegarde une nouvelle entité
    
    void save(T entity) throws SQLException;
    
    
    //Recherche par identifiant
     
    T findById(Object id) throws SQLException;
    
    
    //Récupère toutes les entités
     
    List<T> findAll() throws SQLException;
    
    /**
     * Met à jour une entité
     */
    void update(T entity) throws SQLException;
    
    /**
     * Supprime par identifiant
     */
    void delete(Object id) throws SQLException;
}