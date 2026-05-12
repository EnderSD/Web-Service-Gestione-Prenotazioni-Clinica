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

    // Conta quante prenotazioni ha un medico in un determinato giorno
    public int contaPrenotazioniMedicoGiorno(String medico, Timestamp data) throws SQLException {
        String query = "SELECT COUNT(*) FROM Prenotazione WHERE Medico = ? AND DATE(Data_appuntamento) = DATE(?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, medico);
            ps.setTimestamp(2, data);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    // Inserimento con validazione
    public boolean inserisciPrenotazione(Prenotazione p) throws SQLException {
        // 1. Controllo limite 4 prenotazioni
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

    // Lista prenotazioni per utente
    public List<Prenotazione> getListaPerUtente(int idUtente) throws SQLException {
        List<Prenotazione> lista = new ArrayList<>();
        String query = "SELECT * FROM Prenotazione WHERE Id_Utente = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, idUtente);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
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
}