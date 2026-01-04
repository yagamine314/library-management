// Source code is decompiled from a .class file using FernFlower decompiler (from Intellij IDEA).
package com.library.model;

public class Membre {
   private int id;
   private String nom;
   private String prenom;
   private String email;
   private boolean actif;

   public Membre() {
      this.actif = true;
   }

   public Membre(String nom, String prenom, String email) {
      this.nom = nom;
      this.prenom = prenom;
      this.email = email;
      this.actif = true;
   }

   public Membre(String nom, String prenom, String email, boolean actif) {
      this.nom = nom;
      this.prenom = prenom;
      this.email = email;
      this.actif = actif;
   }

   public int getId() {
      return this.id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public String getNom() {
      return this.nom;
   }

   public void setNom(String nom) {
      this.nom = nom;
   }

   public String getPrenom() {
      return this.prenom;
   }

   public void setPrenom(String prenom) {
      this.prenom = prenom;
   }

   public String getEmail() {
      return this.email;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public boolean isActif() {
      return this.actif;
   }

   public void setActif(boolean actif) {
      this.actif = actif;
   }

   public String toString() {
      String statut = this.actif ? "Actif" : "Inactif";
      return String.format("%s %s (%s) - %s", this.prenom, this.nom, this.email, statut);
   }

   public void toggleActif() {
      this.actif = !this.actif;
   }
}

