package francescocristiano.dao;

import francescocristiano.entities.puntiVendita.PuntoVendita;
import francescocristiano.exceptions.NotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.UUID;

public class PuntoVenditaDAO {

    private final EntityManager em;

    public PuntoVenditaDAO(EntityManager em) {
        this.em = em;
    }

    public void aggiungiPuntoVendita(PuntoVendita puntoVendita){
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.persist(puntoVendita);
        transaction.commit();
        System.out.println("Punto Vendita salvato con successo");
    }

    public PuntoVendita findById(UUID id) {
        PuntoVendita puntoVendita = em.find(PuntoVendita.class, id);
        if (puntoVendita == null) throw new NotFoundException(id);
        return puntoVendita;
    }
}
