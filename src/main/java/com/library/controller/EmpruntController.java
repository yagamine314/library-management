package com.library.controller;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import com.library.exception.EmpruntNotFoundException;
import com.library.exception.LimiteEmpruntDepasseeException;
import com.library.exception.LivreIndisponibleException;
import com.library.exception.MembreInactifException;
import com.library.model.Emprunt;
import com.library.service.EmpruntService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Contrôleur JavaFX pour la gestion des emprunts.
 */
public class EmpruntController {

    @FXML
    private TextField isbnField;
    @FXML
    private TextField membreIdField;
    @FXML
    private DatePicker dateRetourPrevuePicker;
    @FXML
    private TextField empruntIdRetourField;
    @FXML
    private Button empruntButton;
    @FXML
    private Button retourButton;
    @FXML
    private Button refreshButton;
    @FXML
    private TableView<Emprunt> empruntsTable;
    @FXML
    private TableColumn<Emprunt, Integer> colId;
    @FXML
    private TableColumn<Emprunt, String> colIsbnLivre;
    @FXML
    private TableColumn<Emprunt, Integer> colIdMembre;
    @FXML
    private TableColumn<Emprunt, Date> colDateEmprunt;
    @FXML
    private TableColumn<Emprunt, Date> colDateRetourPrevue;
    @FXML
    private TableColumn<Emprunt, Date> colDateRetourEffective;
    @FXML
    private TableColumn<Emprunt, BigDecimal> colPenalite;
    @FXML
    private Label statusLabel;

    private EmpruntService empruntService;
    private final ObservableList<Emprunt> empruntsList;

    /**
     * Constructeur par défaut.
     */
    public EmpruntController() {
        try {
            this.empruntService = new EmpruntService();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'initialisation du service d'emprunt", e);
        }
        this.empruntsList = FXCollections.observableArrayList();
    }

    /**
     * Constructeur avec injection de dépendance.
     */
    public EmpruntController(EmpruntService empruntService) {
        this.empruntService = empruntService;
        this.empruntsList = FXCollections.observableArrayList();
    }

    /**
     * Méthode d'initialisation appelée après le chargement du FXML.
     */
    @FXML
    public void initialize() {
        // Configuration des colonnes de la table
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colIsbnLivre.setCellValueFactory(new PropertyValueFactory<>("idLivre"));
        colIdMembre.setCellValueFactory(new PropertyValueFactory<>("idMembre"));
        colDateEmprunt.setCellValueFactory(new PropertyValueFactory<>("dateEmprunt"));
        colDateRetourPrevue.setCellValueFactory(new PropertyValueFactory<>("dateRetourPrevue"));
        colDateRetourEffective.setCellValueFactory(new PropertyValueFactory<>("dateRetourEffective"));
        colPenalite.setCellValueFactory(new PropertyValueFactory<>("penalite"));

        // Liaison de la liste observable à la table
        empruntsTable.setItems(empruntsList);

        // Chargement initial des emprunts
        handleRefresh();
    }

    /**
     * Gère l'emprunt d'un livre.
     */
    @FXML
    private void handleEmprunter() {
        try {
            String isbn = isbnField.getText().trim();
            String membreIdStr = membreIdField.getText().trim();
            LocalDate dateRetourPrevue = dateRetourPrevuePicker.getValue();

            if (isbn.isEmpty() || membreIdStr.isEmpty() || dateRetourPrevue == null) {
                afficherMessage("Veuillez remplir tous les champs", Alert.AlertType.WARNING);
                return;
            }

            int membreId = Integer.parseInt(membreIdStr);
            if (membreId <= 0) {
                afficherMessage("L'ID du membre doit être un nombre positif", Alert.AlertType.ERROR);
                return;
            }

            // Basic ISBN validation (should be 10 or 13 digits)
            if (!isbn.matches("\\d{10}|\\d{13}")) {
                afficherMessage("L'ISBN doit contenir 10 ou 13 chiffres", Alert.AlertType.ERROR);
                return;
            }
            Date dateRetour = Date.valueOf(dateRetourPrevue);

            empruntService.emprunterLivre(isbn, membreId, dateRetour);
            handleRefresh();
            viderFormulaireEmprunt();
            afficherMessage("Emprunt effectué avec succès", Alert.AlertType.INFORMATION);

        } catch (NumberFormatException e) {
            afficherMessage("L'ID du membre doit être un nombre valide", Alert.AlertType.ERROR);
        } catch (LivreIndisponibleException | MembreInactifException | LimiteEmpruntDepasseeException | SQLException e) {
            afficherMessage("Erreur lors de l'emprunt: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * Gère le retour d'un livre.
     */
    @FXML
    private void handleRetourner() {
        try {
            String empruntIdStr = empruntIdRetourField.getText().trim();

            if (empruntIdStr.isEmpty()) {
                afficherMessage("Veuillez saisir l'ID de l'emprunt", Alert.AlertType.WARNING);
                return;
            }

            int empruntId = Integer.parseInt(empruntIdStr);
            BigDecimal penalite = empruntService.retournerLivre(empruntId);

            handleRefresh();
            empruntIdRetourField.clear();

            String message = "Retour effectué avec succès";
            if (penalite.compareTo(BigDecimal.ZERO) > 0) {
                message += ". Pénalité: " + penalite + "€";
            }
            afficherMessage(message, Alert.AlertType.INFORMATION);

        } catch (NumberFormatException e) {
            afficherMessage("L'ID de l'emprunt doit être un nombre valide", Alert.AlertType.ERROR);
        } catch (EmpruntNotFoundException | SQLException e) {
            afficherMessage("Erreur lors du retour: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * Actualise la liste des emprunts.
     */
    @FXML
    private void handleRefresh() {
        try {
            List<Emprunt> emprunts = empruntService.getAllEmprunts();
            empruntsList.clear();
            empruntsList.addAll(emprunts);
            statusLabel.setText("Liste actualisée - " + emprunts.size() + " emprunt(s)");
        } catch (SQLException e) {
            afficherMessage("Erreur lors du chargement des emprunts: " + e.getMessage(), Alert.AlertType.ERROR);
            statusLabel.setText("Erreur de chargement");
        }
    }

    /**
     * Vide le formulaire d'emprunt.
     */
    private void viderFormulaireEmprunt() {
        isbnField.clear();
        membreIdField.clear();
        dateRetourPrevuePicker.setValue(null);
    }

    /**
     * Affiche un message à l'utilisateur.
     */
    private void afficherMessage(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
