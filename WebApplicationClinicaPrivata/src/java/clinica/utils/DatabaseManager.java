/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clinica.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {

    private static final String URL = "jdbc:mysql://localhost:3306/clinica_db?useSSL=false&serverTimezone=UTC";
    //Utente predefinito di MySQL (root) e password (vuota nel tuo caso)
    private static final String USER = "root";
    private static final String PASSWORD = ""; 

    public static Connection getConnection() throws SQLException {
        try {
            //Carica in memoria il Driver JDBC di MySQL
            //E'ciò che consente a java di comunicare con MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            //Se il Driver non è nel progetto, lancia un errore specifico
            throw new SQLException("Driver MySQL non trovato!", e);
        }
    }
}