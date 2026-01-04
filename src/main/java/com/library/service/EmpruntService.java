package com.library.service;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import com.library.dao.EmpruntDAO;
import com.library.dao.impl.EmpruntDAOImpl;
import com.library.exception.EmpruntNotFoundException;
import com.library.exception.LimiteEmpruntDepasseeException;
import com.library.exception.LivreIndisponibleException;
import com.library.exception.MembreInactifException;
import com.library.exception.ValidationException;
import com.library.model.Emprunt;
import com.library.model.Livre;
import com.library.util.DateUtils;

/**
 * Service métier pour la gestion des emprunts
 * Contient toute la logique métier liée aux emprunts
 */
public class EmpruntService {

    private static final int LIMITE_EMPRUNTS = 3;
    private static final double PENALITE_PAR_JOUR = 5.0;

    private final EmpruntDAO empruntDAO;
    private final BibliothequeService bibliothequeService;

    public EmpruntService() throws SQLException {
        this.empruntDAO = new EmpruntDAOImpl();
        this.bibliothequeService = new BibliothequeService();
    }

    /**
     * Effectue un emprunt de livre
     */
    public void emprunterLivre(String isbn, int membreId, Date dateRetourPrevue) throws SQLException,
            LivreIndisponibleException, MembreInactifException, LimiteEmpruntDepasseeException {

        // Vérifier si le livre existe et récupérer ses infos
        Livre livre;
        try {
            livre = bibliothequeService.trouverLivreParIsbn(isbn);
        } catch (ValidationException e) {
            throw new LivreIndisponibleException(isbn, "Aucun livre trouvé avec l'ISBN " + isbn + ".");
        }

        // Vérifier si le livre est disponible
        if (!livre.isDisponible()) {
            throw new LivreIndisponibleException(isbn);
        }

        // Vérifier si le membre existe et est actif
        try {
            if (!bibliothequeService.isMembreActif(membreId)) {
                throw new MembreInactifException(membreId);
            }
        } catch (ValidationException e) {
            throw new MembreInactifException(membreId, "Aucun membre trouvé avec l'ID " + membreId + ".");
        }

        // Vérifier la limite d'emprunts
        int empruntsEnCours = empruntDAO.countEmpruntsEnCoursByMembre(membreId);
        if (empruntsEnCours >= LIMITE_EMPRUNTS) {
            throw new LimiteEmpruntDepasseeException(membreId, empruntsEnCours);
        }

        Date dateEmprunt = Date.valueOf(LocalDate.now());

        // Utiliser la méthode transactionnelle
        empruntDAO.emprunterLivreTransactional(livre.getId(), membreId, dateEmprunt, dateRetourPrevue);
    }

    /**
     * Enregistre le retour d'un livre
     */
    public BigDecimal retournerLivre(int empruntId) throws EmpruntNotFoundException, SQLException {
        Date dateRetourEffective = Date.valueOf(LocalDate.now());

        // Récupérer l'emprunt pour calculer la pénalité
        Emprunt emprunt = empruntDAO.findById(empruntId);
        if (emprunt == null) {
            throw new EmpruntNotFoundException(empruntId);
        }

        // Calculer la pénalité si retour en retard
        BigDecimal penalite = DateUtils.calculerPenaliteBigDecimal(
            emprunt.getDateRetourPrevue().toLocalDate(),
            dateRetourEffective.toLocalDate()
        );

        // Utiliser la méthode transactionnelle
        empruntDAO.retournerEmpruntTransactional(empruntId, dateRetourEffective, penalite);

        return penalite;
    }

    /**
     * Recherche un emprunt par ID
     */
    public Emprunt getEmpruntById(int id) throws SQLException {
        return empruntDAO.findById(id);
    }

    /**
     * Récupère tous les emprunts
     */
    public List<Emprunt> getAllEmprunts() throws SQLException {
        return empruntDAO.findAll();
    }

    /**
     * Récupère les emprunts d'un membre
     */
    public List<Emprunt> getEmpruntsByMembre(int membreId) throws SQLException {
        return empruntDAO.findByMembreId(membreId);
    }

    /**
     * Récupère les emprunts d'un livre
     */
    public List<Emprunt> getEmpruntsByLivre(String idLivre) throws SQLException {
        return empruntDAO.findByLivreId(idLivre);
    }

    /**
     * Récupère les emprunts en cours
     */
    public List<Emprunt> getEmpruntsEnCours() throws SQLException {
        return empruntDAO.findEmpruntsEnCours();
    }

    /**
     * Récupère les emprunts en retard
     */
    public List<Emprunt> getEmpruntsEnRetard() throws SQLException {
        List<Emprunt> empruntsEnCours = empruntDAO.findEmpruntsEnCours();
        Date aujourdHui = Date.valueOf(LocalDate.now());

        return empruntsEnCours.stream()
                .filter(emprunt -> emprunt.getDateRetourPrevue().before(aujourdHui))
                .toList();
    }

    /**
     * Met à jour un emprunt
     */
    public void updateEmprunt(Emprunt emprunt) throws SQLException {
        empruntDAO.update(emprunt);
    }

    /**
     * Supprime un emprunt
     */
    public void deleteEmprunt(int id) throws SQLException {
        empruntDAO.delete(id);
    }

    /**
     * Vérifie si un membre peut emprunter
     */
    public boolean peutEmprunter(int membreId) throws SQLException {
        int empruntsEnCours = empruntDAO.countEmpruntsEnCoursByMembre(membreId);
        return empruntsEnCours < 3; // Limite de 3 emprunts
    }

    /**
     * Calcule le nombre d'emprunts en cours pour un membre
     */
    public int getNombreEmpruntsEnCours(int membreId) throws SQLException {
        return empruntDAO.countEmpruntsEnCoursByMembre(membreId);
    }

    /**
     * Génère des statistiques sur les emprunts
     */
    public String genererStatistiques() throws SQLException {
        List<Emprunt> tousEmprunts = empruntDAO.findAll();
        List<Emprunt> empruntsEnCours = empruntDAO.findEmpruntsEnCours();

        int totalEmprunts = tousEmprunts.size();
        int empruntsEnCoursCount = empruntsEnCours.size();
        int empruntsTermines = totalEmprunts - empruntsEnCoursCount;

        // Calculer le taux de retour à temps
        long retoursATemps = tousEmprunts.stream()
                .filter(e -> e.getDateRetourEffective() != null)
                .filter(e -> !e.getDateRetourEffective().after(e.getDateRetourPrevue()))
                .count();

        long totalRetours = tousEmprunts.stream()
                .mapToLong(e -> e.getDateRetourEffective() != null ? 1 : 0)
                .sum();

        double tauxRetourATemps = totalRetours > 0 ? (double) retoursATemps / totalRetours * 100 : 0;

        return String.format("""
                             Statistiques des emprunts:
                             - Total emprunts: %d
                             - Emprunts en cours: %d
                             - Emprunts terminés: %d
                             - Taux de retour à temps: %.2f%%
                             """,
            totalEmprunts, empruntsEnCoursCount, empruntsTermines, tauxRetourATemps
        );
    }
}
