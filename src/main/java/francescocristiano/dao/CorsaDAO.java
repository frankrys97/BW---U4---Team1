package francescocristiano.dao;

import francescocristiano.entities.mezzi.Corsa;
import francescocristiano.entities.mezzi.Tratta;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class CorsaDAO {
    private final EntityManager em;

    public CorsaDAO(EntityManager em) {
        this.em = em;
    }

    public void aggiungiCorsa(Corsa corsa) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.persist(corsa);
        transaction.commit();

        System.out.println("Corsa salvata con successo nel database!");
    }

    public Duration calcolaTempioMedioPercorrenza(Tratta tratta) {
        List<Corsa> corse = em.createQuery("SELECT c FROM Corsa c WHERE c.tratta = :tratta", Corsa.class).setParameter("tratta", tratta).getResultList();
        if (corse.isEmpty()) {
            return Duration.ZERO;
        }

        long totaleMinuti = 0;
        for (Corsa corsa : corse) {
            LocalDateTime inizio = corsa.getInizioCorsa();
            LocalDateTime fine = corsa.getFineCorsa();

            if (fine.isBefore(inizio)) {
                fine = fine.plusDays(1);
            }

            Duration durata = Duration.between(inizio, fine);
            totaleMinuti += durata.toMinutes();
        }

        long tempoMedioMinuti = totaleMinuti / corse.size();
        return Duration.ofMinutes(tempoMedioMinuti);
    }
}
