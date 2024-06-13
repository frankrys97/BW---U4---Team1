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
                        System.out.println();
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
        String password = sc.nextLine();
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
                        System.out.println("La lista di Distributori Automatici attivi è la seguente: ");
                        puntoVenditaDAO.listaDistributoriAutomaticiAttivi().forEach(System.out::println);
                        System.out.println();
                        System.out.println("Il numero totale di Distributori Automatici attivi è: " + puntoVenditaDAO.listaDistributoriAutomaticiAttivi().size());
                        break;
                    case 2:
                        System.out.println("La lista di Distributori Automatici Fuori Servizio è la seguente: ");
                        puntoVenditaDAO.listaDistributoriAutomaticiNonAttivi().forEach(System.out::println);
                        System.out.println();
                        System.out.println("Il numero totale di Distributori Automatici Fuori Servizio è: " + puntoVenditaDAO.listaDistributoriAutomaticiNonAttivi().size());
                        break;
                    case 3:
                        System.out.println("La lista di Rivenditori Con Licenza è la seguente: ");
                        puntoVenditaDAO.listaRivenditoriConLicenza().forEach(System.out::println);
                        System.out.println();
                        System.out.println("Il numero totale di Rivenditori Con Licenza è: " + puntoVenditaDAO.listaRivenditoriConLicenza().size());
                        break;
                    case 4:
                        System.out.println("La lista di Rivenditori Senza Licenza è la seguente: ");
                        puntoVenditaDAO.listaRivenditoriSenzaLicenza().forEach(System.out::println);
                        System.out.println();
                        System.out.println("Il numero totale di Rivenditori Senza Licenza è: " + puntoVenditaDAO.listaRivenditoriSenzaLicenza().size());
                        break;
                    case 5:
                        System.out.println("Inserisci l'id del punto vendita per visualizzare il numero di biglietti e/o abbonamenti emessi:");
                        String idPuntoVendita = sc.nextLine();
                        PuntoVendita puntoVenditaTrovato = puntoVenditaDAO.findById(idPuntoVendita);
                        System.out.println("Il numero di biglietti e/o abbonamenti emessi per il punto vendita con id " + idPuntoVendita + " è: " + puntoVenditaDAO.conteggioTitoliDiViaggioEmessiPerPuntoVendita(puntoVenditaTrovato));
                        break;
                    case 6:
                        System.out.println("Inserisci l'intervallo di tempo per visualizzare il numero di biglietti e/o abbonamenti emessi per un determinato periodo:");
                        System.out.println("Data inizio (yyyy-mm-dd):");
                        LocalDate dataInizio = LocalDate.parse(sc.nextLine());
                        System.out.println("Data fine (yyyy-mm-dd):");
                        LocalDate dataFine = LocalDate.parse(sc.nextLine());
                        System.out.println("Il numero di biglietti e/o abbonamenti emessi per il periodo selezionato è: " + puntoVenditaDAO.conteggioTitoliDiViaggioEmessiPerData(dataInizio, dataFine));
                        break;
                    case 7:
                        System.out.println("Inserisci l'id del punto vendita:");
                        String idPuntoVendita2 = sc.nextLine();
                        PuntoVendita puntoVenditaTrovato2 = puntoVenditaDAO.findById(idPuntoVendita2);
                        System.out.println("Inserisci la data d'inizio del periodo da verificare (yyyy-mm-dd):");
                        LocalDate dataInizio2 = LocalDate.parse(sc.nextLine());
                        System.out.println("Inserisci la data di fine del periodo da verificare (yyyy-mm-dd):");
                        LocalDate dataFine2 = LocalDate.parse(sc.nextLine());
                        System.out.println("Il numero di biglietti emessi dal punto vendita con id " + idPuntoVendita2 + " per il periodo selezionato è: " + puntoVenditaDAO.conteggioTitoliDiViaggioEmessiPerDataPerPuntoVendita(dataInizio2, dataFine2, puntoVenditaTrovato2));
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
            System.out.println("1. Aggiungi mezzo");
            System.out.println("2. Visualizza mezzi");
            System.out.println("3. Aggiungi tratta");
            System.out.println("4. Aggiungi corsa");
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

                        int scelta2 = Integer.parseInt(sc.nextLine());
                        if (scelta2 == 1) {
                            mezzoDAO.aggiungiMezzo(new Tram());
                            System.out.println("Tram aggiunto correttamente");
                        } else if (scelta2 == 2) {
                            mezzoDAO.aggiungiMezzo(new Autobus());
                            System.out.println("Autobus aggiunto correttamente");
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
                        break;
                    case 4: //Aggiungi corsa
                        System.out.println("Inserisci l'ID del mezzo relativo alla corsa:");
                        String idMezzo = sc.nextLine();
                        System.out.println("Inserisci l'ID della tratta relativa alla corsa:");
                        String idTratta = sc.nextLine();
                        System.out.println("Inserisci la partenza della corsa (yyyy-MM-dd HH:mm:): ");
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:");
                        LocalDateTime inizioCorsa = LocalDateTime.parse(sc.nextLine(), formatter);
                        System.out.println("Inserisci la fine della corsa (yyyy-MM-dd HH:mm:): ");
                        LocalDateTime fineCorsa = LocalDateTime.parse(sc.nextLine(), formatter);
                        Mezzo mezzoTrovato = mezzoDAO.findById(idMezzo);
                        Tratta trattaTrovata = trattaDAO.findById(idTratta);
                        if (mezzoTrovato != null && trattaTrovata != null) {
                            corsaDAO.aggiungiCorsa(new Corsa(inizioCorsa, fineCorsa, trattaTrovata, mezzoTrovato));
                        } else {
                            System.out.println("Mezzo o Tratta non trovati");
                        }
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
                            System.out.println("Lista dei mezzi:");
                            System.out.println();
                            mezzoDAO.findAll().forEach(System.out::println);
                        }
                        break;
                    case 2:
                        if (mezzoDAO.findAll().stream().noneMatch(Mezzo::isInServizio)) {
                            System.out.println("Nessun mezzo in servizio");
                        } else {
                            System.out.println("Lista dei mezzi in servizio:");
                            System.out.println();
                            mezzoDAO.findAll().stream().filter(Mezzo::isInServizio).forEach(System.out::println);
                        }
                        break;
                    case 3:
                        if (mezzoDAO.findAll().stream().filter(m -> !m.isInServizio()).count() == 0) {
                            System.out.println("Nessun mezzo fuori servizio");
                        } else {
                            System.out.println("Lista dei mezzi fuori servizio:");
                            System.out.println();
                            mezzoDAO.findAll().stream().filter(m -> !m.isInServizio()).forEach(System.out::println);
                        }
                        break;
                    case 4:
                        System.out.println("Inserisci l'id del mezzo:");
                        String idMezzo = sc.nextLine();
                        Mezzo mezzoTrovato = mezzoDAO.findById(idMezzo);
                        if (mezzoTrovato != null) {
                            System.out.println("Elenco dei servizi di periodo/manutenzione per il mezzo con id " + mezzoTrovato.getId() + ":");
                            System.out.println();
                            periodoServizioManutenzioneDAO.trovaPeriodoServizioManutenzioneMezzo(mezzoTrovato).forEach(System.out::println);
                        } else {
                            System.out.println("Mezzo non trovato");
                        }
                        break;
                    case 5:
                        System.out.println("Inserisci l'id del mezzo:");
                        String idMezzo2 = sc.nextLine();
                        Mezzo mezzoTrovato2 = mezzoDAO.findById(idMezzo2);
                        if (mezzoTrovato2 != null) {
                            System.out.println("Numero totale di biglietti validati per il mezzo con id: " + mezzoTrovato2.getId() + ": " + validazioneDAO.conteggioTitoliDiViaggioValidatiPerUnMezzo(mezzoTrovato2));
                        } else {
                            System.out.println("Mezzo non trovato");
                        }
                        break;
                    case 6:
                        System.out.println();
                        System.out.println("Inserisci l'id del mezzo:");
                        String idMezzo3 = sc.nextLine();
                        System.out.println("Inserisci l'id della tratta:");
                        String idTratta = sc.nextLine();

                        Tratta trattaTrovata = trattaDAO.findById(idTratta);
                        Mezzo mezzoTrovato3 = mezzoDAO.findById(idMezzo3);

                        if (mezzoTrovato3 != null && trattaTrovata != null) {
                            System.out.println("Numero di corse per la tratta " + trattaTrovata.getId() + " per il mezzo con id: " + mezzoTrovato3.getId() + ": " + trattaDAO.conteggioCorsePerMezzoPerTratta(trattaTrovata, mezzoTrovato3));
                        } else {
                            System.out.println("Mezzo o tratta non trovati");

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
    public void gestioneUtenteNuovo() {
        Random rand = new Random();
        System.out.println("Inserisci il tuo nome: ");
        String nome = sc.nextLine();
        System.out.println("Inserisci il tuo cognome:");
        String cognome = sc.nextLine();
        Utente nuovoUtente = new Utente(nome, cognome);
        utenteDAO.aggiungiUtente(nuovoUtente);
        Utente utenteFromDB = utenteDAO.findById(nuovoUtente.getId().toString());
        while (true) {
            System.out.println("Scegli un opzione:");
            System.out.println("1. Acquistare un biglietto");
            System.out.println("2. Acquistare un Abbonamento");
            System.out.println("3. Validare Biglietto");
            System.out.println("4. Validare Abbonamento");
            System.out.println("5. Torna indietro");
            int scelta = Integer.parseInt(sc.nextLine());
            try {
                switch (scelta) {
                    case 1:
                        System.out.println("Da quale punto di vendita emettere il biglietto?");
                        System.out.println("1. Distributore Automatico");
                        System.out.println("2. Rivenditore Autorizzato");
                        int scelta1 = Integer.parseInt(sc.nextLine());
                        if (scelta1 == 1) {
                            List<DistributoreAutomatico> listaDistributoriFromDB = puntoVenditaDAO.listaDistributoriAutomaticiAttivi();
                            Biglietto bigliettoEmesso = puntoVenditaDAO.emettiBiglietto(listaDistributoriFromDB.get(rand.nextInt(listaDistributoriFromDB.size())));
                            titoloDiViaggioDAO.aggiungiTitoloDiViaggio(bigliettoEmesso);
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
                                        break;
                                    case 2:
                                        return;
                                }
                            } catch (Exception e) {
                                System.out.println("Scelta non valida");
                            }

                        }
                        break;
                    case 2:
                        Tessera nuovaTessera = new Tessera(LocalDate.now());
                        tesseraDAO.aggiungiTessera(nuovaTessera, utenteFromDB);
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
                                Abbonamento abbonamentoEmesso = puntoVenditaDAO.emettiAbbonamento(listaDistributoriFromDB.get(rand.nextInt(listaDistributoriFromDB.size())), utenteFromDB, TipoAbbonamento.MENSILE );
                                titoloDiViaggioDAO.aggiungiTitoloDiViaggio(abbonamentoEmesso);
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
                                            break;
                                        case 2:
                                            return;
                                    }
                                } catch (Exception e) {
                                    System.out.println("Scelta non valida");
                                }
                            } else if (scelta3 == 2) {
                                Abbonamento abbonamentoEmesso = puntoVenditaDAO.emettiAbbonamento(listaDistributoriFromDB.get(rand.nextInt(listaDistributoriFromDB.size())), utenteFromDB, TipoAbbonamento.SETTIMANALE );
                                titoloDiViaggioDAO.aggiungiTitoloDiViaggio(abbonamentoEmesso);
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
                                Abbonamento abbonamentoEmesso = puntoVenditaDAO.emettiAbbonamento(listaRivenditoriFromDB.get(rand.nextInt(listaRivenditoriFromDB.size())), utenteFromDB, TipoAbbonamento.MENSILE );
                                titoloDiViaggioDAO.aggiungiTitoloDiViaggio(abbonamentoEmesso);
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
                                            break;
                                        case 2:
                                            return;
                                    }
                                } catch (Exception e) {
                                    System.out.println("Scelta non valida");
                                }
                            } else if (scelta3 == 2) {
                                Abbonamento abbonamentoEmesso = puntoVenditaDAO.emettiAbbonamento(listaRivenditoriFromDB.get(rand.nextInt(listaRivenditoriFromDB.size())), utenteFromDB, TipoAbbonamento.SETTIMANALE );
                                titoloDiViaggioDAO.aggiungiTitoloDiViaggio(abbonamentoEmesso);
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
                                            break;
                                        case 2:
                                            return;
                                    }
                                } catch (Exception e) {
                                    System.out.println("Scelta non valida");
                                }
                            }
                        }
                        break;
                    case 3:
                        System.out.println("Inserisci il biglietto da validare:");

                }
            } catch (Exception e) {
                System.out.println("Scelta non valida");
            }
        }
    }
}

