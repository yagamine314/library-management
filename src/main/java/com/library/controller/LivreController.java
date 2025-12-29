package com.library.controller;

import com.library.exception.LivreIndisponibleException;
import com.library.exception.ValidationException;
import com.model.Livre;
import com.service.BibliothequeService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.SQLException;
import java.util.List;

/**
 * Contrôleur JavaFX pour la gestion des livres.
 */
public class LivreController {

    @FXML
    private TextField isbnField;
    @FXML
    private TextField titreField;
    @FXML
    private TextField auteurField;
    @FXML
    private TextField anneeField;
    @FXML
    private CheckBox disponibleCheckBox;
    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<String> searchTypeComboBox;
    @FXML
    private TableView<Livre> livresTable;
    @FXML
    private TableColumn<Livre, String> isbnColumn;
    @FXML
    private TableColumn<Livre, String> titreColumn;
    @FXML
    private TableColumn<Livre, String> auteurColumn;
    @FXML
    private TableColumn<Livre, Integer> anneeColumn;
    @FXML
    private TableColumn<Livre, Boolean> disponibleColumn;
    @FXML
    private Button ajouterButton;
    @FXML
    private Button modifierButton;
    @FXML
    private Button supprimerButton;
    @FXML
    private Button emprunterButton;
    @FXML
    private Button retournerButton;
    @FXML
    private Button rechercherButton;
    @FXML
    private Button actualiserButton;
    @FXML
    private Label statusLabel;

    private final BibliothequeService bibliothequeService;
    private final ObservableList<Livre> livresList;

    /**
     * Constructeur par défaut.
     */
    public LivreController() {
        this.bibliothequeService = new BibliothequeService();
        this.livresList = FXCollections.observableArrayList();
    }

    /**
     * Constructeur avec injection de dépendance.
     */
    public LivreController(BibliothequeService bibliothequeService) {
        this.bibliothequeService = bibliothequeService;
        this.livresList = FXCollections.observableArrayList();
    }

