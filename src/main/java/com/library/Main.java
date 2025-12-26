package com.library;

import com.library.controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Classe principale - Point d'entrée de l'application
 */
public class Main extends Application {
    
    private MainController mainController;
    
    @Override
    public void start(Stage primaryStage) {
        try {
            // TODO: Charger MainView.fxml quand il sera créé
            // Pour l'instant, juste une fenêtre vide
            
            primaryStage.setTitle("Système de Gestion de Bibliothèque");
            primaryStage.setWidth(1200);
            primaryStage.setHeight(800);
            
            // Gérer la fermeture propre
            primaryStage.setOnCloseRequest(event -> {
                if (mainController != null) {
                    mainController.shutdown();
                }
            });
            
            // primaryStage.show(); // À décommenter plus tard
            
            System.out.println("✅ Application prête (interface pas encore créée)");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}