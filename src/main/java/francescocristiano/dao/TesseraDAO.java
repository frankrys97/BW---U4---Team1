package francescocristiano.dao;

import francescocristiano.entities.utenti.Tessera;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class TesseraDAO {
    private final EntityManager em;

    public TesseraDAO(EntityManager em) {
        this.em = em;
    }

    public void aggiungiTessera(Tessera tessera) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.persist(tessera);
        transaction.commit();
        System.out.println("Tessera di " + tessera.getUtente().getNome() + ", " + tessera.getUtente().getCognome() + " salvata con successo nel database!");
    }
}
