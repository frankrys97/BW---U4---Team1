package francescocristiano.dao;

import francescocristiano.entities.utenti.Tessera;
import francescocristiano.entities.utenti.Utente;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class TesseraDAO {
    private final EntityManager em;

    public TesseraDAO(EntityManager em) {
        this.em = em;
    }

    public void aggiungiTessera(Tessera tessera, Utente utente) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        tessera.setUtente(utente);
        utente.setNumeroTessera(tessera);
        em.persist(tessera);
        em.merge(utente);
        transaction.commit();
        System.out.println("Tessera di " + tessera.getUtente().getNome() + ", " + tessera.getUtente().getCognome() + " salvata con successo nel database!");
    }
}
