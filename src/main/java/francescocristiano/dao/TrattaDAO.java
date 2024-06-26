package francescocristiano.dao;

import francescocristiano.entities.mezzi.Mezzo;
import francescocristiano.entities.mezzi.Tratta;
import francescocristiano.exceptions.NotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;
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
    }

    public Tratta findById(String id) {
        Tratta tratta = em.find(Tratta.class, UUID.fromString(id));
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

    public List<Tratta> findAll() {
        return em.createQuery("SELECT t FROM Tratta t", Tratta.class).getResultList();
    }

    public List<Mezzo> trovaMezziPerTratta(Tratta tratta) {
        return em.createQuery("SELECT c.mezzo FROM Corsa c WHERE c.tratta = :tratta", Mezzo.class).setParameter("tratta", tratta)
                .getResultList();
    }

}
