
# Documentazione Approfondita: Web Service Gestione Prenotazioni Clinica

## Indice

1. **Scopo del Sistema**
   * 1.1. Obiettivi e Contesto
   * 1.2. Problemi Risolti e Benefici
   * 1.3. Utenti e Sistemi Target
   * 1.4. Confini e Limitazioni

2. **Utilizzo delle API (Web Service REST)**
   * 2.1. Principi e Architettura API
   * 2.2. URL di Base e Versioning
   * 2.3. Endpoint Dettagliati
     * 2.3.1. `POST /prenotazioni/registrazione` (Registrazione Utente)
     * 2.3.2. `POST /prenotazioni/login` (Login Utente)
     * 2.3.3. `POST /prenotazioni/nuova` (Nuova Prenotazione)
     * 2.3.4. `GET /prenotazioni/lista` (Lista Prenotazioni Utente)
     * 2.3.5. `PUT /prenotazioni/modifica` (Modifica Prenotazione)
     * 2.3.6. `DELETE /prenotazioni/cancella` (Cancella Prenotazione)
     * 2.3.7. `POST /prenotazioni/logout` (Uscita dalla sessione)
   * 2.4. Flussi di Lavoro Tipici
   * 2.5. Gestione degli Errori e Codici di Stato

3. **Dettagli di Implementazione**
   * 3.1. Requisiti di sistema e ambiente
   * 3.2. Architettura Software
   * 3.3. Struttura del Database
   * 3.4. Componenti Chiave del Backend (Java)
   * 3.5. Logica di Business e Validazione
   * 3.6. Considerazioni sulla Sicurezza (Attuale e Futura)
   * 3.7. Possibili Estensioni Future
---

## 1. Scopo del Sistema

### 1.1. Obiettivi e Contesto

L'obiettivo primario di questo sistema è fornire una soluzione **centralizzata, affidabile e automatizzata** per la gestione delle prenotazioni di una clinica medica di piccole-medie dimensioni. Il sistema permette ai pazienti di registrarsi, autenticarsi e prenotare appuntamenti con i medici, garantendo una **tracciabilità completa** e una **validazione automatica** delle prenotazioni stesse.

Il sistema nasce dall'esigenza di superare i limiti dei sistemi manuali (agende cartacee, telefonate) offrendo una piattaforma digitale moderna che consente:
- Prenotazioni senza errori di comunicazione
- Disponibilità in tempo reale
- Archivio digitale delle prenotazioni

### 1.2. Problemi Risolti e Benefici

* **Eliminazione Errori Manuali:** Riduce drasticamente le imprecisioni e i conflitti di double-booking (prenotazioni doppie).
* **Disponibilità 24/7:** I pazienti possono prenotare in qualsiasi momento, non solo durante gli orari di apertura della clinica.
* **Dati in Tempo Reale:** Fornisce una visione aggiornata della disponibilità dei medici e delle stanze.
* **Efficienza Amministrativa:** Semplifica il lavoro dello staff clinico per la gestione dei pazienti e degli appuntamenti.
* **Tracciabilità:** Crea un archivio digitale immutabile dei dati di prenotazione (utente, medico, data/ora, stanza, motivo visita).
* **Flessibilità:** L'architettura basata su API permette l'integrazione con portali web, app mobile o sistemi di gestione sanitaria.
* **Validazione Automatica:** Implementa regole di business (es. massimo 4 prenotazioni al giorno per medico, solo date future) garantendo coerenza dei dati.

### 1.3. Utenti e Sistemi Target

* **Pazienti:** Utenti finali che accedono per registrarsi, autenticarsi e gestire le proprie prenotazioni.
* **Staff Clinico:** Utilizatori dell'interfaccia di amministrazione per visualizzare, modificare e gestire prenotazioni e medici.
* **Medici:** Possono visualizzare il loro calendario di appuntamenti (in futuri sviluppi).
* **Portale Web/App Mobile:** Client che consuma le API per presentare l'interfaccia ai pazienti.
* **Sviluppatori/Integratori:** Coloro che devono interfacciare sistemi di gestione sanitaria (ERP, CRM) con questa piattaforma.paghe.

### 1.4. Confini e Limitazioni (Attuali)

Il sistema, nella sua implementazione attuale:

* **Fa:** 
  - Registrazione utenti
  - Autenticazione email + password
  - CRUD prenotazioni con validazioni di business
  - Elenco prenotazioni personalizzate per utente
  - Cancellazione prenotazioni
  
* **Non Fa:**
  - Gestione medici e stanze via API
  - Crittografia password 
  - Gestione ferie/permessi dei medici
  - Notifiche (email, SMS) di conferma/reminder
  - Reportistica e analytics
  - Sistema di pagamento
  - Gestione liste d'attesa

---

## 2. Utilizzo delle API (Web Service REST)

### 2.1. Principi e Architettura API

Il web service è progettato seguendo i principi REST (Representational State Transfer):

