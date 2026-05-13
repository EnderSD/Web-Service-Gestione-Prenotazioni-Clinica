CREATE DATABASE IF NOT EXISTS clinica_db;
USE clinica_db;

CREATE TABLE Utente (
    Id_Utente INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(50) NOT NULL,
    cognome VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL, 
    password VARCHAR(255) NOT NULL
);

CREATE TABLE Prenotazione (
    Id_prenotazione INT AUTO_INCREMENT PRIMARY KEY,
    Id_Utente INT NOT NULL,
    Medico VARCHAR(100) NOT NULL,
    stanza VARCHAR(50),
    Data_appuntamento DATETIME NOT NULL,
    Motivo_visita TEXT,
    FOREIGN KEY (Id_Utente) REFERENCES Utente(Id_Utente) ON DELETE CASCADE
);