package francescocristiano.entities.service;

import com.github.javafaker.Faker;
import francescocristiano.dao.*;
import francescocristiano.entities.mezzi.*;
import francescocristiano.entities.puntiVendita.DistributoreAutomatico;
import francescocristiano.entities.puntiVendita.Rivenditore;
import francescocristiano.entities.utenti.Tessera;
import francescocristiano.entities.utenti.Utente;
import francescocristiano.enums.AttivitaMezzo;
import francescocristiano.enums.StatusDistributore;
import jakarta.persistence.EntityManager;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Service {
    static Scanner sc = new Scanner(System.in);
    private EntityManager em;
    private TrattaDAO trattaDAO;
    private CorsaDAO corsaDAO;
    private MezzoDAO mezzoDAO;
    private UtenteDAO utenteDAO;
    private TesseraDAO tesseraDAO;
    private PeriodoServizioManutenzioneDAO periodoServizioManutenzioneDAO;
    private PuntoVenditaDAO puntoVenditaDAO;

    public Service(EntityManager em) {
        this.em = em;
        this.trattaDAO = new TrattaDAO(em);
        this.corsaDAO = new CorsaDAO(em);
        this.mezzoDAO = new MezzoDAO(em);
        this.utenteDAO = new UtenteDAO(em);
        this.tesseraDAO = new TesseraDAO(em);
        this.periodoServizioManutenzioneDAO = new PeriodoServizioManutenzioneDAO(em);
        this.puntoVenditaDAO = new PuntoVenditaDAO(em);
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

    public void inizializzaDataBase() {
        Random rand = new Random();
        Faker faker = new Faker();

        for (int i = 0; i < 15; i++) {
            Utente utente = new Utente(faker.name().firstName(), faker.name().lastName());
            utenteDAO.aggiungiUtente(utente);

            Utente utenteFromDB = utenteDAO.findById(utente.getId().toString());

            Tessera tessera = new Tessera(LocalDate.now());
            tesseraDAO.aggiungiTessera(tessera, utenteFromDB);
        }

        for (int i = 0; i < 5; i++) {
            Utente utente = new Utente(faker.name().firstName(), faker.name().lastName());
            utenteDAO.aggiungiUtente(utente);
        }

        for (int i = 0; i < 15; i++) {
            puntoVenditaDAO.aggiungiPuntoVendita(new DistributoreAutomatico(StatusDistributore.values()[rand.nextInt(StatusDistributore.values().length)]));
            puntoVenditaDAO.aggiungiPuntoVendita(new Rivenditore(faker.company().name(), rand.nextBoolean()));
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

            List<Tratta> tratte = trattaDAO.findAll();
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
                        System.out.println();
                        break;
                    case 2:
                        System.out.println();
                        break;
                    case 3:
                        System.out.println("Arrivederci");
                        resetDataBase();
                        break;
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
/*
                        munuGestioneParcoMezzi();
*/
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
                }
            } catch (Exception e) {

            }

        }

    }
}

