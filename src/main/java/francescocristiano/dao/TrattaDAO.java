package francescocristiano.dao;

import francescocristiano.entities.mezzi.Tratta;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class TrattaDAO {
    private final EntityManager em;

    public TrattaDAO(EntityManager em) {
        this.em = em;
    }

    public void aggiungiTratta(Tratta tratta) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.persist(tratta);
        transaction.commit();
        System.out.println("La tratta da " + tratta.getZonaPartenza() + " a " + tratta.getCapolinea() + " è salvata con successo nel database!");
    }
}
