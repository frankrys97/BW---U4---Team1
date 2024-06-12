package francescocristiano.dao;

import francescocristiano.entities.mezzi.Mezzo;
import francescocristiano.entities.mezzi.Tratta;
import francescocristiano.exceptions.NotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.UUID;

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

    public Tratta findById(UUID id) {
        Tratta tratta = em.find(Tratta.class, id);
        if (tratta == null) throw new NotFoundException(id);
        return tratta;
    }

    public long conteggioCorsePerMezzoPerTratta(Tratta tratta, Mezzo mezzo) {
        return em.createQuery("SELECT COUNT(c) FROM Corsa c WHERE c.tratta = :tratta AND c.mezzo = :mezzo", Long.class).setParameter("tratta", tratta).setParameter("mezzo", mezzo).getSingleResult();
    }

    public void aggiornaTratta(Tratta tratta) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.merge(tratta);
        transaction.commit();
    }


}
