/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clinica.dao;

import clinica.model.Prenotazione;
import clinica.utils.DatabaseManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrenotazioneDAO {

   //contaPrenotazioniMedicoGiorno: Impedisce che un medico riceva troppi appuntamenti nello stesso giorno
   //Logica buisness del sistema
    
    public int contaPrenotazioniMedicoGiorno(String medico, Timestamp data) throws SQLException {
        String query = "SELECT COUNT(*) FROM Prenotazione WHERE Medico = ? AND DATE(Data_appuntamento) = DATE(?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, medico);
            ps.setTimestamp(2, data);
            ResultSet rs = ps.executeQuery();
            //Ritorna il numero di prenotazioni trovate
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }
    //Semplice Insert per aggiunere una prenotazione
    public boolean inserisciPrenotazione(Prenotazione p) throws SQLException {
      
        if (contaPrenotazioniMedicoGiorno(p.getMedico(), p.getDataAppuntamento()) >= 4) {
            return false; 
        }

        String query = "INSERT INTO Prenotazione (Id_Utente, Medico, stanza, Data_appuntamento, Motivo_visita) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, p.getIdUtente());
            ps.setString(2, p.getMedico());
            ps.setString(3, p.getStanza());
            ps.setTimestamp(4, p.getDataAppuntamento());
            ps.setString(5, p.getMotivoVisita());
            return ps.executeUpdate() > 0;
        }
    }


    public List<Prenotazione> getListaPerUtente(int idUtente) throws SQLException {
        List<Prenotazione> lista = new ArrayList<>();
        String query = "SELECT * FROM Prenotazione WHERE Id_Utente = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, idUtente);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                //Trasforma ogni riga del database in un oggetto Java: Prenotazione
                Prenotazione p = new Prenotazione();
                p.setIdPrenotazione(rs.getInt("Id_prenotazione"));
                p.setMedico(rs.getString("Medico"));
                p.setStanza(rs.getString("stanza"));
                p.setDataAppuntamento(rs.getTimestamp("Data_appuntamento"));
                p.setMotivoVisita(rs.getString("Motivo_visita"));
                lista.add(p);
            }
        }
        return lista;
    }
    

    public boolean cancellaPrenotazione(int idPrenotazione) throws SQLException {
     String query = "DELETE FROM Prenotazione WHERE Id_prenotazione = ?";
     try (Connection conn = DatabaseManager.getConnection();
          PreparedStatement ps = conn.prepareStatement(query)) {
         ps.setInt(1, idPrenotazione);
         return ps.executeUpdate() > 0;
     }
 }

    public boolean modificaPrenotazioneCompleta(int id, String motivo, Timestamp data) throws SQLException {
        String query = "UPDATE Prenotazione SET Motivo_visita = ?, Data_appuntamento = ? WHERE Id_prenotazione = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, motivo);
            ps.setTimestamp(2, data);
            ps.setInt(3, id);
            return ps.executeUpdate() > 0;
            }
        }
    }
