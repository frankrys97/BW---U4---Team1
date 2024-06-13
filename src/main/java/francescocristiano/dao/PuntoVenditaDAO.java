package francescocristiano.dao;

import francescocristiano.entities.puntiVendita.DistributoreAutomatico;
import francescocristiano.entities.puntiVendita.PuntoVendita;
import francescocristiano.entities.puntiVendita.Rivenditore;
import francescocristiano.entities.titoliDiViaggio.Abbonamento;
import francescocristiano.entities.titoliDiViaggio.Biglietto;
import francescocristiano.entities.utenti.Tessera;
import francescocristiano.entities.utenti.Utente;
import francescocristiano.enums.StatusDistributore;
import francescocristiano.enums.TipoAbbonamento;
import francescocristiano.exceptions.NotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class PuntoVenditaDAO {

    private final EntityManager em;
    private final TitoloDiViaggioDAO titoloDiViaggioDAO;

    public PuntoVenditaDAO(EntityManager em) {

        this.em = em;
        this.titoloDiViaggioDAO = new TitoloDiViaggioDAO(em);
    }

    public void aggiungiPuntoVendita(PuntoVendita puntoVendita) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.persist(puntoVendita);
        transaction.commit();
        System.out.println("Punto Vendita salvato con successo");
    }

    public PuntoVendita findById(String id) {
        PuntoVendita puntoVendita = em.find(PuntoVendita.class, UUID.fromString(id));
        if (puntoVendita == null) throw new NotFoundException(id);
        return puntoVendita;
    }

    public void emettiBiglietto(PuntoVendita puntoVendita) throws Exception {
        if (puntoVendita instanceof Rivenditore rivenditore) {
            if (!rivenditore.isLicenza()) {
                throw new Exception("Il rivenditore non è in possesso di licenza");
            }
        } else if (puntoVendita instanceof DistributoreAutomatico distributoreAutomatico) {
            if (distributoreAutomatico.getStato() != StatusDistributore.ATTIVO) {
                throw new Exception("Il distributore è fuori servizio");
            }
        } else {
            throw new Exception("Punto vendita non valido");
        }

        Biglietto bigliettoEmesso = new Biglietto(LocalDate.now(), puntoVendita);
        titoloDiViaggioDAO.aggiungiTitoloDiViaggio(bigliettoEmesso);
        System.out.println("Biglietto emesso con successo");
    }

    public void emettiAbbonamento(PuntoVendita puntoVendita, Utente utente, TipoAbbonamento tipoAbbonamento) throws Exception {
        Tessera tessera = utente.getNumeroTessera();
        if (tessera == null) {
            throw new Exception("L'utente non possiede una tessera per l'abbonamento");
        } else if (tessera.getDataScadenza().isBefore(LocalDate.now())) {
            throw new Exception("La tessera dell'utente è scaduta");
        }

        if (puntoVendita instanceof Rivenditore rivenditore) {
            if (!rivenditore.isLicenza()) {
                throw new Exception("Il rivenditore non è in possesso di licenza");
            }
        } else if (puntoVendita instanceof DistributoreAutomatico distributoreAutomatico) {
            if (distributoreAutomatico.getStato() != StatusDistributore.ATTIVO) {
                throw new Exception("Il distributore è fuori servizio");
            }
        } else {
            throw new Exception("Punto vendita non valido");
        }

        Abbonamento abbonamentoEmesso = new Abbonamento(LocalDate.now(), puntoVendita, tipoAbbonamento, tessera);
        titoloDiViaggioDAO.aggiungiTitoloDiViaggio(abbonamentoEmesso);
        System.out.println("Abbbonamento emesso con successo");
    }

    public long conteggioTitoliDiViaggioEmessiPerPuntoVendita(PuntoVendita puntoVendita) {
        return em.createQuery("SELECT COUNT(t) FROM TitoloDiViaggio t WHERE t.puntoVendita = :puntoVendita", Long.class).setParameter("puntoVendita", puntoVendita).getSingleResult();
    }

    public List<PuntoVendita> findAll() {
        return em.createQuery("SELECT p FROM PuntoVendita p", PuntoVendita.class).getResultList();
    }

  /*  public long conteggioTitoliDiViaggioEmessiPerPuntoVendita(String id) {
        PuntoVendita puntoVendita = findById(id);
        return em.createQuery("SELECT COUNT(t) FROM TitoloDiViaggio t WHERE t.puntoVendita = :puntoVendita", Long.class).setParameter("puntoVendita", puntoVendita).getSingleResult();
    }*/

    public long conteggioTitoliDiViaggioEmessiPerData(LocalDate dataInizio, LocalDate dataFine) {
        return em.createQuery("SELECT COUNT(tv) FROM TitoloDiViaggio tv WHERE tv.dataEmissione BETWEEN :dataInizio AND :dataFine", Long.class).setParameter("dataInizio", dataInizio).setParameter("dataFine", dataFine).getSingleResult();
    }

    public long conteggioTitoliDiViaggioEmessiPerDataPerPuntoVendita(LocalDate dataInizio, LocalDate dataFine, PuntoVendita puntoVendita) {
        return em.createQuery("SELECT COUNT(tv) FROM TitoloDiViaggio tv WHERE tv.dataEmissione BETWEEN :dataInizio AND :dataFine AND tv.puntoVendita = :puntoVendita", Long.class).setParameter("dataInizio", dataInizio).setParameter("dataFine", dataFine).setParameter("puntoVendita", puntoVendita).getSingleResult();
    }

    public List<DistributoreAutomatico> listaDistributoriAutomaticiAttivi() {
        return em.createQuery("SELECT d FROM DistributoreAutomatico d WHERE d.stato = :stato", DistributoreAutomatico.class)
                .setParameter("stato", StatusDistributore.ATTIVO)
                .getResultList();
    }

    public List<DistributoreAutomatico> listaDistributoriAutomaticiNonAttivi() {
        return em.createQuery("SELECT d FROM DistributoreAutomatico d WHERE d.stato = :stato", DistributoreAutomatico.class)
                .setParameter("stato", StatusDistributore.FUORI_SERVIZIO)
                .getResultList();
    }

    public List<Rivenditore> listaRivenditoriConLicenza() {
        return em.createQuery("SELECT r FROM Rivenditore r WHERE r.licenza = true", Rivenditore.class).getResultList();
    }

    public List<Rivenditore> listaRivenditoriSenzaLicenza() {
        return em.createQuery("SELECT r FROM Rivenditore r WHERE r.licenza = false", Rivenditore.class).getResultList();
    }
}
