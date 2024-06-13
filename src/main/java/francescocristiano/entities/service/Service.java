package francescocristiano.entities.service;

import com.github.javafaker.Faker;
import francescocristiano.dao.*;
import francescocristiano.entities.mezzi.*;
import francescocristiano.entities.utenti.Tessera;
import francescocristiano.entities.utenti.Utente;
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


    public Service(EntityManager em) {
        this.em = em;
        this.trattaDAO = new TrattaDAO(em);
        this.corsaDAO = new CorsaDAO(em);
        this.mezzoDAO = new MezzoDAO(em);
        this.utenteDAO = new UtenteDAO(em);
        this.tesseraDAO = new TesseraDAO(em);
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

        for(int i = 0; i < 5; i++){
            Utente utente = new Utente(faker.name().firstName(),faker.name().lastName());
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

            List<Tratta> tratte = trattaDAO.findAll();
            for (int j = 0; j < 10; j++) {
                LocalDateTime oraPartenza = LocalDateTime.of(2024, rand.nextInt(12) + 1, rand.nextInt(28) + 1, rand.nextInt(24), rand.nextInt(60));
                corsaDAO.aggiungiCorsa(new Corsa(oraPartenza, oraPartenza.plusMinutes(rand.nextInt(15, 300)), tratta, mezzi.get(rand.nextInt(mezzi.size()))));
            }
            Duration tempoMedio = corsaDAO.calcolaTempioMedioPercorrenza(tratta);
            tratta.setTempo_medio_percorrenza(tempoMedio.toMinutes());
            trattaDAO.aggiornaTratta(tratta);
        }
    }
}

