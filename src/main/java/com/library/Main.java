package com.library;

import com.library.controller.MainController;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Classe principale - Point d'entrée de l'application
 */
public class Main extends Application {
    
    private MainController mainController;
    
    @Override
    public void start(Stage primaryStage) {
        try {
           
            
            
            primaryStage.setTitle("Système de Gestion de Bibliothèque");
            primaryStage.setWidth(1200);
            primaryStage.setHeight(800);
            
            // Gérer la fermeture propre
            primaryStage.setOnCloseRequest(event -> {
                if (mainController != null) {
                    mainController.shutdown();
                }
            });
            
            //primaryStage.show(); // nut now
            
            System.out.println("Jaafar, do your work");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}