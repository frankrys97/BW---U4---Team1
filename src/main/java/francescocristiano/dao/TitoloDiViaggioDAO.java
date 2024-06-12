package francescocristiano.dao;

import francescocristiano.entities.titoliDiViaggio.TitoloDiViaggio;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class TitoloDiViaggioDAO {
    private final EntityManager em;

    public TitoloDiViaggioDAO(EntityManager em) {
        this.em = em;
    }

    public void aggiungiTitoloDiViaggio(TitoloDiViaggio titoloDiViaggio) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.persist(titoloDiViaggio);
        transaction.commit();
        System.out.println("Titolo di viaggio salvato con successo nel database!");
    }
}
