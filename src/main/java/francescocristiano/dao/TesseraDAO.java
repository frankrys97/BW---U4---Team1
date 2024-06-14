package francescocristiano.dao;

import francescocristiano.entities.utenti.Tessera;
import francescocristiano.entities.utenti.Utente;
import francescocristiano.exceptions.NotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.UUID;

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

    public Tessera findById(String id) {
        Tessera tessera = em.find(Tessera.class, UUID.fromString(id));
        if (tessera == null) throw new NotFoundException(id);
        return tessera;
    }


}
