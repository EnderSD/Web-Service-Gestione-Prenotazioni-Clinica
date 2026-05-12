/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clinica.model;

import java.sql.Timestamp;

public class Prenotazione {
    private int idPrenotazione;
    private int idUtente;
    private String medico;
    private String stanza;
    private Timestamp dataAppuntamento;
    private String motivoVisita;

    public Prenotazione() {}

    // Getter e Setter
    public int getIdPrenotazione() { return idPrenotazione; }
    public void setIdPrenotazione(int idPrenotazione) { this.idPrenotazione = idPrenotazione; }
    public int getIdUtente() { return idUtente; }
    public void setIdUtente(int idUtente) { this.idUtente = idUtente; }
    public String getMedico() { return medico; }
    public void setMedico(String medico) { this.medico = medico; }
    public String getStanza() { return stanza; }
    public void setStanza(String stanza) { this.stanza = stanza; }
    public Timestamp getDataAppuntamento() { return dataAppuntamento; }
    public void setDataAppuntamento(Timestamp dataAppuntamento) { this.dataAppuntamento = dataAppuntamento; }
    public String getMotivoVisita() { return motivoVisita; }
    public void setMotivoVisita(String motivoVisita) { this.motivoVisita = motivoVisita; }
}