    /**
     * Méthode d'initialisation appelée après le chargement du FXML.
     */
    @FXML
    public void initialize() {
        // Configuration des colonnes de la table
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        titreColumn.setCellValueFactory(new PropertyValueFactory<>("titre"));
        auteurColumn.setCellValueFactory(new PropertyValueFactory<>("auteur"));
        anneeColumn.setCellValueFactory(new PropertyValueFactory<>("anneePublication"));
        disponibleColumn.setCellValueFactory(new PropertyValueFactory<>("disponible"));

        // Liaison de la liste observable à la table
        livresTable.setItems(livresList);

        // Configuration de la ComboBox de recherche
        searchTypeComboBox.setItems(FXCollections.observableArrayList("Titre", "Auteur", "ISBN"));
        searchTypeComboBox.setValue("Titre");

        // Écouteur de sélection dans la table
        livresTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                afficherLivre(newSelection);
            }
        });

        // Chargement initial des livres
        actualiserTable();
    }

    /**
     * Ajoute un nouveau livre.
     */
    @FXML
    private void ajouterLivre() {
        try {
            Livre livre = creerLivreDepuisFormulaire();
            bibliothequeService.ajouterLivre(livre);
            actualiserTable();
            viderFormulaire();
            afficherMessage("Livre ajouté avec succès", Alert.AlertType.INFORMATION);
        } catch (ValidationException | SQLException e) {
            afficherMessage("Erreur lors de l'ajout: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * Modifie le livre sélectionné.
     */
    @FXML
    private void modifierLivre() {
        Livre livreSelectionne = livresTable.getSelectionModel().getSelectedItem();
        if (livreSelectionne == null) {
            afficherMessage("Veuillez sélectionner un livre à modifier", Alert.AlertType.WARNING);
            return;
        }

        try {
            Livre livreModifie = creerLivreDepuisFormulaire();
            livreModifie.setIsbn(livreSelectionne.getIsbn()); // Conserver l'ISBN original
            bibliothequeService.modifierLivre(livreModifie);
            actualiserTable();
            afficherMessage("Livre modifié avec succès", Alert.AlertType.INFORMATION);
        } catch (ValidationException | SQLException e) {
            afficherMessage("Erreur lors de la modification: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * Supprime le livre sélectionné.
     */
    @FXML
    private void supprimerLivre() {
        Livre livreSelectionne = livresTable.getSelectionModel().getSelectedItem();
        if (livreSelectionne == null) {
            afficherMessage("Veuillez sélectionner un livre à supprimer", Alert.AlertType.WARNING);
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION,
                "Êtes-vous sûr de vouloir supprimer ce livre ?",
                ButtonType.YES, ButtonType.NO);
        confirmation.showAndWait();

        if (confirmation.getResult() == ButtonType.YES) {
            try {
                bibliothequeService.supprimerLivre(livreSelectionne.getIsbn());
                actualiserTable();
                viderFormulaire();
                afficherMessage("Livre supprimé avec succès", Alert.AlertType.INFORMATION);
            } catch (ValidationException | SQLException e) {
                afficherMessage("Erreur lors de la suppression: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    /**
     * Emprunte le livre sélectionné.
     */
    @FXML
    private void emprunterLivre() {
        Livre livreSelectionne = livresTable.getSelectionModel().getSelectedItem();
        if (livreSelectionne == null) {
            afficherMessage("Veuillez sélectionner un livre à emprunter", Alert.AlertType.WARNING);
            return;
        }

        try {
            bibliothequeService.emprunterLivre(livreSelectionne.getIsbn());
            actualiserTable();
            afficherMessage("Livre emprunté avec succès", Alert.AlertType.INFORMATION);
        } catch (LivreIndisponibleException | ValidationException | SQLException e) {
            afficherMessage("Erreur lors de l'emprunt: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * Retourne le livre sélectionné.
     */
    @FXML
    private void retournerLivre() {
        Livre livreSelectionne = livresTable.getSelectionModel().getSelectedItem();
        if (livreSelectionne == null) {
            afficherMessage("Veuillez sélectionner un livre à retourner", Alert.AlertType.WARNING);
            return;
        }

        try {
            bibliothequeService.retournerLivre(livreSelectionne.getIsbn());
            actualiserTable();
            afficherMessage("Livre retourné avec succès", Alert.AlertType.INFORMATION);
        } catch (ValidationException | SQLException e) {
            afficherMessage("Erreur lors du retour: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * Effectue une recherche selon le type sélectionné.
     */
    @FXML
    private void rechercherLivres() {
        String terme = searchField.getText().trim();
        String type = searchTypeComboBox.getValue();

        if (terme.isEmpty()) {
            actualiserTable();
            return;
        }

        try {
            List<Livre> resultats;
            switch (type) {
                case "Titre":
                    resultats = bibliothequeService.rechercherParTitre(terme);
                    break;
                case "Auteur":
                    resultats = bibliothequeService.rechercherParAuteur(terme);
                    break;
                case "ISBN":
                    Livre livre = bibliothequeService.trouverLivreParIsbn(terme);
                    resultats = livre != null ? List.of(livre) : List.of();
                    break;
                default:
                    resultats = List.of();
            }
            livresList.clear();
            livresList.addAll(resultats);
        } catch (ValidationException | SQLException e) {
            afficherMessage("Erreur lors de la recherche: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * Actualise la table avec tous les livres.
     */
    @FXML
    private void actualiserTable() {
        try {
            List<Livre> livres = bibliothequeService.listerTousLesLivres();
            livresList.clear();
            livresList.addAll(livres);
            searchField.clear();
        } catch (SQLException e) {
            afficherMessage("Erreur lors du chargement des livres: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * Affiche les détails d'un livre dans le formulaire.
     */
    private void afficherLivre(Livre livre) {
        isbnField.setText(livre.getIsbn());
        titreField.setText(livre.getTitre());
        auteurField.setText(livre.getAuteur());
        anneeField.setText(String.valueOf(livre.getAnneePublication()));
        disponibleCheckBox.setSelected(livre.isDisponible());
    }

    /**
     * Vide le formulaire.
     */
    private void viderFormulaire() {
        isbnField.clear();
        titreField.clear();
        auteurField.clear();
        anneeField.clear();
        disponibleCheckBox.setSelected(true);
    }

    /**
     * Crée un objet Livre à partir des données du formulaire.
     */
    private Livre creerLivreDepuisFormulaire() throws ValidationException {
        String isbn = isbnField.getText().trim();
        String titre = titreField.getText().trim();
        String auteur = auteurField.getText().trim();
        String anneeStr = anneeField.getText().trim();
        boolean disponible = disponibleCheckBox.isSelected();

        if (isbn.isEmpty() || titre.isEmpty() || auteur.isEmpty() || anneeStr.isEmpty()) {
            throw new ValidationException("Tous les champs doivent être remplis");
        }

        int annee;
        try {
            annee = Integer.parseInt(anneeStr);
        } catch (NumberFormatException e) {
            throw new ValidationException("L'année doit être un nombre valide");
        }

        return new Livre(isbn, titre, auteur, annee, disponible);
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
