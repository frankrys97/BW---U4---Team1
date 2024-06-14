package francescocristiano.dao;

import francescocristiano.entities.mezzi.Mezzo;
import francescocristiano.entities.mezzi.PeriodoServizioManutenzione;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;

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
    }

    public List<PeriodoServizioManutenzione> trovaPeriodoServizioManutenzioneMezzo(Mezzo mezzo) {
        return em.createQuery("SELECT p FROM PeriodoServizioManutenzione p WHERE p.mezzo = :mezzo", PeriodoServizioManutenzione.class)
                .setParameter("mezzo", mezzo)
                .getResultList();
    }
}
