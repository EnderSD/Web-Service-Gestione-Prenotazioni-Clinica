package clinica.controller;

import java.text.SimpleDateFormat;
import clinica.dao.*;
import clinica.model.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "ClinicaServlet", urlPatterns = {"/api/*"})
public class ClinicaServlet extends HttpServlet {

    private UtenteDAO utenteDAO = new UtenteDAO();
    private PrenotazioneDAO prenotazioneDAO = new PrenotazioneDAO();
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String path = request.getPathInfo();
        
        try {
            if ("/registrazione".equals(path)) {
                Utente u = new Utente();
                u.setNome(request.getParameter("nome"));
                u.setCognome(request.getParameter("cognome"));
                u.setEmail(request.getParameter("email"));
                u.setPassword(request.getParameter("password"));

                if (utenteDAO.registraUtente(u)) {
                    response.sendRedirect(request.getContextPath() + "/index.html?reg=success");
                } else {
                    response.sendRedirect(request.getContextPath() + "/index.html?error=exists");
                }

            } else if ("/login".equals(path)) {
                String email = request.getParameter("email");
                String pass = request.getParameter("password");
                Utente u = utenteDAO.login(email, pass);
                
                if (u != null) {
                    HttpSession session = request.getSession();
                    session.setAttribute("utenteLoggato", u);
                    // ASSICURATI CHE IL FILE SI CHIAMI dashboard.html CON LA 'A'
                    response.sendRedirect(request.getContextPath() + "/dashboard.html");
                } else {
                    response.sendRedirect(request.getContextPath() + "/index.html?error=auth");
                }

            } else if ("/nuova".equals(path)) {
                HttpSession session = request.getSession(false);
                if (session == null || session.getAttribute("utenteLoggato") == null) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
                
                Utente u = (Utente) session.getAttribute("utenteLoggato");
                
                String[] medici = {"Dr. Bianchi", "Dr. Verdi", "Dr. Rossi"};
                String[] stanze = {"101", "102", "103"};
                int idx = (int) (Math.random() * 3);

                Prenotazione p = new Prenotazione();
                p.setIdUtente(u.getIdUtente());
                p.setMedico(medici[idx]);
                p.setStanza(stanze[idx]);
                
                String dataInput = request.getParameter("data");
                p.setDataAppuntamento(Timestamp.valueOf(dataInput + " 00:00:00"));
                p.setMotivoVisita(request.getParameter("motivo"));

                if (prenotazioneDAO.inserisciPrenotazione(p)) {
                    response.sendRedirect(request.getContextPath() + "/dashboard.html?msg=ok");
                } else {
                    response.sendRedirect(request.getContextPath() + "/dashboard.html?error=full");
                }
            }
        } catch (SQLException e) {
            response.sendRedirect(request.getContextPath() + "/index.html?error=db");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String path = request.getPathInfo();
        HttpSession session = request.getSession(false);

        if ("/logout".equals(path)) {
            if (session != null) { session.invalidate(); }
            response.sendRedirect(request.getContextPath() + "/index.html");
            
        } else if ("/lista".equals(path)) {
            if (session == null || session.getAttribute("utenteLoggato") == null) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            
            Utente u = (Utente) session.getAttribute("utenteLoggato");
            try {
                List<Prenotazione> lista = prenotazioneDAO.getListaPerUtente(u.getIdUtente());
                response.setContentType("text/plain;charset=UTF-8");
                PrintWriter out = response.getWriter();
                
               for (Prenotazione p : lista) {
                String dataFormattata;
                if (p.getDataAppuntamento() != null) {
                    // Usiamo il formattatore, ma se fallisce prendiamo i primi 16 caratteri
                    try {
                        dataFormattata = sdf.format(p.getDataAppuntamento());
                    } catch (Exception e) {
                        dataFormattata = p.getDataAppuntamento().toString().substring(0, 16);
                    }
                } else {
                    dataFormattata = "Data non disp.";
                }

                out.println(p.getIdPrenotazione() + " | " + p.getMedico() + " | " + p.getStanza() + " | " + dataFormattata + " | " + p.getMotivoVisita());
            }
            } catch (SQLException e) {
                response.setStatus(500);
            }
        }
    }
}