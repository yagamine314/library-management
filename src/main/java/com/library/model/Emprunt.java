package com.library.model;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * Classe modèle représentant un emprunt de livre
 */
public class Emprunt {
    private int id;
    private String isbnLivre;
    private int idMembre;
    private Date dateEmprunt;
    private Date dateRetourPrevue;
    private Date dateRetourEffective;
    private BigDecimal penalite;

    // Constructeurs
    public Emprunt() {}

    public Emprunt(String isbnLivre, int idMembre, Date dateEmprunt, Date dateRetourPrevue) {
        this.isbnLivre = isbnLivre;
        this.idMembre = idMembre;
        this.dateEmprunt = dateEmprunt;
        this.dateRetourPrevue = dateRetourPrevue;
        this.penalite = BigDecimal.ZERO;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIsbnLivre() {
        return isbnLivre;
    }

    public void setIsbnLivre(String isbnLivre) {
        this.isbnLivre = isbnLivre;
    }

    public int getIdMembre() {
        return idMembre;
    }

    public void setIdMembre(int idMembre) {
        this.idMembre = idMembre;
    }

    public Date getDateEmprunt() {
        return dateEmprunt;
    }

    public void setDateEmprunt(Date dateEmprunt) {
        this.dateEmprunt = dateEmprunt;
    }

    public Date getDateRetourPrevue() {
        return dateRetourPrevue;
    }

    public void setDateRetourPrevue(Date dateRetourPrevue) {
        this.dateRetourPrevue = dateRetourPrevue;
    }

    public Date getDateRetourEffective() {
        return dateRetourEffective;
    }

    public void setDateRetourEffective(Date dateRetourEffective) {
        this.dateRetourEffective = dateRetourEffective;
    }

    public BigDecimal getPenalite() {
        return penalite;
    }

    public void setPenalite(BigDecimal penalite) {
        this.penalite = penalite;
    }

    @Override
    public String toString() {
        return "Emprunt{" +
                "id=" + id +
                ", isbnLivre='" + isbnLivre + '\'' +
                ", idMembre=" + idMembre +
                ", dateEmprunt=" + dateEmprunt +
                ", dateRetourPrevue=" + dateRetourPrevue +
                ", dateRetourEffective=" + dateRetourEffective +
                ", penalite=" + penalite +
                '}';
    }
}
