package com.library.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.library.dao.LivreDAO;
import com.library.dao.MembreDAO;
import com.library.dao.impl.LivreDAOImpl;
import com.library.dao.impl.MembreDAOImpl;
import com.library.exception.LivreIndisponibleException;
import com.library.exception.ValidationException;
import com.library.model.Emprunt;
import com.library.model.Livre;
import com.library.model.Membre;
import com.library.util.StringValidator;

/**
 * Service de gestion de la bibliothèque pour les opérations sur les livres.
 */
public class BibliothequeService {

    private final LivreDAO livreDAO;
    private final MembreDAO membreDAO;

    /**
     * Constructeur par défaut utilisant l'implémentation par défaut du DAO.
     */
    public BibliothequeService() throws SQLException {
        this.livreDAO = new LivreDAOImpl();
        this.membreDAO = new MembreDAOImpl();
    }

    /**
     * Constructeur avec injection de dépendance pour le DAO.
     */
    public BibliothequeService(LivreDAO livreDAO, MembreDAO membreDAO) {
        this.livreDAO = livreDAO;
        this.membreDAO = membreDAO;
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
    // ==================== PARTIE MEMBRES ====================

    /**
     * Ajoute un nouveau membre à la bibliothèque.
     */
    public Membre ajouterMembre(Membre membre) throws SQLException, ValidationException {
        validerMembre(membre);
        // Vérifier l'unicité de l'email
        if (!isEmailUnique(membre.getEmail(),0)) {
            throw new ValidationException("L'email est déjà utilisé par un autre membre");
        }
        return membreDAO.save(membre);
    }

    /**
     * Récupère un membre par son identifiant.
     */
    public Membre getMembreById(int id) throws SQLException ,  ValidationException {
        Optional<Membre> membreOpt = membreDAO.findById(id);
        if (!membreOpt.isPresent()) {
            throw new ValidationException("Membre non trouvé avec l'ID: " + id);
        }
        return membreOpt.get();
    }

    /**
     * Récupère tous les membres.
     */
    public List<Membre> getAllMembres() throws SQLException {
        return membreDAO.findAll();
    }

    /**
     * Modifie les informations d'un membre.
     */
    public Membre modifierMembre(int id, Membre membreDetails) throws SQLException, ValidationException {
        Membre membre = getMembreById(id);
        validerMembre(membreDetails);
        
        // Vérifier l'unicité de l'email si modifié
        if (!membre.getEmail().equals(membreDetails.getEmail()) && 
            !isEmailUnique(membreDetails.getEmail(), id)) {
            throw new ValidationException("L'email est déjà utilisé par un autre membre");
        }
        
        membre.setNom(membreDetails.getNom());
        membre.setPrenom(membreDetails.getPrenom());
        membre.setEmail(membreDetails.getEmail());
        membre.setActif(membreDetails.isActif());
        
        return membreDAO.update(membre);
    }

    /**
     * Supprime un membre.
     */
    public void supprimerMembre(int id) throws SQLException , ValidationException  {
        Membre membre = getMembreById(id);
        
        // Vérifier si le membre a des emprunts en cours
        List<Emprunt> empruntsEnCours = membreDAO.findEmpruntsEnCoursByMembre(id);
        if (!empruntsEnCours.isEmpty()) {
            throw new RuntimeException("Impossible de supprimer le membre. Il a des emprunts en cours.");
        }
        
        membreDAO.delete(id);
    }

    /**
     * Active un membre.
     */
    public Membre activerMembre(int id) throws SQLException , ValidationException {
        Membre membre = getMembreById(id);
        membre.setActif(true);
        return membreDAO.update(membre);
    }

    /**
     * Désactive un membre.
     */
    public Membre desactiverMembre(int  id) throws SQLException , ValidationException {
        Membre membre = getMembreById(id);
        
        // Vérifier si le membre a des emprunts en cours
        List<Emprunt> empruntsEnCours = membreDAO.findEmpruntsEnCoursByMembre(id);
        if (!empruntsEnCours.isEmpty()) {
            throw new RuntimeException("Impossible de désactiver le membre. Il a des emprunts en cours.");
        }
        
        membre.setActif(false);
        return membreDAO.update(membre);
    }

    /**
     * Active ou désactive un membre.
     */
    public Membre activerDesactiverMembre(int id) throws SQLException , ValidationException {
        Membre membre = getMembreById(id);
        
        if (membre.isActif()) {
            // Vérifier si le membre a des emprunts en cours avant de désactiver
            List<Emprunt> empruntsEnCours = membreDAO.findEmpruntsEnCoursByMembre(id);
            if (!empruntsEnCours.isEmpty()) {
                throw new RuntimeException("Impossible de désactiver le membre. Il a des emprunts en cours.");
            }
            membre.setActif(false);
        } else {
            membre.setActif(true);
        }
        
        return membreDAO.update(membre);
    }

    /**
     * Recherche des membres par mot-clé.
     */
    public List<Membre> rechercherMembres(String keyword) throws SQLException {
        return membreDAO.rechercherMembres(keyword);
    }

    /**
     * Récupère tous les membres actifs.
     */
    public List<Membre> getMembresActifs() throws SQLException {
        return membreDAO.findActifs();
    }

    /**
     * Récupère l'historique des emprunts d'un membre.
     */
    public List<Emprunt> getHistoriqueEmprunts(int membreId) throws SQLException , ValidationException {
        getMembreById(membreId); // Vérifie que le membre existe
        return membreDAO.getHistoriqueEmprunts(membreId);
    }

    /**
     * Vérifie si un email est unique.
     */
    public boolean isEmailUnique(String email, int membreId) throws SQLException {
        Optional<Membre> membres = membreDAO.findByEmail(email);
        
        if (membres.isEmpty()) {
            return true;
        }
        
        if (membreId != 0) {
            return membres.get().getId() == membreId;
        }
        
        return false;
    }

    /**
     * Vérifie si un membre est actif.
     */
    public boolean isMembreActif(int membreId) throws SQLException , ValidationException {
        Membre membre = getMembreById(membreId);
        return membre.isActif();
    }

    /**
     * Valide les données d'un membre.
     */
    private void validerMembre(Membre membre) throws ValidationException {
        StringValidator.validateNotEmpty(membre.getNom(), "Nom");
        StringValidator.validateLength(membre.getNom(), "Nom", 2, 100);
        
        StringValidator.validateNotEmpty(membre.getPrenom(), "Prénom");
        StringValidator.validateLength(membre.getPrenom(), "Prénom", 2, 100);
        
        StringValidator.validateNotEmpty(membre.getEmail(), "Email");
        StringValidator.validateEmail(membre.getEmail());
        
    }

    /**
     * Recherche des membres par nom.
     */
    public List<Membre> rechercherMembresParNom(String nom) throws SQLException, ValidationException {
        StringValidator.validateNotEmpty(nom, "Nom");
        return membreDAO.findByNom(nom);
    }

}
