package com.library.util;

import org.junit.jupiter.api.*;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseConnectionTest {
    
    @Test
    @DisplayName("Test: getInstance() retourne toujours la même instance")
    void testSingletonInstance() {
        DatabaseConnection instance1 = DatabaseConnection.getInstance();
        DatabaseConnection instance2 = DatabaseConnection.getInstance();
        
        assertSame(instance1, instance2, "Les instances doivent être identiques (Singleton)");
    }
    
    @Test
    @DisplayName("Test: getConnection() retourne une connexion valide")
    void testGetConnection() throws SQLException {
        DatabaseConnection dbConnection = DatabaseConnection.getInstance();
        Connection connection = dbConnection.getConnection();
        
        assertNotNull(connection, "La connexion ne doit pas être null");
        assertFalse(connection.isClosed(), "La connexion doit être ouverte");
    }
    
    @Test
    @DisplayName("Test: isConnected() retourne true")
    void testIsConnected() {
        DatabaseConnection dbConnection = DatabaseConnection.getInstance();
        assertTrue(dbConnection.isConnected(), "La connexion doit être active");
    }
}