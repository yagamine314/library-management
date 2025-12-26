package com.library.controller;

import com.library.util.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.scene.control.TabPane;

/**
 * Contrôleur principal pour gérer la navigation entre les modules
 */
public class MainController {
    
    @FXML
    private TabPane mainTabPane;
    
    /**
     * Initialisation du contrôleur
     */
    @FXML
    public void initialize() {
        System.out.println("MainController initialisé");
    }
    
    /**
     * Méthode appelée à la fermeture de l'application
     */
    public void shutdown() {
        DatabaseConnection.getInstance().closeConnection();
        System.out.println("Application fermée proprement");
    }
}