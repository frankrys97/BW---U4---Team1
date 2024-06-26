package francescocristiano.entities.service;

import com.github.javafaker.Faker;
import francescocristiano.dao.*;
import francescocristiano.entities.mezzi.*;
import francescocristiano.entities.puntiVendita.DistributoreAutomatico;
import francescocristiano.entities.puntiVendita.PuntoVendita;
import francescocristiano.entities.puntiVendita.Rivenditore;
import francescocristiano.entities.titoliDiViaggio.Abbonamento;
import francescocristiano.entities.titoliDiViaggio.Biglietto;
import francescocristiano.entities.titoliDiViaggio.TitoloDiViaggio;
import francescocristiano.entities.utenti.Tessera;
import francescocristiano.entities.utenti.Utente;
import francescocristiano.enums.AttivitaMezzo;
import francescocristiano.enums.StatusDistributore;
import francescocristiano.enums.TipoAbbonamento;
import jakarta.persistence.EntityManager;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Service {
    static Scanner sc = new Scanner(System.in);
    private final EntityManager em;
    private final TrattaDAO trattaDAO;
    private final CorsaDAO corsaDAO;
    private final MezzoDAO mezzoDAO;
    private final UtenteDAO utenteDAO;
    private final TesseraDAO tesseraDAO;
    private final PeriodoServizioManutenzioneDAO periodoServizioManutenzioneDAO;
    private final PuntoVenditaDAO puntoVenditaDAO;
    private final ValidazioneDAO validazioneDAO;
    private final TitoloDiViaggioDAO titoloDiViaggioDAO;

    public Service(EntityManager em) {
        this.em = em;
        this.trattaDAO = new TrattaDAO(em);
        this.corsaDAO = new CorsaDAO(em);
        this.mezzoDAO = new MezzoDAO(em);
        this.utenteDAO = new UtenteDAO(em);
        this.tesseraDAO = new TesseraDAO(em);
        this.periodoServizioManutenzioneDAO = new PeriodoServizioManutenzioneDAO(em);
        this.puntoVenditaDAO = new PuntoVenditaDAO(em);
        this.validazioneDAO = new ValidazioneDAO(em);
        this.titoloDiViaggioDAO = new TitoloDiViaggioDAO(em);
    }

/*    public static LocalTime generaOraCasuale() {
        Random rand = new Random();

        int ore = rand.nextInt(24);
        int minuti = rand.nextInt(60);
        return LocalTime.of(ore, minuti);
    }

    public static LocalTime generaOraCasualeSuccessiva(LocalTime ora) {
        Random rand = new Random();
        int minuti = rand.nextInt(60);
        return ora.plusMinutes(minuti);
    }*/

    public LocalDate generaDataCasuale() {
        Random rand = new Random();
        int anno = rand.nextInt(2018, 2024);
        int mese = rand.nextInt(12) + 1;
        int giorno = rand.nextInt(28) + 1;
        return LocalDate.of(anno, mese, giorno);
    }

    public LocalDate generaDataCasualeSuccessiva(LocalDate dataIniziale) {
        Random random = new Random();
        int rangeGiorni = 365;
        int giorniCasuali = random.nextInt(rangeGiorni) + 1;

        return dataIniziale.plusDays(giorniCasuali);
    }

    public void inizializzaDataBase() {
        Random rand = new Random();
        Faker faker = new Faker();

        for (int i = 0; i < 15; i++) {
            puntoVenditaDAO.aggiungiPuntoVendita(new DistributoreAutomatico(StatusDistributore.values()[rand.nextInt(StatusDistributore.values().length)]));
            puntoVenditaDAO.aggiungiPuntoVendita(new Rivenditore(faker.company().name(), rand.nextBoolean()));
        }


        for (int i = 0; i < 15; i++) {
            Utente utente = new Utente(faker.name().firstName(), faker.name().lastName());
            utenteDAO.aggiungiUtente(utente);

            Utente utenteFromDB = utenteDAO.findById(utente.getId().toString());

            Tessera tessera = new Tessera(generaDataCasuale());
            tesseraDAO.aggiungiTessera(tessera, utenteFromDB);

            Tessera tesseraFromDB = tesseraDAO.findById(tessera.getId().toString());

            List<PuntoVendita> puntiVendita = puntoVenditaDAO.findAll();

            Abbonamento abbonamento = new Abbonamento(generaDataCasualeSuccessiva(tesseraFromDB.getDataEmissione()), puntiVendita.get(rand.nextInt(puntiVendita.size())), TipoAbbonamento.values()[rand.nextInt(TipoAbbonamento.values().length)], tesseraFromDB);
            titoloDiViaggioDAO.aggiungiTitoloDiViaggio(abbonamento);
        }

        for (int i = 0; i < 15; i++) {
            List<PuntoVendita> puntiVenditaFromDB = puntoVenditaDAO.findAll().stream().filter(pv -> (pv instanceof Rivenditore && ((Rivenditore) pv).isLicenza()) || (pv instanceof DistributoreAutomatico && ((DistributoreAutomatico) pv).getStato() == StatusDistributore.ATTIVO)).toList();
            Biglietto bigliettoRandom = new Biglietto(generaDataCasuale(), puntiVenditaFromDB.get(rand.nextInt(puntiVenditaFromDB.size())));
            titoloDiViaggioDAO.aggiungiTitoloDiViaggio(bigliettoRandom);
        }

        for (int i = 0; i < 5; i++) {
            Utente utente = new Utente(faker.name().firstName(), faker.name().lastName());
            utenteDAO.aggiungiUtente(utente);
        }

        for (int i = 0; i < 20; i++) {
            mezzoDAO.aggiungiMezzo(new Tram(rand.nextBoolean()));
        }

        for (int i = 0; i < 30; i++) {
            mezzoDAO.aggiungiMezzo(new Autobus(rand.nextBoolean()));
        }

        List<Mezzo> mezzi = mezzoDAO.findAll().stream().filter(Mezzo::isInServizio).toList();


        for (int i = 0; i < 10; i++) {
            Tratta tratta = new Tratta("Zona " + (i + 1), "Capolinea " + (i + 1));
            trattaDAO.aggiungiTratta(tratta);

            /*List<Tratta> tratte = trattaDAO.findAll();*/
            for (int j = 0; j < 10; j++) {
                LocalDateTime oraPartenza = LocalDateTime.of(2024, rand.nextInt(12) + 1, rand.nextInt(28) + 1, rand.nextInt(24), rand.nextInt(60));
                corsaDAO.aggiungiCorsa(new Corsa(oraPartenza, oraPartenza.plusMinutes(rand.nextInt(15, 300)), tratta, mezzi.get(rand.nextInt(mezzi.size()))));
            }
            Duration tempoMedio = corsaDAO.calcolaTempioMedioPercorrenza(tratta);
            tratta.setTempo_medio_percorrenza(tempoMedio.toMinutes());
            trattaDAO.aggiornaTratta(tratta);
        }

        List<Mezzo> mezziFuoriServizio = mezzoDAO.findAll().stream().filter(m -> !m.isInServizio()).toList();
        for (Mezzo mezzo : mezziFuoriServizio) {
            LocalDate inizio = LocalDate.now().minusDays(rand.nextInt(30));
            LocalDate fine = inizio.plusDays(rand.nextInt(30));
            PeriodoServizioManutenzione periodo = new PeriodoServizioManutenzione(inizio, fine, AttivitaMezzo.IN_MANUTENZIONE, faker.lorem().sentence(3), mezzo);
            periodoServizioManutenzioneDAO.aggiungiPeriodoServizioManutenzione(periodo);
        }

    }

    public void startApp() {
        System.out.println("Benvenuto In EpiAtac - JPA Edition");
        inizializzaDataBase();
        System.out.println();
        while (true) {
            System.out.println("Che tipo di utente sei: ");
            System.out.println("1. Amministratore");
            System.out.println("2. Utente");
            System.out.println("3. Esci dall'applicazione");
            System.out.println();

            try {
                int scelta = Integer.parseInt(sc.nextLine());
                switch (scelta) {
                    case 1:
                        menuAdmin();
                        break;
                    case 2:
                        menuUtente();
                        break;
                    case 3:
                        System.out.println("Arrivederci");
                        resetDataBase();
                        return;
                }
            } catch (Exception e) {
                System.out.println("Scelta non valida");
            }
        }
    }

    public void resetDataBase() {
        em.getTransaction().begin();
        em.createNativeQuery("DROP TABLE abbonamento, biglietto, corsa, distributoreautomatico, mezzo, periodoserviziomanutenzione, puntovendita, rivenditore, tessera, titolodiviaggio, tratta, utente, validazione, validazioneabbonamento, validazionebiglietto").executeUpdate();
        em.getTransaction().commit();
        em.close();
    }

    public void menuAdmin() {
        System.out.println("Inserisci la password: ");
        String password = sc.nextLine().toLowerCase();
        if (!password.equals("epicode")) {
            System.out.println("Password errata!");
            return;
        }
        while (true) {
            System.out.println("Menu Amministratore - Scegli un'opzione");
            System.out.println("1. Gestione Vendite");
            System.out.println("2. Gestione Parco Mezzi");
            System.out.println("3. Torna indietro");
            int scelta = Integer.parseInt(sc.nextLine());
            try {
                switch (scelta) {
                    case 1:
                        menuGestioneVendita();
                        break;
                    case 2:
                        menuGestioneParcoMezzi();
                        break;
                    case 3:
                        return;
                }
            } catch (Exception e) {
                System.out.println("Scelta non valida");
            }
        }
    }

    public void menuGestioneVendita() {
        while (true) {
            System.out.println("Menu Gestione Vendita - Scegli un'opzione");
            System.out.println("1. Utente");
            System.out.println("2. Punti Vendita");
            System.out.println("3. Torna indietro");
            int scelta = Integer.parseInt(sc.nextLine());
            try {
                switch (scelta) {
                    case 1:
                        menuGestioneUtenti();
                        break;
                    case 2:
                        menuGestionePuntiVendita();
                        break;
                    case 3:
                        return;
                }
            } catch (Exception e) {
                System.out.println("Scelta non valida");
            }
        }
    }

    public void menuGestioneUtenti() {
        System.out.println();
        while (true) {
            System.out.println("Menu Gestione Utenti - Scegli un'opzione");
            System.out.println("1. Stampa la lista di utenti con abbonamento");
            System.out.println("2. Torna indietro");
            int scelta = Integer.parseInt(sc.nextLine());
            try {
                switch (scelta) {
                    case 1:
                        System.out.println("La lista di utenti con abbonamento è la seguente: ");
                        utenteDAO.findListaUtentiConAbbonamento().forEach(System.out::println);
                        System.out.println();
                        System.out.println("Il numero totale di utenti con abbonamento è: " + utenteDAO.findListaUtentiConAbbonamento().size());
                        System.out.println();
                        break;
                    case 2:
                        return;
                }
            } catch (Exception e) {
                System.out.println("Scelta non valida");
            }
        }
    }

    public void menuGestionePuntiVendita() {
        System.out.println();
        while (true) {
            System.out.println("Menu Punti Vendita - Scegli un'opzione");
            System.out.println("1. Lista Distributori Automatici attivi");
            System.out.println("2. Lista Distributori Automatici fuori servizio");
            System.out.println("3. Lista Rivenditori con licenza");
            System.out.println("4. Lista Rivenditori senza licenza");
            System.out.println("5. Numero di biglietti e/o abbonamenti emessi per punto vendita");
            System.out.println("6. Numero di biglietti e/o abbonamenti emessi per periodo");
            System.out.println("7. Numero di biglietti e/o abbonamenti emessi per punto vendita e periodo");
            System.out.println("8. Torna indietro");
            int scelta = Integer.parseInt(sc.nextLine());
            try {
                switch (scelta) {
                    case 1:
                        System.out.println();
                        System.out.println("La lista di Distributori Automatici attivi è la seguente: ");
                        puntoVenditaDAO.listaDistributoriAutomaticiAttivi().forEach(System.out::println);
                        System.out.println();
                        System.out.println("Il numero totale di Distributori Automatici attivi è: " + puntoVenditaDAO.listaDistributoriAutomaticiAttivi().size());
                        System.out.println();
                        break;
                    case 2:
                        System.out.println();
                        System.out.println("La lista di Distributori Automatici Fuori Servizio è la seguente: ");
                        puntoVenditaDAO.listaDistributoriAutomaticiNonAttivi().forEach(System.out::println);
                        System.out.println();
                        System.out.println("Il numero totale di Distributori Automatici Fuori Servizio è: " + puntoVenditaDAO.listaDistributoriAutomaticiNonAttivi().size());
                        System.out.println();
                        break;
                    case 3:
                        System.out.println();
                        System.out.println("La lista di Rivenditori Con Licenza è la seguente: ");
                        puntoVenditaDAO.listaRivenditoriConLicenza().forEach(System.out::println);
                        System.out.println();
                        System.out.println("Il numero totale di Rivenditori Con Licenza è: " + puntoVenditaDAO.listaRivenditoriConLicenza().size());
                        System.out.println();
                        break;
                    case 4:
                        System.out.println();
                        System.out.println("La lista di Rivenditori Senza Licenza è la seguente: ");
                        puntoVenditaDAO.listaRivenditoriSenzaLicenza().forEach(System.out::println);
                        System.out.println();
                        System.out.println("Il numero totale di Rivenditori Senza Licenza è: " + puntoVenditaDAO.listaRivenditoriSenzaLicenza().size());
                        System.out.println();
                        break;
                    case 5:
                        PuntoVendita puntoVenditaTrovato = null;
                        System.out.println();
                        try {
                            System.out.println("Inserisci l'id del punto vendita per visualizzare il numero di biglietti e/o abbonamenti emessi:");
                            String idPuntoVendita = sc.nextLine();
                            puntoVenditaTrovato = puntoVenditaDAO.findById(idPuntoVendita);
                            System.out.println("Il numero di biglietti e/o abbonamenti emessi per il punto vendita con id " + puntoVenditaTrovato.getId() + " è: " + puntoVenditaDAO.conteggioTitoliDiViaggioEmessiPerPuntoVendita(puntoVenditaTrovato));
                            System.out.println();
                        } catch (Exception e) {
                            System.out.println();
                            System.out.println("Punto vendita non trovato");
                            System.out.println();
                            break;
                        }

                        break;
                    case 6:
                        System.out.println();
                        LocalDate dataInizio = null;
                        LocalDate dataFine = null;
                        try {
                            System.out.println("Inserisci l'intervallo di tempo per visualizzare il numero di biglietti e/o abbonamenti emessi per un determinato periodo:");
                            System.out.println("Data inizio (yyyy-mm-dd):");
                            dataInizio = LocalDate.parse(sc.nextLine());
                            System.out.println("Data fine (yyyy-mm-dd):");
                            dataFine = LocalDate.parse(sc.nextLine());
                            System.out.println("Il numero di biglietti e/o abbonamenti emessi per il periodo selezionato è: " + puntoVenditaDAO.conteggioTitoliDiViaggioEmessiPerData(dataInizio, dataFine));
                            System.out.println();
                        } catch (DateTimeParseException e) {
                            System.out.println();
                            System.out.println("Data non valida");
                            System.out.println();
                            break;
                        }

                        break;
                    case 7:
                        PuntoVendita puntoVenditaTrovato2 = null;
                        LocalDate dataInizio2 = null;
                        LocalDate dataFine2 = null;
                        System.out.println();
                        try {
                            System.out.println("Inserisci l'id del punto vendita per visualizzare il numero di biglietti e/o abbonamenti emessi:");
                            String idPuntoVendita = sc.nextLine();
                            puntoVenditaTrovato2 = puntoVenditaDAO.findById(idPuntoVendita);
                        } catch (Exception e) {
                            System.out.println();
                            System.out.println("Punto vendita non trovato");
                            System.out.println();
                            break;
                        }
                        try {
                            System.out.println("Inserisci la data d'inizio del periodo da verificare (yyyy-mm-dd):");
                            dataInizio2 = LocalDate.parse(sc.nextLine());
                            System.out.println("Inserisci la data di fine del periodo da verificare (yyyy-mm-dd):");
                            dataFine2 = LocalDate.parse(sc.nextLine());
                            System.out.println();
                            System.out.println("Il numero di biglietti emessi dal punto vendita con id " + puntoVenditaTrovato2.getId() + " per il periodo selezionato è: " + puntoVenditaDAO.conteggioTitoliDiViaggioEmessiPerDataPerPuntoVendita(dataInizio2, dataFine2, puntoVenditaTrovato2));
                            System.out.println();
                        } catch (DateTimeParseException e) {
                            System.out.println();
                            System.out.println("Data non valida");
                            System.out.println();
                            break;
                        }

                        break;
                    case 8:
                        return;
                }
            } catch (Exception e) {
                System.out.println("Scelta non valida");
            }

        }

    }

    public void menuGestioneParcoMezzi() {
        while (true) {
            System.out.println("Gestione Parco Mezzi");
            System.out.println("1. Aggiungi Mezzo");
            System.out.println("2. Visualizza Mezzi");
            System.out.println("3. Aggiungi Tratta");
            System.out.println("4. Aggiungi Corsa");
            System.out.println("5. Torna indietro");
      /*      System.out.println("5. Aggiungi tratta ad un mezzo");
            System.out.println("6. Torna indietro");*/
            int scelta = Integer.parseInt(sc.nextLine());
            try {
                switch (scelta) {
                    case 1: //Aggiungi mezzo
                        System.out.println("Che tipo di mezzo vuoi aggiungere?");
                        System.out.println("1. Tram");
                        System.out.println("2. Autobus");
                        System.out.println();
                        try {
                            int scelta2 = Integer.parseInt(sc.nextLine());

                            if (scelta2 == 1) {
                                System.out.println();
                                mezzoDAO.aggiungiMezzo(new Tram());
                                System.out.println("Tram aggiunto correttamente");
                                System.out.println();
                            } else if (scelta2 == 2) {
                                System.out.println();
                                mezzoDAO.aggiungiMezzo(new Autobus());
                                System.out.println("Autobus aggiunto correttamente");
                                System.out.println();
                            }
                        } catch (Exception e) {
                            System.out.println("Scelta non valida");
                        }
                        break;
                    case 2: //Visualizza mezzi
                        menuVisualizzaMezzi();
                        break;
                    case 3: //Aggiungi tratta
                        System.out.println("Inserisci la zona di partenza della tratta:");
                        String zonaPartenza = sc.nextLine();
                        System.out.println("Inserisci la zona di arrivo della tratta:");
                        String zonaArrivo = sc.nextLine();
                        trattaDAO.aggiungiTratta(new Tratta(zonaPartenza, zonaArrivo));
                        System.out.println();
                        System.out.println("Tratta aggiunta correttamente");
                        System.out.println();
                        break;
                    case 4: //Aggiungi corsa
                        aggiungiCorsa();
                        break;
                   /* case 5:
                        System.out.println("Inserisci l'id del mezzo a cui assegnare la tratta:");
                        String idMezzo2 = sc.nextLine();
                        System.out.println("Inserisci l'id della tratta da assegnare al mezzo:");
                        String idTratta2 = sc.nextLine();

                        break;*/
                    case 5:
                        return;

                }

            } catch (Exception e) {
                System.out.println("Scelta non valida");
            }


        }
    }

    public void aggiungiCorsa() {
        Mezzo mezzoTrovato = null;
        Tratta trattaTrovata = null;
        try {
            System.out.println("Inserisci l'ID del mezzo relativo alla corsa:");
            String idMezzo = sc.nextLine();
            mezzoTrovato = mezzoDAO.findById(idMezzo);
        } catch (Exception e) {
            System.out.println();
            System.out.println("Mezzo non trovato");
            System.out.println();
            return;
        }

        try {
            System.out.println("Inserisci l'ID della tratta relativa alla corsa:");
            String idTratta = sc.nextLine();
            trattaTrovata = trattaDAO.findById(idTratta);
        } catch (Exception e) {
            System.out.println();
            System.out.println("Tratta non trovata");
            System.out.println();
            return;

        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            System.out.println("Inserisci la partenza della corsa (yyyy-MM-dd HH:mm): ");
            String dataPartenza = sc.nextLine();
            LocalDateTime inizioCorsa = LocalDateTime.parse(dataPartenza, formatter);

            System.out.println("Inserisci la fine della corsa (yyyy-MM-dd HH:mm): ");
            String dataFine = sc.nextLine();
            LocalDateTime fineCorsa = LocalDateTime.parse(dataFine, formatter);


            corsaDAO.aggiungiCorsa(new Corsa(inizioCorsa, fineCorsa, trattaTrovata, mezzoTrovato));
            System.out.println("Corsa aggiunta con successo");

                    /*        if (mezzoTrovato != null && trattaTrovata != null) {
                                System.out.println("Corsa aggiunta con successo!");
                            } else {
                                System.out.println("Mezzo o Tratta non trovati");
                            }*/
        } catch (DateTimeParseException e) {
            System.out.println("Formato della data non valido. Riprova.");
        }
    }

    public void menuVisualizzaMezzi() {

        while (true) {
            System.out.println("Visualizza mezzi");
            System.out.println("1. Visualizza tutti i mezzi");
            System.out.println("2. Visualizza mezzi in servizio");
            System.out.println("3. Visualizza mezzi fuori servizio");
            System.out.println("4. Controlla attività mezzo");
            System.out.println("5. Controlla produttività mezzo per verificare quanti biglietti sono stati validati per un mezzo");
            System.out.println("6. Lista corse per tratta di un mezzo");
            System.out.println("7. Torna indietro");
            int scelta = Integer.parseInt(sc.nextLine());

            try {
                switch (scelta) {
                    case 1:
                        if (mezzoDAO.findAll().isEmpty()) {
                            System.out.println("Nessun mezzo presente");
                        } else {
                            System.out.println();
                            System.out.println("Lista dei mezzi:");
                            System.out.println();
                            mezzoDAO.findAll().forEach(System.out::println);
                        }
                        break;
                    case 2:
                        if (mezzoDAO.findAll().stream().noneMatch(Mezzo::isInServizio)) {
                            System.out.println("Nessun mezzo in servizio");
                        } else {
                            System.out.println();
                            System.out.println("Lista dei mezzi in servizio:");
                            System.out.println();
                            mezzoDAO.findAll().stream().filter(Mezzo::isInServizio).forEach(System.out::println);
                            System.out.println();
                            System.out.println("Totale dei mezzi in servizio: " + mezzoDAO.findAll().stream().filter(Mezzo::isInServizio).count());
                            System.out.println();
                        }
                        break;
                    case 3:
                        if (mezzoDAO.findAll().stream().filter(m -> !m.isInServizio()).count() == 0) {
                            System.out.println("Nessun mezzo fuori servizio");
                        } else {
                            System.out.println();
                            System.out.println("Lista dei mezzi fuori servizio:");
                            System.out.println();
                            mezzoDAO.findAll().stream().filter(m -> !m.isInServizio()).forEach(System.out::println);
                            System.out.println();
                            System.out.println("Totale dei mezzi fuori servizio: " + mezzoDAO.findAll().stream().filter(m -> !m.isInServizio()).count());
                            System.out.println();
                        }
                        break;
                    case 4:
                        Mezzo mezzoTrovato = null;
                        try {
                            System.out.println("Inserisci l'id del mezzo:");
                            String idMezzo = sc.nextLine();
                            mezzoTrovato = mezzoDAO.findById(idMezzo);
                        } catch (Exception e) {
                            System.out.println();
                            System.out.println("Mezzo non trovato");
                            System.out.println();
                            break;
                        }
                        List<PeriodoServizioManutenzione> periodiServiziManutenzione = periodoServizioManutenzioneDAO.trovaPeriodoServizioManutenzioneMezzo(mezzoTrovato);

                        if (periodiServiziManutenzione.isEmpty()) {
                            System.out.println();
                            System.out.println("Il mezzo non ha periodi di manutenzione");
                            System.out.println();
                            break;
                        } else {
                            System.out.println();
                            System.out.println("Periodi di manutenzione del mezzo con id: " + mezzoTrovato.getId() + ":");
                            System.out.println();
                            periodiServiziManutenzione.forEach(System.out::println);
                        }

                        break;
                    case 5:
                        Mezzo mezzoTrovato2 = null;
                        try {
                            System.out.println("Inserisci l'id del mezzo:");
                            String idMezzo2 = sc.nextLine();
                            mezzoTrovato2 = mezzoDAO.findById(idMezzo2);
                        } catch (Exception e) {
                            System.out.println();
                            System.out.println("Mezzo non trovato");
                            System.out.println();
                            break;
                        }
                        System.out.println();
                        System.out.println("Numero totale di biglietti validati per il mezzo con id: " + mezzoTrovato2.getId() + ": " + validazioneDAO.conteggioTitoliDiViaggioValidatiPerUnMezzo(mezzoTrovato2));
                        System.out.println();

                        break;
                    case 6:

                        Mezzo mezzoTrovato4 = null;
                        Tratta trattaTrovata = null;
                        try {
                            System.out.println("Inserisci l'ID del mezzo:");
                            String idMezzo = sc.nextLine();
                            mezzoTrovato4 = mezzoDAO.findById(idMezzo);
                        } catch (Exception e) {
                            System.out.println("Mezzo non trovato");
                            return;
                        }

                        try {
                            System.out.println("Inserisci l'ID della tratta:");
                            String idTratta = sc.nextLine();
                            trattaTrovata = trattaDAO.findById(idTratta);
                        } catch (Exception e) {
                            System.out.println("Tratta non trovata");
                            return;

                        }
                       /* System.out.println();
                        System.out.println("Numero di corse per la tratta " + trattaTrovata.getId() + " per il mezzo con id: " + mezzoTrovato4.getId() + ": " + trattaDAO.conteggioCorsePerMezzoPerTratta(trattaTrovata, mezzoTrovato4));
                        System.out.println();*/
                        long count = trattaDAO.conteggioCorsePerMezzoPerTratta(trattaTrovata, mezzoTrovato4);
                        if (count == 0) {
                            System.out.println();
                            System.out.println("Nessuna corsa per la tratta " + trattaTrovata.getId() + " per il mezzo con id: " + mezzoTrovato4.getId());
                            System.out.println();
                        } else {
                            System.out.println();
                            System.out.println("Numero di corsa per la tratta " + trattaTrovata.getId() + " per il mezzo con id: " + mezzoTrovato4.getId() + ": " + count);
                            System.out.println();
                        }

                        break;
                    case 7:
                        return;
                }

            } catch (Exception e) {
                System.out.println("Scelta non valida");
            }

        }

    }

    public void menuUtente() {
        while (true) {
            System.out.println("Menu Utente - Scegli un opzione");
            System.out.println("1. Utente Nuovo");
            System.out.println("2. Utente Registrato");
            System.out.println("3. Torna indietro");
            int scelta = Integer.parseInt(sc.nextLine());
            try {
                switch (scelta) {
                    case 1:
                        gestioneUtenteNuovo();
                        break;
                    case 2:
                        gestioneUtenteRegistrato();
                        break;
                    case 3:
                        return;
                }
            } catch (Exception e) {
                System.out.println("Scelta non valida");
            }

        }

    }

    public void acquistaBiglietto() throws Exception {
        Random rand = new Random();
        System.out.println();
        System.out.println("Da quale punto di vendita emettere il biglietto?");
        System.out.println("1. Distributore Automatico");
        System.out.println("2. Rivenditore Autorizzato");
        int scelta1 = Integer.parseInt(sc.nextLine());
        if (scelta1 == 1) {
            List<DistributoreAutomatico> listaDistributoriFromDB = puntoVenditaDAO.listaDistributoriAutomaticiAttivi();
            Biglietto bigliettoEmesso = puntoVenditaDAO.emettiBiglietto(listaDistributoriFromDB.get(rand.nextInt(listaDistributoriFromDB.size())));
            titoloDiViaggioDAO.aggiungiTitoloDiViaggio(bigliettoEmesso);
            System.out.println();
            System.out.println("Biglietto emesso con id: " + bigliettoEmesso.getId());
            System.out.println();
            TitoloDiViaggio bigliettoEmessoFromDB = titoloDiViaggioDAO.findById(bigliettoEmesso.getId().toString());
            System.out.println("Scegli tra le seguenti opzioni:");
            System.out.println("1. Valida Biglietto");
            System.out.println("2. Torna indietro");
            int sceltaValidazione = Integer.parseInt(sc.nextLine());
            try {
                switch (sceltaValidazione) {
                    case 1:
                        List<Mezzo> mezziFromDB = mezzoDAO.findAll().stream().filter(Mezzo::isInServizio).toList();
                        validazioneDAO.vidimazioneBiglietto((Biglietto) bigliettoEmessoFromDB, mezziFromDB.get(rand.nextInt(mezziFromDB.size())));
                        System.out.println();
                        System.out.println("Biglietto validato con successo");
                        System.out.println();
                        break;
                    case 2:
                        return;
                }
            } catch (Exception e) {
                System.out.println("Scelta non valida");
            }
        } else if (scelta1 == 2) {
            List<Rivenditore> listaRivenditoriFromDB = puntoVenditaDAO.listaRivenditoriConLicenza();
            Biglietto bigliettoEmesso = puntoVenditaDAO.emettiBiglietto(listaRivenditoriFromDB.get(rand.nextInt(listaRivenditoriFromDB.size())));
            titoloDiViaggioDAO.aggiungiTitoloDiViaggio(bigliettoEmesso);
            System.out.println();
            System.out.println("Biglietto emesso con id: " + bigliettoEmesso.getId());
            System.out.println();
            TitoloDiViaggio bigliettoEmessoFromDB = titoloDiViaggioDAO.findById(bigliettoEmesso.getId().toString());
            System.out.println("Scegli tra le seguenti opzioni:");
            System.out.println("1. Valida Biglietto");
            System.out.println("2. Torna indietro");
            int sceltaValidazione = Integer.parseInt(sc.nextLine());
            try {
                switch (sceltaValidazione) {
                    case 1:
                        List<Mezzo> mezziFromDB = mezzoDAO.findAll().stream().filter(Mezzo::isInServizio).toList();
                        validazioneDAO.vidimazioneBiglietto((Biglietto) bigliettoEmessoFromDB, mezziFromDB.get(rand.nextInt(mezziFromDB.size())));
                        System.out.println();
                        System.out.println("Biglietto validato con successo");
                        System.out.println();
                        break;
                    case 2:
                        return;
                }
            } catch (Exception e) {
                System.out.println("Scelta non valida");
            }

        }

    }

    public void acquistaAbbonamento(Utente utenteFromDB) throws Exception {
        Random rand = new Random();
        Tessera nuovaTessera = new Tessera(LocalDate.now());
        tesseraDAO.aggiungiTessera(nuovaTessera, utenteFromDB);
        System.out.println();
        System.out.println("Tessera emessa con id: " + nuovaTessera.getId());
        System.out.println();
        System.out.println("Da quale punto di vendita emettere l'abbonamento?");
        System.out.println("1. Distributore Automatico");
        System.out.println("2. Rivenditore Autorizzato");
        int scelta2 = Integer.parseInt(sc.nextLine());
        if (scelta2 == 1) {
            List<DistributoreAutomatico> listaDistributoriFromDB = puntoVenditaDAO.listaDistributoriAutomaticiAttivi();
            System.out.println("Selezionare il tipo di abbonamento:");
            System.out.println("1. Mensile");
            System.out.println("2. Settimanale");
            int scelta3 = Integer.parseInt(sc.nextLine());
            if (scelta3 == 1) {
                Abbonamento abbonamentoEmesso = puntoVenditaDAO.emettiAbbonamento(listaDistributoriFromDB.get(rand.nextInt(listaDistributoriFromDB.size())), utenteFromDB, TipoAbbonamento.MENSILE);
                titoloDiViaggioDAO.aggiungiTitoloDiViaggio(abbonamentoEmesso);
                System.out.println();
                System.out.println("Abbonamento Mensile emesso con successo");
                System.out.println();
                TitoloDiViaggio abbonamentoEmessoFromDB = titoloDiViaggioDAO.findById(abbonamentoEmesso.getId().toString());
                System.out.println("Scegli tra le seguenti opzioni:");
                System.out.println("1. Valida Abbonamento");
                System.out.println("2. Torna indietro");
                int sceltaValidazione = Integer.parseInt(sc.nextLine());
                try {
                    switch (sceltaValidazione) {
                        case 1:
                            List<Mezzo> mezziFromDB = mezzoDAO.findAll().stream().filter(Mezzo::isInServizio).toList();
                            validazioneDAO.vidimazioneAbbonamento((Abbonamento) abbonamentoEmessoFromDB, mezziFromDB.get(rand.nextInt(mezziFromDB.size())));
                            System.out.println();
                            System.out.println("Abbonamento validato con successo");
                            System.out.println();
                            break;
                        case 2:
                            return;
                    }
                } catch (Exception e) {
                    System.out.println("Scelta non valida");
                }
            } else if (scelta3 == 2) {
                Abbonamento abbonamentoEmesso = puntoVenditaDAO.emettiAbbonamento(listaDistributoriFromDB.get(rand.nextInt(listaDistributoriFromDB.size())), utenteFromDB, TipoAbbonamento.SETTIMANALE);
                titoloDiViaggioDAO.aggiungiTitoloDiViaggio(abbonamentoEmesso);
                System.out.println();
                System.out.println("Abbonamento Settimanale emesso con successo");
                System.out.println();
                TitoloDiViaggio abbonamentoEmessoFromDB = titoloDiViaggioDAO.findById(abbonamentoEmesso.getId().toString());
                System.out.println("Scegli tra le seguenti opzioni:");
                System.out.println("1. Valida Abbonamento");
                System.out.println("2. Torna indietro");
                int sceltaValidazione = Integer.parseInt(sc.nextLine());
                try {
                    switch (sceltaValidazione) {
                        case 1:
                            List<Mezzo> mezziFromDB = mezzoDAO.findAll().stream().filter(Mezzo::isInServizio).toList();
                            validazioneDAO.vidimazioneAbbonamento((Abbonamento) abbonamentoEmessoFromDB, mezziFromDB.get(rand.nextInt(mezziFromDB.size())));
                            System.out.println();
                            System.out.println("Abbonamento validato con successo");
                            System.out.println();
                            break;
                        case 2:
                            return;
                    }
                } catch (Exception e) {
                    System.out.println("Scelta non valida");
                }
            }

        } else if (scelta2 == 2) {
            List<Rivenditore> listaRivenditoriFromDB = puntoVenditaDAO.listaRivenditoriConLicenza();
            System.out.println("Selezionare il tipo di abbonamento:");
            System.out.println("1. Mensile");
            System.out.println("2. Settimanale");
            int scelta3 = Integer.parseInt(sc.nextLine());
            if (scelta3 == 1) {
                Abbonamento abbonamentoEmesso = puntoVenditaDAO.emettiAbbonamento(listaRivenditoriFromDB.get(rand.nextInt(listaRivenditoriFromDB.size())), utenteFromDB, TipoAbbonamento.MENSILE);
                titoloDiViaggioDAO.aggiungiTitoloDiViaggio(abbonamentoEmesso);
                System.out.println();
                System.out.println("Abbonamento Mensile emesso con successo");
                System.out.println();
                TitoloDiViaggio abbonamentoEmessoFromDB = titoloDiViaggioDAO.findById(abbonamentoEmesso.getId().toString());
                System.out.println("Scegli tra le seguenti opzioni:");
                System.out.println("1. Valida Abbonamento");
                System.out.println("2. Torna indietro");
                int sceltaValidazione = Integer.parseInt(sc.nextLine());
                try {
                    switch (sceltaValidazione) {
                        case 1:
                            List<Mezzo> mezziFromDB = mezzoDAO.findAll().stream().filter(Mezzo::isInServizio).toList();
                            validazioneDAO.vidimazioneAbbonamento((Abbonamento) abbonamentoEmessoFromDB, mezziFromDB.get(rand.nextInt(mezziFromDB.size())));
                            System.out.println();
                            System.out.println("Abbonamento validato con successo");
                            System.out.println();
                            break;
                        case 2:
                            return;
                    }
                } catch (Exception e) {
                    System.out.println("Scelta non valida");
                }
            } else if (scelta3 == 2) {
                Abbonamento abbonamentoEmesso = puntoVenditaDAO.emettiAbbonamento(listaRivenditoriFromDB.get(rand.nextInt(listaRivenditoriFromDB.size())), utenteFromDB, TipoAbbonamento.SETTIMANALE);
                titoloDiViaggioDAO.aggiungiTitoloDiViaggio(abbonamentoEmesso);
                System.out.println();
                System.out.println("Abbonamento Settimanale emesso con successo");
                System.out.println();
                TitoloDiViaggio abbonamentoEmessoFromDB = titoloDiViaggioDAO.findById(abbonamentoEmesso.getId().toString());
                System.out.println("Scegli tra le seguenti opzioni:");
                System.out.println("1. Valida Abbonamento");
                System.out.println("2. Torna indietro");
                int sceltaValidazione = Integer.parseInt(sc.nextLine());
                try {
                    switch (sceltaValidazione) {
                        case 1:
                            List<Mezzo> mezziFromDB = mezzoDAO.findAll().stream().filter(Mezzo::isInServizio).toList();
                            validazioneDAO.vidimazioneAbbonamento((Abbonamento) abbonamentoEmessoFromDB, mezziFromDB.get(rand.nextInt(mezziFromDB.size())));
                            System.out.println();
                            System.out.println("Abbonamento validato con successo");
                            System.out.println();
                            break;
                        case 2:
                            return;
                    }
                } catch (Exception e) {
                    System.out.println("Scelta non valida");
                }
            }
        }
    }

    public void gestioneUtenteNuovo() {
        System.out.println("Inserisci il tuo nome: ");
        String nome = sc.nextLine();
        System.out.println("Inserisci il tuo cognome:");
        String cognome = sc.nextLine();
        Utente nuovoUtente = new Utente(nome, cognome);
        utenteDAO.aggiungiUtente(nuovoUtente);
        System.out.println();
        System.out.println("Benvenuto " + nuovoUtente.getNome() + " " + nuovoUtente.getCognome());
        System.out.println();
        Utente utenteFromDB = utenteDAO.findById(nuovoUtente.getId().toString());
        while (true) {
            System.out.println("Scegli un opzione:");
            System.out.println("1. Acquista Biglietto");
            System.out.println("2. Acquista Abbonamento");
            System.out.println("3. Valida Biglietto");
            System.out.println("4. Valida Abbonamento");
            System.out.println("5. Torna indietro");
            int scelta = Integer.parseInt(sc.nextLine());
            try {
                switch (scelta) {
                    case 1:
                        acquistaBiglietto();
                        break;
                    case 2:
                        acquistaAbbonamento(utenteFromDB);
                        break;
                    case 3:
                        System.out.println("Sei già in possesso di un biglietto?");
                        System.out.println("1. Si");
                        System.out.println("2. No, voglio acquistare un biglietto");
                        System.out.println("3. Torna indietro");
                        int scelta2 = Integer.parseInt(sc.nextLine());
                        if (scelta2 == 1) {
                            validaBiglietto();

                        } else if (scelta2 == 2) {
                            acquistaBiglietto();
                        } else if (scelta2 == 3) {
                            return;
                        } else {
                            System.out.println("Scelta non valida");
                        }
                        break;
                    case 4:
                        System.out.println("Sei già in possesso di un abbonamento?");
                        System.out.println("1. Si");
                        System.out.println("2. No, voglio acquistare un abbonamento");
                        System.out.println("3. Torna indietro");
                        int scelta3 = Integer.parseInt(sc.nextLine());
                        if (scelta3 == 1) {
                            validaAbbonamento();

                        } else if (scelta3 == 2) {
                            acquistaAbbonamento(utenteFromDB);
                        } else if (scelta3 == 3) {
                            return;
                        } else {
                            System.out.println("Scelta non valida");
                        }
                        break;
                    case 5:
                        return;

                }
            } catch (Exception e) {
                System.out.println("Scelta non valida");
            }
        }
    }

    public void validaBiglietto() {

        Random rand = new Random();

        List<Mezzo> mezziInServizio = mezzoDAO.findAll().stream().filter(Mezzo::isInServizio).toList();
        if (mezziInServizio.isEmpty()) {
            System.out.println("Nessun mezzo in servizio disponibile.");
            return;
        }

        Mezzo mezzoCasualeFromDB = mezziInServizio.get(rand.nextInt(mezziInServizio.size()));
        Biglietto bigliettoFromDB = null;
        try {
            System.out.println("Inserisci l'ID del tuo biglietto per validarlo:");
            String idBiglietto = sc.nextLine();
            bigliettoFromDB = (Biglietto) titoloDiViaggioDAO.findById(idBiglietto);
        } catch (Exception e) {
            System.out.println();
            System.out.println("Biglietto non trovato");
            System.out.println();
            return;
        }
        if (bigliettoFromDB.isValido()) {
            try {
                validazioneDAO.vidimazioneBiglietto(bigliettoFromDB, mezzoCasualeFromDB);
                System.out.println();
                System.out.println("Biglietto valido, Buon Viaggio!");
                System.out.println();
            } catch (Exception e) {
                System.out.println();
                System.out.println("Validazione non riuscita");
                System.out.println();
            }
        } else {
            System.out.println();
            System.out.println("Biglietto non valido");
            System.out.println();
        }


    }

    public void validaAbbonamento() {
        Random rand = new Random();

        List<Mezzo> mezziInServizio = mezzoDAO.findAll().stream().filter(Mezzo::isInServizio).toList();
        if (mezziInServizio.isEmpty()) {
            System.out.println("Nessun mezzo in servizio disponibile.");
            return;
        }

        Mezzo mezzoCasualeFromDB = mezziInServizio.get(rand.nextInt(mezziInServizio.size()));
        Abbonamento abbonamentoFromDB;
        try {
            System.out.println("Inserisci l'ID del tuo abbonamento per validarlo:");
            String idAbbonamento = sc.nextLine();
            abbonamentoFromDB = (Abbonamento) titoloDiViaggioDAO.findById(idAbbonamento);
        } catch (Exception e) {
            System.out.println();
            System.out.println("Abbonamento non trovato");
            System.out.println();
            return;
        }

        if (abbonamentoFromDB.dataScadenzaAbbonamento().isAfter(LocalDate.now())) {
            try {
                validazioneDAO.vidimazioneAbbonamento(abbonamentoFromDB, mezzoCasualeFromDB);
                System.out.println();
                System.out.println("Abbonamento valido, Buon Viaggio!");
                System.out.println();
            } catch (Exception e) {
                System.out.println();
                System.out.println("Validazione non riuscita");
                System.out.println();
            }
        } else {
            System.out.println();
            System.out.println("Abbonamento non valido");
            System.out.println();
        }

    }

    public void gestioneUtenteRegistrato() {
        System.out.println("Inserisci il tuo codice utente:");
        String codiceUtente = sc.nextLine();
        Utente utenteFromDB = (Utente) utenteDAO.findById(codiceUtente);
        if (utenteFromDB.getNumeroTessera() == null) {
            System.out.println("Non hai un abbonamento associato, vorresti acquistarlo?");
            System.out.println("1. Si");
            System.out.println("2. No");
            int sceltaAcquisto = Integer.parseInt(sc.nextLine());
            try {
                switch (sceltaAcquisto) {
                    case 1:
                        acquistaAbbonamento(utenteFromDB);
                        break;
                    case 2:
                        menuUtente();
                        break;
                }
            } catch (Exception e) {
                System.out.println("Scelta non valida");
            }
        } else {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Questi sono i tuoi dati: ");
            System.out.println();
            System.out.println(utenteFromDB);
            System.out.println();
            System.out.println("Cosa vuoi fare?");
            System.out.println("1. Verifica scadenza abbonamento");
            System.out.println("2. Rinnova abbonamento");
            System.out.println("3. Torna indietro");
            int scelta = Integer.parseInt(scanner.nextLine().trim());
            try {
                switch (scelta) {
                    case 1:
                        verificaScadenzaAbbonamentoDiUtente(utenteFromDB);
                        break;
                    case 2:
                        rinnovaAbbonamentoGenerico(utenteFromDB);
                        break;
                    case 3:
                        return;
                }
            } catch (NumberFormatException e) {
                System.out.println("Scelta non valida");
            } catch (Exception e) {
                System.out.println("Errore: " + e.getMessage());

            }
        }
    }

    public void verificaScadenzaAbbonamentoDiUtente(Utente utente) {
        Tessera tesseraFromDB = tesseraDAO.findById(utente.getNumeroTessera().getId().toString().trim());
        List<Abbonamento> abbonamenti = titoloDiViaggioDAO.trovaTuttiGliAbbonamenti().stream().filter(abbonamento -> abbonamento.getTessera().getId().equals(tesseraFromDB.getId())).toList();
        if (tesseraFromDB != null && !abbonamenti.isEmpty()) {
            System.out.println("Scegli l'abbonamento che vuoi verificare:");
            for (int i = 0; i < abbonamenti.size(); i++) {
                System.out.println((i + 1) + ". " + abbonamenti.get(i));
            }
            int sceltaAbbonamento = Integer.parseInt(sc.nextLine());
            if (sceltaAbbonamento < 1 || sceltaAbbonamento > abbonamenti.size()) {
                System.out.println("Scelta non valida.");
                return;
            }
            Abbonamento abbonamento = abbonamenti.get(sceltaAbbonamento - 1);
            Abbonamento abbonamentoFromDB = (Abbonamento) titoloDiViaggioDAO.findById(abbonamento.getId().toString().trim());
            LocalDate dataScadenza = abbonamentoFromDB.getDataScadenza();
            if (dataScadenza.isBefore(LocalDate.now())) {
                System.out.println("Il tuo abbonamento e' scaduto");
                System.out.println("Vuoi rinnovarlo?");
                System.out.println("1. Si");
                System.out.println("2. No");
                int scelta2 = Integer.parseInt(sc.nextLine());
                switch (scelta2) {
                    case 1:
                        rinnovaAbbonamento(abbonamento);
                        break;
                    case 2:
                        break;
                }
            } else {
                System.out.println("Il tuo abbonamento è valido ancora per " + ChronoUnit.DAYS.between(LocalDate.now(), dataScadenza) + " giorni");
            }
        } else {
            System.out.println("Non hai abbonamenti");

        }

    }

    public void rinnovaAbbonamento(Abbonamento abbonamento) {
        System.out.println("Hai scelto di rinnovare l'abbonamento. Il costo è di 1000€.");
        System.out.println("Quanto vuoi rinnovare?");
        System.out.println("1. 7 giorni");
        System.out.println("2. 1 mese");

        int sceltaRinnovo = Integer.parseInt(sc.nextLine());
        LocalDate nuovaDataScadenza = LocalDate.now();

        switch (sceltaRinnovo) {
            case 1:
                nuovaDataScadenza = nuovaDataScadenza.plusDays(7);
                abbonamento.setDataScadenza(nuovaDataScadenza);
                abbonamento.setTipoAbbonamento(TipoAbbonamento.SETTIMANALE);
                break;
            case 2:
                nuovaDataScadenza = nuovaDataScadenza.plusMonths(1);
                abbonamento.setDataScadenza(nuovaDataScadenza);
                abbonamento.setTipoAbbonamento(TipoAbbonamento.MENSILE);
                break;
        }

        titoloDiViaggioDAO.aggiornaTitoloDiViaggio(abbonamento);

        System.out.println("Abbonamento rinnovato con successo fino al " + nuovaDataScadenza);
    }

    public void rinnovaAbbonamentoGenerico(Utente utente) {
        Tessera tesseraFromDB = tesseraDAO.findById(utente.getNumeroTessera().getId().toString().trim());
        List<Abbonamento> abbonamenti = titoloDiViaggioDAO.trovaTuttiGliAbbonamenti().stream().filter(abbonamento -> abbonamento.getTessera().getId().equals(tesseraFromDB.getId())).toList();
        if (tesseraFromDB != null && !abbonamenti.isEmpty()) {
            System.out.println("Scegli l'abbonamento che vuoi rinnovare:");
            for (int i = 0; i < abbonamenti.size(); i++) {
                System.out.println((i + 1) + ". " + abbonamenti.get(i));
            }
            int sceltaAbbonamento = Integer.parseInt(sc.nextLine());
            if (sceltaAbbonamento < 1 || sceltaAbbonamento > abbonamenti.size()) {
                System.out.println("Scelta non valida.");
                return;
            }
            Abbonamento abbonamento = abbonamenti.get(sceltaAbbonamento - 1);

            if (abbonamento.getDataScadenza().isBefore(LocalDate.now())) {
                rinnovaAbbonamento(abbonamento);
            } else {
                System.out.println("Il tuo abbonamento è valido per " + ChronoUnit.DAYS.between(LocalDate.now(), abbonamento.getDataScadenza()) + " giorni");
            }
        } else {
            System.out.println("Non hai abbonamenti");
        }
    }
}