* **Risorsa:** L'entità principale è la "prenotazione", accessibile tramite l'endpoint `/prenotazioni`.
* **Metodi HTTP:** Utilizza i verbi HTTP standard per definire le azioni:
  * `GET`: Per recuperare informazioni (lettura).
  * `POST`: Per creare nuove risorse (registrazione, login, nuova prenotazione).
  * `PUT`: Per aggiornare risorse esistenti (modifica prenotazione).
  * `DELETE`: Per rimuovere risorse (cancellazione prenotazione).
* **Stateless:** Ogni richiesta deve contenere tutte le informazioni necessarie (email + password in ogni richiesta).
* **Interfaccia Uniforme:** L'uso di HTTP, URL standard e XML garantisce compatibilità totale.

### 2.2. URL di Base e Versioning

L'URL di base è: `http://<server>:<porta>/<nome_applicazione>/api`

Esempio: `http://localhost:????/GestionePrenotazioniClinica/api/prenotazioni`

Attualmente **non è implementato un sistema di versioning** delle API. Per future evoluzioni (versione 2.0), sarà consigliabile introdurre `/api/v1/prenotazioni` e `/api/v2/prenotazioni`.

### 2.3. Endpoint Dettagliati

#### 2.3.1. `GET /prenotazioni`

* **Scopo:** Recuperare l'elenco completo delle prenotazioni.
* **Flusso:**
1. Client invia i dati: nome, cognome, email, password.
2. Server valida i dati (email univoca, non vuota, ecc.).
3. Se valido, inserisce il nuovo utente nel DB.

#### 2.3.2. `POST /prenotazioni/login - Autenticazione Utent`

* **Scopo:** Autenticare un utente tramite email e password.
* **Flusso:** 
    1. Il client invia email e password.
    2. Il server verifica la combinazione nel DB.
    3. Se valida, restituisce l'ID utente e i dettagli.

####  2.3.3. `POST /api/prenotazioni/nuova - Nuova Prenotazione`

* **Scopo:** Creare una nuova prenotazione per un paziente.
* **Flusso:**

    1. Client invia: idUtente, email, password (per autenticazione), dataAppuntamento, motivoVisita.
    2. Server valida l'autenticazione dell'utente.
    3. Verifica che la data sia futura.
    4. Verifica il numero di prenotazioni per quel medico in quel giorno (max 4).
    5. Controlla univocità (medico + data_appuntamento).
    6. Se valido, assegna automaticamente medico e stanza.
    7. Inserisce la prenotazione nel DB.


####  2.3.4. `GET /api/prenotazioni/lista - Lista Prenotazioni Utente`

* **Scopo:** Recuperare tutte le prenotazioni di un paziente.
* **Flusso:**
    1. Client invia idUtente come parametro query.
    2. Server interroga il DB per tutte le prenotazioni dell'utente.
    3. Restituisce le prenotazioni.

####  2.3.5. `PUT /api/prenotazioni/modifica - Modifica Prenotazione`

* **Scopo:** Modificare una prenotazione esistente (es. cambiare data).
* **Flusso:**
    1. Client invia: idPrenotazione, idUtente, email, password, e i nuovi dati (dataAppuntamento).
    2. Server valida l'autenticazione.
    3. Verifica che la prenotazione appartenga all'utente.
    4. Valida i nuovi dati (data futura, disponibilità medico).
    5. Aggiorna il record nel DB.

####  2.3.6. DELETE /api/prenotazioni/cancella - Cancella Prenotazione

* **Scopo:**Eliminare una prenotazione.
* **Flusso:**
    1. Client invia: idPrenotazione, idUtente, email, password.
    2. Server valida l'autenticazione.
    3. Verifica che la prenotazione appartenga all'utente.
    4. Elimina il record dal DB.

####  2.3.7. POST /api/prenotazioni/logout - Rimozione sessione

* **Scopo:**Chiudere la sessione.
* **Flusso:**
    1. Client invia richiesta di logout.
    2. Server valida la richiesta.
    3. Chiusura della sessione.

### 2.4. Flussi di Lavoro Tipici

Scenario 1: Registrazione e Prima Prenotazione

1.  Mario si registra
2.  Mario effettua il login
3.  Mario crea una prenotazione
4.  Mario consulta le sue prenotazioni

Scenario 2: Modifica Prenotazione

1. Mario decide di cambiare data

Scenario 3: Cancellazione prenotazione

1. Marco decide di cancellare la prenotazione

Scenario 4: Email già registrata

1. Un altro utente tenta di registrarsi con un email già usata
2. Presentazione dell'errore

### 2.5. Errrori/Conferme

È fondamentale che i client interpretino correttamente i codici di stato HTTP:

* Richiesta (GET, DELETE) completata con successo.
* Risorsa creata con successo.
* Richiesta malformata (campi mancanti, formato errato).
* Autenticazione fallita (email/password errati).
* Risorsa non trovata (utente, prenotazione inesistente)
* Conflitto con regola di business


