package francescocristiano.dao;

import francescocristiano.entities.mezzi.Mezzo;
import francescocristiano.entities.mezzi.PeriodoServizioManutenzione;
import francescocristiano.enums.AttivitaMezzo;
import francescocristiano.exceptions.NotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.time.LocalDate;
import java.util.UUID;

public class MezzoDAO {
    private final EntityManager em;

    public MezzoDAO(EntityManager em) {
        this.em = em;
    }

    public void aggiungiMezzo(Mezzo mezzo) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.persist(mezzo);
        transaction.commit();
        System.out.println("Mezzo salvato con successo nel database!");
    }

    public Mezzo findById(UUID id) {
        Mezzo mezzo = em.find(Mezzo.class, id);
        if (mezzo == null) throw new NotFoundException(id);
        return mezzo;
    }

    public void aggiungiManutenzione(Mezzo mezzo, LocalDate dataInizio, LocalDate dataFine, String descrizione) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        PeriodoServizioManutenzione manutenzione = new PeriodoServizioManutenzione(dataInizio, dataFine, AttivitaMezzo.IN_MANUTENZIONE, descrizione, mezzo);
        em.persist(manutenzione);

        mezzo.setInServizio(false);
        em.merge(mezzo);
        transaction.commit();
        System.out.println("Periodo di manutenzione aggiunto per il mezzo");
    }

    public void ripristinaServizio(Mezzo mezzo) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        if (mezzo.isInServizio()) {
            System.out.println("Il mezzo non è in manutenzione");
        } else {
            mezzo.setInServizio(true);
            em.merge(mezzo);


            PeriodoServizioManutenzione periodoServizio = new PeriodoServizioManutenzione(LocalDate.now(), null, AttivitaMezzo.IN_SERVIZIO, "Servizio ripristinato", mezzo);
            em.persist(periodoServizio);

            transaction.commit();
            System.out.println("Servizio ripristinato per il mezzo");
        }
    }

    
}
