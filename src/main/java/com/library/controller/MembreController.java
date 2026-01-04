package com.library.controller;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import com.library.exception.ValidationException;
import com.library.model.Emprunt;
import com.library.model.Membre;
import com.library.service.BibliothequeService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class MembreController {
    
    @FXML private TableView<Membre> membresTable;
    @FXML private TableColumn<Membre, Integer> idCol;
    @FXML private TableColumn<Membre, String> nomCol;
    @FXML private TableColumn<Membre, String> prenomCol;
    @FXML private TableColumn<Membre, String> emailCol;
    @FXML private TableColumn<Membre, String> statutCol;
    
    @FXML private TextField idField;
    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField emailField;
    @FXML private TextField telephoneField;
    @FXML private DatePicker dateNaissancePicker;
    @FXML private DatePicker dateAdhesionPicker;
    @FXML private CheckBox actifCheckBox;
    
    @FXML private TableView<Emprunt> historiqueTable;
    @FXML private TableColumn<Emprunt, String> livreCol;
    @FXML private TableColumn<Emprunt, LocalDate> dateEmpruntCol;
    @FXML private TableColumn<Emprunt, LocalDate> dateRetourCol;
    @FXML private TableColumn<Emprunt, String> statutEmpruntCol;
    
    @FXML private TextField searchField;
    
    private BibliothequeService bibliothequeService;
    private ObservableList<Membre> membresList = FXCollections.observableArrayList();
    private ObservableList<Emprunt> historiqueList = FXCollections.observableArrayList();
    
    public void setBibliothequeService(BibliothequeService service) throws SQLException{
        this.bibliothequeService = service;
        loadMembres();
    }
    
    @FXML
    private void initialize() {
        // Configuration de la table des membres
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomCol.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        statutCol.setCellValueFactory(cellData -> {
            Membre membre = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(
                membre.isActif() ? "Actif" : "Inactif"
            );
        });
        
        // Configuration de la table d'historique
        livreCol.setCellValueFactory(new PropertyValueFactory<>("livreTitre"));
        dateEmpruntCol.setCellValueFactory(new PropertyValueFactory<>("dateEmprunt"));
        dateRetourCol.setCellValueFactory(new PropertyValueFactory<>("dateRetourPrevue"));
        statutEmpruntCol.setCellValueFactory(new PropertyValueFactory<>("statut"));
        
        
        // Initialiser la date d'adhésion à aujourd'hui
        dateAdhesionPicker.setValue(LocalDate.now());
    }
    
    private void loadMembres() throws SQLException {
        BibliothequeService bibliothequeService = new BibliothequeService();
        List<Membre> membres = bibliothequeService.getAllMembres();
        membresList.setAll(membres);
        membresTable.setItems(membresList);
    }
    
    private void showMembreDetails(Membre membre) throws ValidationException , SQLException{
        if (membre != null) {
            idField.setText(String.valueOf(membre.getId()));
            nomField.setText(membre.getNom());
            prenomField.setText(membre.getPrenom());
            emailField.setText(membre.getEmail());
            actifCheckBox.setSelected(membre.isActif());
            
            // Charger l'historique des emprunts
            loadHistorique(membre.getId());
        }
    }
    
    private void loadHistorique(int membreId) throws SQLException , ValidationException {
        BibliothequeService bibliothequeService = new BibliothequeService();
        List<Emprunt> emprunts = bibliothequeService.getHistoriqueEmprunts(membreId);
        historiqueList.setAll(emprunts);
        historiqueTable.setItems(historiqueList);
    }
    
    @FXML
    private void handleAjouterMembre() {
        if (validateFields()) {
            try {
                Membre membre = new Membre();
                membre.setNom(nomField.getText());
                membre.setPrenom(prenomField.getText());
                membre.setEmail(emailField.getText());
                membre.setActif(actifCheckBox.isSelected());
                
                bibliothequeService.ajouterMembre(membre);
                loadMembres();
                clearFields();
                showAlert("Succès", "Membre ajouté avec succès.", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                showAlert("Erreur", e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }
    
    @FXML
    private void handleModifierMembre() {
        Membre selectedMembre = membresTable.getSelectionModel().getSelectedItem();
        if (selectedMembre == null) {
            showAlert("Avertissement", "Veuillez sélectionner un membre à modifier.", Alert.AlertType.WARNING);
            return;
        }
        
        if (validateFields()) {
            try {
                selectedMembre.setNom(nomField.getText());
                selectedMembre.setPrenom(prenomField.getText());
                selectedMembre.setEmail(emailField.getText());
                selectedMembre.setActif(actifCheckBox.isSelected());
                                // 1. Récupère les valeurs dont tu as besoin
                int membreId = selectedMembre.getId(); // ou autre variable
                Membre membreModifie = new Membre(); // méthode qui crée l'objet Membre

                // 2. Appelle la méthode avec ces valeurs
                bibliothequeService.modifierMembre(membreId, membreModifie);
                loadMembres();
                showAlert("Succès", "Membre modifié avec succès.", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                showAlert("Erreur", e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }
    
    @FXML
    private void handleSupprimerMembre() {
        Membre selectedMembre = membresTable.getSelectionModel().getSelectedItem();
        if (selectedMembre == null) {
            showAlert("Avertissement", "Veuillez sélectionner un membre à supprimer.", Alert.AlertType.WARNING);
            return;
        }
        
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText("Supprimer le membre");
        confirmation.setContentText("Êtes-vous sûr de vouloir supprimer " + selectedMembre.getPrenom() + " " + selectedMembre.getNom() + " ?");
        
        if (confirmation.showAndWait().get() == ButtonType.OK) {
            try {
                bibliothequeService.supprimerMembre(selectedMembre.getId());
                loadMembres();
                clearFields();
                showAlert("Succès", "Membre supprimé avec succès.", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                showAlert("Erreur", e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }
    
    @FXML
    private void handleActiverDesactiver() {
        Membre selectedMembre = membresTable.getSelectionModel().getSelectedItem();
        if (selectedMembre == null) {
            showAlert("Avertissement", "Veuillez sélectionner un membre.", Alert.AlertType.WARNING);
            return;
        }
        
        String action = selectedMembre.isActif() ? "désactiver" : "activer";
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation");
        confirmation.setHeaderText(action.substring(0, 1).toUpperCase() + action.substring(1) + " le membre");
        confirmation.setContentText("Êtes-vous sûr de vouloir " + action + " " + selectedMembre.getPrenom() + " " + selectedMembre.getNom() + " ?");
        
        if (confirmation.showAndWait().get() == ButtonType.OK) {
            try {
                bibliothequeService.activerDesactiverMembre(selectedMembre.getId());
                loadMembres();
                showAlert("Succès", "Membre " + action + " avec succès.", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                showAlert("Erreur", e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }
    
    @FXML
    private void handleRechercher() throws SQLException, ValidationException{
        String searchText = searchField.getText().trim();
        if (searchText.isEmpty())  {
            loadMembres();
        } else  {
            List<Membre> membres = bibliothequeService.rechercherMembresParNom( searchText);
            membresList.setAll(membres);
            membresTable.setItems(membresList);
        }
    }
    
    @FXML
    private void handleNouveau() {
        clearFields();
        membresTable.getSelectionModel().clearSelection();
        historiqueList.clear();
    }
    
    @FXML
    private void handleAfficherActifs() throws SQLException {
        List<Membre> membresActifs = bibliothequeService.getMembresActifs();
        membresList.setAll(membresActifs);
        membresTable.setItems(membresList);
    }
    
    private boolean validateFields() {
        StringBuilder errors = new StringBuilder();
        
        if (nomField.getText().trim().isEmpty()) {
            errors.append("Le nom est obligatoire.\n");
        }
        if (prenomField.getText().trim().isEmpty()) {
            errors.append("Le prénom est obligatoire.\n");
        }
        if (emailField.getText().trim().isEmpty()) {
            errors.append("L'email est obligatoire.\n");
        } else if (!emailField.getText().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            errors.append("Format d'email invalide.\n");
        }
        if (dateAdhesionPicker.getValue() == null) {
            errors.append("La date d'adhésion est obligatoire.\n");
        }
        
        if (errors.length() > 0) {
            showAlert("Validation", errors.toString(), Alert.AlertType.ERROR);
            return false;
        }
        
        return true;
    }
    
    private void clearFields() {
        idField.clear();
        nomField.clear();
        prenomField.clear();
        emailField.clear();
        telephoneField.clear();
        dateNaissancePicker.setValue(null);
        dateAdhesionPicker.setValue(LocalDate.now());
        actifCheckBox.setSelected(true);
        searchField.clear();
    }
    
    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
