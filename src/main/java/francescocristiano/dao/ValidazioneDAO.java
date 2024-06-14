package francescocristiano.dao;

import francescocristiano.entities.mezzi.Mezzo;
import francescocristiano.entities.titoliDiViaggio.Abbonamento;
import francescocristiano.entities.titoliDiViaggio.Biglietto;
import francescocristiano.entities.validazioni.Validazione;
import francescocristiano.entities.validazioni.ValidazioneAbbonamento;
import francescocristiano.entities.validazioni.ValidazioneBiglietto;
import francescocristiano.exceptions.NotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.time.LocalDate;
import java.util.UUID;

public class ValidazioneDAO {
    private final EntityManager em;

    public ValidazioneDAO(EntityManager em) {
        this.em = em;
    }

    public void aggiungiValidazione(Validazione validazione) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.persist(validazione);
        transaction.commit();
        System.out.println("Validazione salvata con successo nel database!");
    }

    public Validazione findById(String id) {
        Validazione validazione = em.find(Validazione.class, UUID.fromString(id));
        if (validazione == null) throw new NotFoundException(id);
        return validazione;
    }

    public void vidimazioneBiglietto(Biglietto biglietto, Mezzo mezzo) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        biglietto.setValido(false);
        em.merge(biglietto);
        ValidazioneBiglietto validazioneBiglietto = new ValidazioneBiglietto(mezzo, LocalDate.now(), biglietto);
        em.persist(validazioneBiglietto);
        transaction.commit();
        System.out.println("Biglietto validato con successo");
    }

    public void vidimazioneAbbonamento(Abbonamento abbonamento, Mezzo mezzo) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        ValidazioneAbbonamento validazioneAbbonamento = new ValidazioneAbbonamento(mezzo, LocalDate.now(), abbonamento);
        em.persist(validazioneAbbonamento);

        transaction.commit();
        System.out.println("Abbonamento validato con successo");
    }

    public long conteggioTitoliDiViaggioValidatiPerUnMezzo(Mezzo mezzo) {
        return em.createQuery("SELECT COUNT(v) FROM Validazione v WHERE v.mezzo = :mezzo", Long.class).setParameter("mezzo", mezzo).getSingleResult();
    }


}
