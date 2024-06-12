package francescocristiano.dao;

import francescocristiano.entities.utenti.Utente;
import francescocristiano.entities.validazioni.Validazione;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class ValidazioneDAO {
    private final EntityManager em;

    public ValidazioneDAO(EntityManager em) {
        this.em = em;
    }

    public void aggiungiValidazione(Validazione validazione){
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.persist(validazione);
        transaction.commit();
        System.out.println("Validazione salvata con successo nel database!");
    }
}
