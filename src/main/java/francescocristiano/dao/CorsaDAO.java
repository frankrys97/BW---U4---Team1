package francescocristiano.dao;

import francescocristiano.entities.mezzi.Corsa;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

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
}
