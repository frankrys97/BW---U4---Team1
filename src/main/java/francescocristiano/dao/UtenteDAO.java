package francescocristiano.dao;

import francescocristiano.entities.utenti.Utente;
import francescocristiano.exceptions.NotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.UUID;

public class UtenteDAO {
    private final EntityManager em;

    public UtenteDAO(EntityManager em) {
        this.em = em;
    }

    public void aggiungiUtente(Utente utente){
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.persist(utente);
        transaction.commit();
        System.out.println("L'utente " + utente.getNome() + ", " + utente.getCognome() + " salvato con successo nel database!");
    }

    public Utente findById(UUID id) {
        Utente utente = em.find(Utente.class, id);
        if (utente == null) throw new NotFoundException(id);
        return utente;
    }
}
