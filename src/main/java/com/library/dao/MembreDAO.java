// Source code is decompiled from a .class file using FernFlower decompiler (from Intellij IDEA).
package com.library.dao;

import java.util.List;
import java.util.Optional;

import com.library.model.Emprunt;
import com.library.model.Membre;

public interface MembreDAO {
   Membre save(Membre var1);

   Optional<Membre> findById(int var1);

   List<Membre> findAll();

   Membre update(Membre var1);

   Membre delete(int var1);

   Optional<Membre> findByEmail(String var1);

   List<Membre> findActifs();
   List<Membre> findByNom(String nom);
   List<Membre> rechercherMembres(String keyword);
   List<Emprunt> findEmpruntsEnCoursByMembre(int membreId);
   List<Emprunt> getHistoriqueEmprunts(int membreId);
}
