/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clinica.model; // Assicurati che il nome del package sia corretto

public class Utente {
    private int idUtente;
    private String nome;
    private String cognome;
    private String email;
    private String password;

    public Utente() {}

    public Utente(int idUtente, String nome, String cognome, String email, String password) {
        this.idUtente = idUtente;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.password = password;
    }

    
    public int getIdUtente() { 
        
        return idUtente; 
        
    }
    public void setIdUtente(int idUtente) { 
        
        this.idUtente = idUtente;
        
    }
    public String getNome() { 
        
        return nome; 
        
    }
    public void setNome(String nome) { 
        
        this.nome = nome; 
        
    }
    public String getCognome() { 
        
        return cognome;
        
    }
    public void setCognome(String cognome) { 
        
        this.cognome = cognome; 
    
    }
    public String getEmail() {
        
        return email; 
    
    }
    public void setEmail(String email) { 
        
        this.email = email; 
    
    }
    public String getPassword() { 
        
        return password;
    
    }
    public void setPassword(String password) { 
        
        this.password = password;
    
    }
}