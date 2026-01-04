package com.library.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Classe utilitaire pour manipuler les dates
 */
public class DateUtils {
    
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final int DUREE_EMPRUNT_JOURS = 14; // 2 semaines
    private static final double PENALITE_PAR_JOUR = 5.0; // 5 MAD par jour de retard
    
    /**
     * Calcule la date de retour prévue (aujourd'hui + 14 jours)
     */
    public static LocalDate calculerDateRetourPrevue() {
        return LocalDate.now().plusDays(DUREE_EMPRUNT_JOURS);
    }
    
    /**
     * Calcule la date de retour prévue à partir d'une date donnée
     */
    public static LocalDate calculerDateRetourPrevue(LocalDate dateEmprunt) {
        return dateEmprunt.plusDays(DUREE_EMPRUNT_JOURS);
    }
    
    /**
     * Calcule le nombre de jours de retard
     * @return nombre de jours (0 si pas de retard)
     */
    public static long calculerJoursRetard(LocalDate dateRetourPrevue) {
        long jours = ChronoUnit.DAYS.between(dateRetourPrevue, LocalDate.now());
        return Math.max(0, jours); // Pas de jours négatifs
    }
    
    /**
     * Calcule le nombre de jours de retard avec date effective
     */
    public static long calculerJoursRetard(LocalDate dateRetourPrevue, LocalDate dateRetourEffective) {
        long jours = ChronoUnit.DAYS.between(dateRetourPrevue, dateRetourEffective);
        return Math.max(0, jours);
    }
     
    /**
     * Calcule la pénalité avec date effective
     */
    public static double calculerPenalite(LocalDate dateRetourPrevue, LocalDate dateRetourEffective) {
        long joursRetard = calculerJoursRetard(dateRetourPrevue, dateRetourEffective);
        return joursRetard * PENALITE_PAR_JOUR;
    }

    /**
     * Calcule la pénalité avec date effective (version BigDecimal)
     */
    public static java.math.BigDecimal calculerPenaliteBigDecimal(LocalDate dateRetourPrevue, LocalDate dateRetourEffective) {
        long joursRetard = calculerJoursRetard(dateRetourPrevue, dateRetourEffective);
        return java.math.BigDecimal.valueOf(joursRetard * PENALITE_PAR_JOUR);
    }
    
    /**
     * Formate une date en String (dd/MM/yyyy)
     */
    public static String formaterDate(LocalDate date) {
        return date != null ? date.format(FORMATTER) : "";
    }
    
    /**
     * Parse un String en LocalDate
     */
    public static LocalDate parseDate(String dateStr) {
        try {
            return LocalDate.parse(dateStr, FORMATTER);
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Vérifie si une date est dans le passé
     */
    public static boolean estDansLePasse(LocalDate date) {
        return date.isBefore(LocalDate.now());
    }
    
    /**
     * Vérifie si une date est dans le futur
     */
    public static boolean estDansFutur(LocalDate date) {
        return date.isAfter(LocalDate.now());
    }
}