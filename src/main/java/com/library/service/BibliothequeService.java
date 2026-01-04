package com.library.service;

import com.library.dao.LivreDAO;
import com.library.dao.impl.LivreDAOImpl;
import com.library.exception.LivreIndisponibleException;
import com.library.exception.ValidationException;
import com.library.util.StringValidator;
import com.library.model.Livre;
import java.sql.SQLException;
import java.util.List;

/**
 * Service de gestion de la bibliothèque pour les opérations sur les livres.
 */
public class BibliothequeService {

    private final LivreDAO livreDAO;

    /**
     * Constructeur par défaut utilisant l'implémentation par défaut du DAO.
     */
    public BibliothequeService() {
        this.livreDAO = new LivreDAOImpl();
    }

    /**
     * Constructeur avec injection de dépendance pour le DAO.
     */
    public BibliothequeService(LivreDAO livreDAO) {
        this.livreDAO = livreDAO;
    }

    /**
     * Ajoute un nouveau livre à la bibliothèque.
     */
    public void ajouterLivre(Livre livre) throws SQLException, ValidationException {
        validerLivre(livre);
        livreDAO.save(livre);
    }

    /**
     * Recherche un livre par ISBN.
     */
    public Livre trouverLivreParIsbn(String isbn) throws SQLException, ValidationException {
        StringValidator.validateISBN(isbn);
        return livreDAO.findByIsbn(isbn);
    }

    /**
     * Recherche un livre par identifiant.
     */
    public Livre trouverLivreParId(Object id) throws SQLException {
        return livreDAO.findById(id);
    }

    /**
     * Récupère tous les livres.
     */
    public List<Livre> listerTousLesLivres() throws SQLException {
        return livreDAO.findAll();
    }

    /**
     * Recherche des livres par titre.
     */
    public List<Livre> rechercherParTitre(String titre) throws SQLException, ValidationException {
        StringValidator.validateNotEmpty(titre, "Titre");
        return livreDAO.findByTitre(titre);
    }

    /**
     * Recherche des livres par auteur.
     */
    public List<Livre> rechercherParAuteur(String auteur) throws SQLException, ValidationException {
        StringValidator.validateNotEmpty(auteur, "Auteur");
        return livreDAO.findByAuteur(auteur);
    }

    /**
     * Récupère les livres disponibles.
     */
    public List<Livre> listerLivresDisponibles() throws SQLException {
        return livreDAO.findDisponibles();
    }

    /**
     * Récupère les livres indisponibles.
     */
    public List<Livre> listerLivresIndisponibles() throws SQLException {
        return livreDAO.findIndisponibles();
    }

    /**
     * Met à jour les informations d'un livre.
     */
    public void modifierLivre(Livre livre) throws SQLException, ValidationException {
        validerLivre(livre);
        livreDAO.update(livre);
    }

    /**
     * Supprime un livre par identifiant.
     */
    public void supprimerLivre(Object id) throws SQLException {
        livreDAO.delete(id);
    }

    /**
     * Emprunte un livre.
     */
    public void emprunterLivre(String isbn) throws SQLException, ValidationException, LivreIndisponibleException {
        Livre livre = trouverLivreParIsbn(isbn);
        if (livre == null) {
            throw LivreIndisponibleException.inexistant(isbn);
        }
        if (!livre.isDisponible()) {
            throw LivreIndisponibleException.dejaEmprunte(isbn);
        }
        livre.emprunter();
        modifierLivre(livre);
    }

    /**
     * Retourne un livre.
     */
    public void retournerLivre(String isbn) throws SQLException, ValidationException {
        Livre livre = trouverLivreParIsbn(isbn);
        if (livre == null) {
            throw new ValidationException("Livre introuvable avec l'ISBN: " + isbn);
        }
        livre.retourner();
        modifierLivre(livre);
    }

    /**
     * Valide les données d'un livre.
     */
    private void validerLivre(Livre livre) throws ValidationException {
        StringValidator.validateNotEmpty(livre.getIsbn(), "ISBN");
        StringValidator.validateISBN(livre.getIsbn());
        StringValidator.validateNotEmpty(livre.getTitre(), "Titre");
        StringValidator.validateLength(livre.getTitre(), "Titre", 1, 255);
        StringValidator.validateNotEmpty(livre.getAuteur(), "Auteur");
        StringValidator.validateLength(livre.getAuteur(), "Auteur", 1, 255);
        if (livre.getAnneePublication() < 0 || livre.getAnneePublication() > java.time.Year.now().getValue()) {
            throw new ValidationException("Année de publication invalide");
        }
    }
}
