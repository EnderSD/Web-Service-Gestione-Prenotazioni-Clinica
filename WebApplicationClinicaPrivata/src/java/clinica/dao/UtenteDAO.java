/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clinica.dao;

import clinica.model.Utente;
import clinica.utils.DatabaseManager;
import java.sql.*;

public class UtenteDAO {

    public boolean registraUtente(Utente utente) throws SQLException {
        String query = "INSERT INTO Utente (nome, cognome, email, password) VALUES (?, ?, ?, ?)";
        
        //try-with-resources: apre la connessione e si assicura di chiuderla in automatico
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setString(1, utente.getNome());
            ps.setString(2, utente.getCognome());
            ps.setString(3, utente.getEmail());
            ps.setString(4, utente.getPassword()); 
            
            return ps.executeUpdate() > 0;
        }
    }

    
    public Utente login(String email, String password) throws SQLException {
        //Cerca nel database un utente che abbia emaile password concordati
        String query = "SELECT * FROM Utente WHERE email = ? AND password = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setString(1, email);
            ps.setString(2, password);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Utente(
                        rs.getInt("Id_Utente"),
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getString("email"),
                        rs.getString("password")
                    );
                }
            }
        }
        return null; 
    }
}