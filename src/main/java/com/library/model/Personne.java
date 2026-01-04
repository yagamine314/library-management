// Source code is decompiled from a .class file using FernFlower decompiler (from Intellij IDEA).
package com.library.model;

public abstract class Personne {
   protected String nom;
   protected String prenom;
   protected String email;
   protected Boolean actif;

   public Personne() {
   }

   public Personne(String nom, String prenom, String email) {
      this.nom = nom;
      this.prenom = prenom;
      this.email = email;
   }

   public Personne(String nom, String prenom, String email, Boolean actif) {
      this.nom = nom;
      this.prenom = prenom;
      this.email = email;
      this.actif = actif;
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

   public Boolean actif() {
      return this.actif;
   }

   public String toString() {
      return this.prenom + " " + this.nom + " (" + this.email + ")";
   }
}

