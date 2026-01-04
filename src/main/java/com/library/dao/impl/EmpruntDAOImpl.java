package com.library.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.library.dao.EmpruntDAO;
import com.library.model.Emprunt;
import com.library.util.DatabaseConnection;

/**
 * Implémentation de l'interface EmpruntDAO
 */
public class EmpruntDAOImpl implements EmpruntDAO {

    @Override
    public void save(Emprunt emprunt) throws SQLException {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        conn.setAutoCommit(false);

        try {
            // Insérer l'emprunt
            String sql = "INSERT INTO emprunts (id_livre, id_membre, date_emprunt, date_retour_prevue, penalite) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, emprunt.getIdLivre());
                stmt.setInt(2, emprunt.getIdMembre());
                stmt.setDate(3, emprunt.getDateEmprunt());
                stmt.setDate(4, emprunt.getDateRetourPrevue());
                stmt.setBigDecimal(5, emprunt.getPenalite());

                stmt.executeUpdate();

                // Récupérer l'ID généré
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        emprunt.setId(rs.getInt(1));
                    }
                }
            }

            conn.commit();

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    @Override
    public Emprunt findById(int id) throws SQLException {
        String sql = "SELECT * FROM emprunts WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEmprunt(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Emprunt> findAll() throws SQLException {
        List<Emprunt> emprunts = new ArrayList<>();
        String sql = "SELECT * FROM emprunts ORDER BY date_emprunt DESC";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                emprunts.add(mapResultSetToEmprunt(rs));
            }
        }
        return emprunts;
    }

    @Override
    public List<Emprunt> findByMembreId(int membreId) throws SQLException {
        List<Emprunt> emprunts = new ArrayList<>();
        String sql = "SELECT * FROM emprunts WHERE id_membre = ? ORDER BY date_emprunt DESC";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, membreId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    emprunts.add(mapResultSetToEmprunt(rs));
                }
            }
        }
        return emprunts;
    }

    @Override
    public List<Emprunt> findByLivreId(String id) throws SQLException {
        List<Emprunt> emprunts = new ArrayList<>();
        String sql = "SELECT * FROM emprunts WHERE id_livre = ? ORDER BY date_emprunt DESC";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    emprunts.add(mapResultSetToEmprunt(rs));
                }
            }
        }
        return emprunts;
    }

    @Override
    public List<Emprunt> findEmpruntsEnCours() throws SQLException {
        List<Emprunt> emprunts = new ArrayList<>();
        String sql = "SELECT * FROM emprunts WHERE date_retour_effective IS NULL ORDER BY date_emprunt DESC";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                emprunts.add(mapResultSetToEmprunt(rs));
            }
        }
        return emprunts;
    }

    @Override
    public void update(Emprunt emprunt) throws SQLException {
        String sql = "UPDATE emprunts SET id_livre = ?, id_membre = ?, date_emprunt = ?, " +
                     "date_retour_prevue = ?, date_retour_effective = ?, penalite = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, emprunt.getIdLivre());
            stmt.setInt(2, emprunt.getIdMembre());
            stmt.setDate(3, emprunt.getDateEmprunt());
            stmt.setDate(4, emprunt.getDateRetourPrevue());
            stmt.setDate(5, emprunt.getDateRetourEffective());
            stmt.setBigDecimal(6, emprunt.getPenalite());
            stmt.setInt(7, emprunt.getId());

            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM emprunts WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public void retournerLivre(int empruntId, Date dateRetourEffective) throws SQLException {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        conn.setAutoCommit(false);

        try {
            // Récupérer l'emprunt pour obtenir l'ISBN
            Emprunt emprunt = findById(empruntId);
            if (emprunt == null) {
                throw new SQLException("Emprunt non trouvé");
            }

            // Mettre à jour la date de retour effective
            String sql = "UPDATE emprunts SET date_retour_effective = ? WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setDate(1, dateRetourEffective);
                stmt.setInt(2, empruntId);
                stmt.executeUpdate();
            }

            conn.commit();

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    @Override
    public int countEmpruntsEnCoursByMembre(int membreId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM emprunts WHERE id_membre = ? AND date_retour_effective IS NULL";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, membreId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    @Override
    public boolean livreExiste(String isbn) throws SQLException {
        String sql = "SELECT COUNT(*) FROM livres WHERE isbn = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, isbn);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    @Override
    public boolean membreExiste(int membreId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM membres WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, membreId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    @Override
    public boolean isLivreDisponible(String isbn) throws SQLException {
        String sql = "SELECT disponible FROM livres WHERE isbn = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, isbn);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean("disponible");
                }
            }
        }
        return false;
    }

    @Override
    public void marquerLivreIndisponible(String isbn) throws SQLException {
        String sql = "UPDATE livres SET disponible = FALSE WHERE isbn = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, isbn);
            stmt.executeUpdate();
        }
    }

    @Override
    public void marquerLivreDisponible(String isbn) throws SQLException {
        String sql = "UPDATE livres SET disponible = TRUE WHERE isbn = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, isbn);
            stmt.executeUpdate();
        }
    }

    @Override
    public boolean isMembreActif(int membreId) throws SQLException {
        String sql = "SELECT actif FROM membres WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, membreId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean("actif");
                }
            }
        }
        return false;
    }

    @Override
    public void emprunterLivreTransactional(String idLivre, int membreId, java.sql.Date dateEmprunt, java.sql.Date dateRetourPrevue) throws SQLException {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        conn.setAutoCommit(false);

        try {
            // Insérer l'emprunt
            String sqlEmprunt = "INSERT INTO emprunts (id_livre, id_membre, date_emprunt, date_retour_prevue, penalite) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sqlEmprunt)) {
                stmt.setString(1, idLivre);
                stmt.setInt(2, membreId);
                stmt.setDate(3, dateEmprunt);
                stmt.setDate(4, dateRetourPrevue);
                stmt.setBigDecimal(5, java.math.BigDecimal.ZERO);
                stmt.executeUpdate();
            }

            // Marquer le livre comme indisponible
            String sqlLivre = "UPDATE livres SET disponible = FALSE WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlLivre)) {
                stmt.setString(1, idLivre);
                stmt.executeUpdate();
            }

            conn.commit();

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    @Override
    public void retournerEmpruntTransactional(int empruntId, java.sql.Date dateRetourEffective, java.math.BigDecimal penalite) throws SQLException {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        conn.setAutoCommit(false);

        try {
            // Récupérer l'emprunt pour obtenir l'ISBN
            Emprunt emprunt = findById(empruntId);
            if (emprunt == null) {
                throw new SQLException("Emprunt non trouvé");
            }

            // Mettre à jour l'emprunt
            String sqlUpdate = "UPDATE emprunts SET date_retour_effective = ?, penalite = ? WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlUpdate)) {
                stmt.setDate(1, dateRetourEffective);
                stmt.setBigDecimal(2, penalite);
                stmt.setInt(3, empruntId);
                stmt.executeUpdate();
            }

            // Marquer le livre comme disponible
            String sqlLivre = "UPDATE livres SET disponible = TRUE WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlLivre)) {
                stmt.setString(1, emprunt.getIdLivre());
                stmt.executeUpdate();
            }

            conn.commit();

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    private Emprunt mapResultSetToEmprunt(ResultSet rs) throws SQLException {
        Emprunt emprunt = new Emprunt();
        emprunt.setId(rs.getInt("id"));
        emprunt.setIdLivre(rs.getString("id_livre"));
        emprunt.setIdMembre(rs.getInt("id_membre"));
        emprunt.setDateEmprunt(rs.getDate("date_emprunt"));
        emprunt.setDateRetourPrevue(rs.getDate("date_retour_prevue"));
        emprunt.setDateRetourEffective(rs.getDate("date_retour_effective"));
        emprunt.setPenalite(rs.getBigDecimal("penalite"));
        return emprunt;
    }
}