I client _dovrebbero_ sempre leggere il corpo della risposta (`text/plain`) in caso di errore per ottenere un messaggio descrittivo.

---

## 3. Dettagli di Implementazione

### 3.1. Requisiti di sistema e ambiente

Per il corretto funzionamento, la compilazione e il deployment dell'applicazione, sono necessari i seguenti componenti:

1. **Java Development Kit (JDK):** Versione 11
2. **Application Server:** Apache Tomcatt(8.5.96)
3. **Database:** MySQL
4. **Libreria:** mysql-connector-j-8.4.0.jar 
5. **IDE:** NetBeans IDE 29

### 3.2. Architettura Software

Il sistema segue un'architettura a layer (strati) semplificata:

1.  **Presentation/Client Layer:** L'interfaccia web (`index.html`) o sistemi esterni.
2.  **Web/Controller Layer**
3.  **Business/Data Access Layer:**
4.  **Data Layer:** Database MySQL (presente all'interno della repository sotto il nome di "DataBase WebApplicationClinica", copiare ed incollare le query presenti per il funzionamento corretto del programma)

Il tutto è eseguito tramite **XAMPP Apache Tomcat**.

### 3.3. Struttura del Database

* **`Utente `:** Anagrafica base, email, password.
* **`Prenotazione`:** Id_prenotazione INT AUTO_INCREMENT PRIMARY KEY, Id_Utente INT, Medico VARCHAR(100), stanza VARCHAR(50), Data_appuntamento DATETIME, Motivo_visita TEXT.

### 3.4. Componenti Chiave del Backend (Java)

#### 3.4.1. `ClinicaServlet` 

* Gestisce le richieste tramite i metodi doGet e doPost.
* Estrae l'azione dal path (registrazione, login, logout, nuova, modifica, cancella).
* Interpreta parametri e formatta risposte.

#### 3.4.2. `UtenteDAO` 

* Gestire le operazioni CRUD su Utenti.
* Interagire con il DB tramite JDBC.
* Implementare la logica di validazione utenti.

#### 3.4.3. `PrenotazioneDAO` 

* Gestire le operazioni CRUD su Prenotazioni.
* Implementare la logica di validazione e assegnazione prenotazioni.

#### 3.4.4. `DatabaseManager` 

* Centralizzare la connessione al database.
* Fornire connessioni JDBC.

### 3.5. Logica di Business e Validazione

Validazioni Registrazione:

1. Nome/Cognome: Non vuoti, lunghezza 1-50 caratteri.
2. Email: Formato valido, non vuota, univoca nel DB.
3. Password: Non vuota, lunghezza minima 6 caratteri.

Validazioni Login:

1. Email: esiste nel DB. 
2. Password: Corrisponde a quella memorizzata.

Validazioni Nuova Prenotazione:

1. IdUtente: Esiste nel DB.
2. Email/Password: Corrette (re-autenticazione).
3. Data Appuntamento.
4. Massimo Prenotazioni Medico/Giorno: Un medico può avere massimo 4 prenotazioni al giorno.
5. Unicità Medico+Data: Non esiste già una prenotazione per quello stesso medico a quell'ora.
6. MotivoVisita: Non vuoto.

Assegnazione Automatica Medico/Stanza:

* Medici disponibili: Dr. Bianchi, Dr. Verdi, Dr. Rossi (con stanze 101, 102, 103).
* Logica di Assegnazione: random successivamente è possibilie implementare un sorteggio Round-robin (il prossimo medico con meno prenotazioni quel giorno).

Validazioni Modifica Prenotazione:

* Stesse validazioni di nuova prenotazione.
* Verifica che la prenotazione appartenga all'utente loggato.
* Se si cambia data, ri-valida disponibilità.

Validazioni Cancellazione:

* Prenotazione esiste.
* Appartiene all'utente autenticato.


### 3.6. Considerazioni sulla Sicurezza (Attuale e Futura)

* **Attuale (Minimale):**
- Autenticazione: Solo email + password (plain text, nessun token).
- Validazione Input: Controllli di base sulla lunghezza campi.
- SQL Injection: Mitigata con PreparedStatement.
- HTTPS: NON implementato (solo HTTP).
- Crittografia Password: NO (plain text nel DB - solo per demo).

### 3.7. Possibili Estensioni Future

* Crittografia Password: Hash con algoritmo sicuro (bcrypt, Argon2).
* HTTPS/TLS: Comunicazione crittografata.
* Rate Limiting: Limitare numero di richieste per IP.
* Autorizzazione Granulare: Ruoli (patient, admin, doctor).
* Aggiunta tabelle Medico e Stanza per gestione anagrafica.
* Endpoint per medici di visualizzare loro prenotazioni.
* Reportistica: statistiche prenotazioni, occupazione medici.
* App mobile dedicata.


