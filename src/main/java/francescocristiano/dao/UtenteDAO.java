package francescocristiano.dao;

import francescocristiano.entities.utenti.Utente;
import francescocristiano.exceptions.NotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;
import java.util.UUID;

public class UtenteDAO {
    private final EntityManager em;

    public UtenteDAO(EntityManager em) {
        this.em = em;
    }

    public void aggiungiUtente(Utente utente) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.persist(utente);
        transaction.commit();
    }

    public Utente findById(String id) {
        Utente utente = em.find(Utente.class, UUID.fromString(id));
        if (utente == null) throw new NotFoundException(id);
        return utente;
    }

    public List<Utente> findListaUtentiConAbbonamento() {
        return em.createQuery("SELECT u FROM Utente u WHERE u.numeroTessera IS NOT NULL", Utente.class).getResultList();
    }

    public List<Utente> findAll() {
        return em.createQuery("SELECT u FROM Utente u", Utente.class).getResultList();
    }
}
