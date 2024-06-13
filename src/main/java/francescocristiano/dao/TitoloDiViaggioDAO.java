package francescocristiano.dao;

import francescocristiano.entities.puntiVendita.PuntoVendita;
import francescocristiano.entities.titoliDiViaggio.TitoloDiViaggio;
import francescocristiano.exceptions.NotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.UUID;

public class TitoloDiViaggioDAO {
    private final EntityManager em;

    public TitoloDiViaggioDAO(EntityManager em) {
        this.em = em;
    }

    public TitoloDiViaggio findById(String id) {
        TitoloDiViaggio titoloDiViaggio = em.find(TitoloDiViaggio.class, UUID.fromString(id));
        if (titoloDiViaggio == null) throw new NotFoundException(id);
        return titoloDiViaggio;
    }

    public void aggiungiTitoloDiViaggio(TitoloDiViaggio titoloDiViaggio) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.persist(titoloDiViaggio);
        transaction.commit();
        System.out.println("Titolo di viaggio salvato con successo nel database!");
    }
}
