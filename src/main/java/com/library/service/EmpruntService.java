package com.library.service;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import com.library.dao.EmpruntDAO;
import com.library.dao.impl.EmpruntDAOImpl;
import com.library.exception.LimiteEmpruntDepasseeException;
import com.library.exception.LivreIndisponibleException;
import com.library.exception.MembreInactifException;
import com.library.model.Emprunt;

/**
 * Service métier pour la gestion des emprunts
 * Contient toute la logique métier liée aux emprunts
 */
public class EmpruntService {

    private final EmpruntDAO empruntDAO;

    public EmpruntService() {
        this.empruntDAO = new EmpruntDAOImpl();
    }

    /**
     * Effectue un emprunt de livre
     */
    public void emprunterLivre(String isbn, int membreId, Date dateRetourPrevue) throws SQLException,
            LivreIndisponibleException, MembreInactifException, LimiteEmpruntDepasseeException {

        // Vérifier si le livre existe
        if (!empruntDAO.livreExiste(isbn)) {
            throw new SQLException("Le livre avec l'ISBN " + isbn + " n'existe pas");
        }

        // Vérifier si le livre est disponible
        if (!empruntDAO.isLivreDisponible(isbn)) {
            throw new LivreIndisponibleException(isbn);
        }

        // Vérifier si le membre existe
        if (!empruntDAO.membreExiste(membreId)) {
            throw new SQLException("Le membre avec l'ID " + membreId + " n'existe pas");
        }

        // Vérifier si le membre est actif
        if (!empruntDAO.isMembreActif(membreId)) {
            throw new MembreInactifException(membreId);
        }

        // Vérifier la limite d'emprunts
        int empruntsEnCours = empruntDAO.countEmpruntsEnCoursByMembre(membreId);
        if (empruntsEnCours >= 3) {
            throw new LimiteEmpruntDepasseeException(membreId, empruntsEnCours);
        }

        Date dateEmprunt = Date.valueOf(LocalDate.now());
        Emprunt emprunt = new Emprunt(isbn, membreId, dateEmprunt, dateRetourPrevue);

        empruntDAO.save(emprunt);

        // Marquer le livre comme indisponible
        empruntDAO.marquerLivreIndisponible(isbn);
    }

    /**
     * Enregistre le retour d'un livre
     */
    public BigDecimal retournerLivre(int empruntId) throws SQLException {
        Date dateRetourEffective = Date.valueOf(LocalDate.now());

        // Récupérer l'emprunt pour calculer la pénalité
        Emprunt emprunt = empruntDAO.findById(empruntId);
        if (emprunt == null) {
            throw new SQLException("Emprunt non trouvé");
        }

        // Calculer la pénalité si retour en retard
        BigDecimal penalite = calculerPenalite(emprunt.getDateRetourPrevue(), dateRetourEffective);
        emprunt.setPenalite(penalite);
        emprunt.setDateRetourEffective(dateRetourEffective);

        // Mettre à jour l'emprunt
        empruntDAO.update(emprunt);

        // Marquer le livre comme disponible
        empruntDAO.marquerLivreDisponible(emprunt.getIsbnLivre());

        // Enregistrer le retour de l'emprunt
        empruntDAO.retournerLivre(empruntId, dateRetourEffective);

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
    public List<Emprunt> getEmpruntsByLivre(String isbn) throws SQLException {
        return empruntDAO.findByLivreIsbn(isbn);
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
     * Calcule la pénalité pour un retour en retard
     * Règle: 5€ par jour de retard
     */
    private BigDecimal calculerPenalite(Date dateRetourPrevue, Date dateRetourEffective) {
        if (dateRetourEffective.after(dateRetourPrevue)) {
            long joursRetard = ChronoUnit.DAYS.between(
                dateRetourPrevue.toLocalDate(),
                dateRetourEffective.toLocalDate()
            );
            return BigDecimal.valueOf(joursRetard * 5.0);
        }
        return BigDecimal.ZERO;
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
                             - Emprunts termin\u00e9s: %d
                             """,
            totalEmprunts, empruntsEnCoursCount, empruntsTermines, tauxRetourATemps
        );
    }
}
