package francescocristiano.dao;

import francescocristiano.entities.mezzi.PeriodoServizioManutenzione;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class PeriodoServizioManutenzioneDAO {
    private final EntityManager em;

    public PeriodoServizioManutenzioneDAO(EntityManager em) {
        this.em = em;
    }

    public void aggiungiPeriodoServizioManutenzione(PeriodoServizioManutenzione periodoServizioManutenzione) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.persist(periodoServizioManutenzione);
        transaction.commit();
        System.out.println("Periodo di servizio/manutenzione del mezzo salvato con successo nel database!");
    }
}
