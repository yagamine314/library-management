package com.library.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.library.dao.MembreDAO;
import com.library.model.Emprunt;
import com.library.model.Membre;
import com.library.util.DatabaseConnection;

public class MembreDAOImpl implements MembreDAO {
    
    private Connection connection;
    
    public MembreDAOImpl() throws SQLException{
        this.connection= DatabaseConnection.getInstance().getConnection();
    }
    
    private Membre extractMembreFromResultSet(ResultSet rs) throws SQLException {
        Membre membre = new Membre();
        membre.setId(rs.getInt("id")); // IMPORTANT : Récupérer l'ID
        membre.setNom(rs.getString("nom"));
        membre.setPrenom(rs.getString("prenom"));
        membre.setEmail(rs.getString("email"));
        membre.setActif(rs.getBoolean("actif"));
        
        return membre;
    }

    @Override
    public Membre save(Membre membre) {
        String sql = "INSERT INTO membres (nom, prenom, email, actif) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, membre.getNom());
            stmt.setString(2, membre.getPrenom());
            stmt.setString(3, membre.getEmail());
            stmt.setBoolean(4, membre.isActif());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        membre.setId(generatedKeys.getInt(1));
                    }
                }
            }
        return membre;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'ajout du membre", e);
        }
    }

    @Override
    public Optional<Membre> findById(int id) {
        String sql = "SELECT * FROM membres WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(extractMembreFromResultSet(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche du membre", e);
        }
    }

    @Override
    public List<Membre> findAll() {
        String sql = "SELECT * FROM membres ORDER BY nom, prenom";
        List<Membre> membres = new ArrayList<>();
        
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                membres.add(extractMembreFromResultSet(rs));
            }
            return membres;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des membres", e);
        }
    }

    @Override
    public Membre update(Membre membre) {
        String sql = "UPDATE membres SET nom = ?, prenom = ?, email = ?, actif = ? WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, membre.getNom());
            stmt.setString(2, membre.getPrenom());
            stmt.setString(3, membre.getEmail());
            stmt.setBoolean(4, membre.isActif());
            stmt.setInt(5, membre.getId()); // Ajout de l'ID pour la clause WHERE
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new RuntimeException("Aucun membre trouvé avec l'ID: " + membre.getId());
            }
        return membre;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la modification du membre", e);
        }
    }

    @Override
    public Membre delete(int id) {
        Optional<Membre> membreOpt = findById(id);
        if (!membreOpt.isPresent()) {
            throw new RuntimeException("Aucun membre trouvé avec l'ID: " + id);
        }
        Membre membre = membreOpt.get();
        String sql = "DELETE FROM membres WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new RuntimeException("Aucun membre trouvé avec l'ID: " + id);
            }
            return membre;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression du membre"+ e.getMessage(), e);
        }
    }

    @Override
    public Optional<Membre> findByEmail(String email) {
        String sql = "SELECT * FROM membres WHERE email = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(extractMembreFromResultSet(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche du membre par email", e);
        }
    }

    @Override
    public List<Membre> findActifs() {
        String sql = "SELECT * FROM membres WHERE actif = true ORDER BY nom, prenom";
        List<Membre> membres = new ArrayList<>();
        
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                membres.add(extractMembreFromResultSet(rs));
            }
            return membres;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des membres actifs", e);
        }
    }
    @Override
    public List<Membre> findByNom(String nom) {
        String sql = "SELECT * FROM membres WHERE nom LIKE ? ORDER BY nom, prenom";
        List<Membre> membres = new ArrayList<>();
        
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, "%" + nom + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    membres.add(mapResultSetToMembre(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche par nom: " + e.getMessage(), e);
        }
        return membres;
    }
    private Emprunt mapResultSetToEmprunt(ResultSet rs) throws SQLException {
    Emprunt emprunt = new Emprunt();
    emprunt.setId(rs.getInt("id"));
    emprunt.setIsbnLivre(String.valueOf((rs.getInt("livre_id"))));
    emprunt.setIdMembre(rs.getInt("membre_id"));
    emprunt.setDateEmprunt(rs.getDate("date_emprunt"));
    
    // Handle nullable dates
    Date dateRetourPrevu = rs.getDate("date_retour_prevu");
    if (dateRetourPrevu != null) {
        emprunt.setDateRetourPrevue(dateRetourPrevu);
    }
    
    Date dateRetourEffectif = rs.getDate("date_retour_effectif");
    if (dateRetourEffectif != null) {
        emprunt.setDateRetourEffective(dateRetourEffectif);
    }
    
    return emprunt;
}
    @Override
    public List<Emprunt> findEmpruntsEnCoursByMembre(int membreId) {
        String sql = "SELECT e.* FROM emprunts e " +
                    "WHERE e.membre_id = ? AND e.date_retour_effectif IS NULL";
        
        List<Emprunt> emprunts = new ArrayList<>();
        
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, membreId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    emprunts.add(mapResultSetToEmprunt(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche des emprunts en cours: " + e.getMessage(), e);
        }
        return emprunts;
    }
    
    @Override
    public List<Emprunt> getHistoriqueEmprunts(int membreId) {
        String sql = "SELECT e.*, l.titre as livre_titre FROM emprunts e " +
                    "JOIN livres l ON e.livre_id = l.id " +
                    "WHERE e.membre_id = ? " +
                    "ORDER BY e.date_emprunt DESC";
        
        List<Emprunt> emprunts = new ArrayList<>();
        
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, membreId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    emprunts.add(mapResultSetToEmprunt(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération de l'historique: " + e.getMessage(), e);
        }
        return emprunts;
    }
    
    @Override
    public List<Membre> rechercherMembres(String keyword) {
        String sql = "SELECT * FROM membres WHERE " +
                    "LOWER(nom) LIKE LOWER(?) OR " +
                    "LOWER(prenom) LIKE LOWER(?) OR " +
                    "LOWER(email) LIKE LOWER(?) OR " +
                    "telephone LIKE ? " +
                    "ORDER BY nom, prenom";
        
        List<Membre> membres = new ArrayList<>();
        
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            String searchTerm = "%" + keyword + "%";
            stmt.setString(1, searchTerm);
            stmt.setString(2, searchTerm);
            stmt.setString(3, searchTerm);
            stmt.setString(4, searchTerm);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    membres.add(mapResultSetToMembre(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche: " + e.getMessage(), e);
        }
        return membres;
    }
    
    // votre méthode mapResultSetToMembre existante
    private Membre mapResultSetToMembre(ResultSet rs) throws SQLException {
        Membre membre = new Membre();
        membre.setId(rs.getInt("id"));
        membre.setNom(rs.getString("nom"));
        membre.setPrenom(rs.getString("prenom"));
        membre.setEmail(rs.getString("email"));
        membre.setActif(rs.getBoolean("actif"));
        return membre;
    }
    
    private Connection getConnection() {
        // Votre logique de connexion à la base de données
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/bibliotheque",
                    "username",
                    "password"
                );
            } catch (SQLException e) {
                throw new RuntimeException("Erreur de connexion à la base de données", e);
            }
        }
        return connection;
    }
}
