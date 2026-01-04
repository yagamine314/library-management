package com.library;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.library.controller.MainController;

/**
 * Classe principale - Point d'entrée de l'application
 * Charge l'interface JavaFX et configure la fenêtre principale
 */
public class Main extends Application {
    
    private MainController mainController;
    
    @Override
    public void start(Stage primaryStage) {
        try {
            // Charger le fichier FXML principal
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainView.fxml"));
            Parent root = loader.load();
            
            // Récupérer le contrôleur
            mainController = loader.getController();
            
            // Créer la scène
            Scene scene = new Scene(root);
            
            // Configurer la fenêtre
            primaryStage.setTitle("Système de Gestion de Bibliothèque");
            primaryStage.setScene(scene);
            primaryStage.setWidth(1200);
            primaryStage.setHeight(800);
            primaryStage.setMinWidth(1000);
            primaryStage.setMinHeight(600);
            
            // Gérer la fermeture propre de l'application
            primaryStage.setOnCloseRequest(event -> {
                if (mainController != null) {
                    mainController.shutdown();
                }
                System.out.println("Application fermée proprement");
            });
            
            // Afficher la fenêtre
            primaryStage.show();
            
            System.out.println("✓ Application démarrée avec succès!");
            
        } catch (Exception e) {
            System.err.println("Erreur lors du démarrage de l'application:");
            e.printStackTrace();
        }
    }
    
    @Override
    public void stop() {
        // Appelé automatiquement à la fermeture
        if (mainController != null) {
            mainController.shutdown();
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}