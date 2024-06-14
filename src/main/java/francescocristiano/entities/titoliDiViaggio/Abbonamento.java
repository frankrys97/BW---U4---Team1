package francescocristiano.entities.titoliDiViaggio;

import francescocristiano.entities.puntiVendita.PuntoVendita;
import francescocristiano.entities.utenti.Tessera;
import francescocristiano.entities.validazioni.ValidazioneAbbonamento;
import francescocristiano.enums.TipoAbbonamento;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Abbonamento extends TitoloDiViaggio {
    @Column(name = "data_scadenza")
    private LocalDate dataScadenza;
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_abbonamento")
    private TipoAbbonamento tipoAbbonamento;
    @ManyToOne
    @JoinColumn(name = "tessera_id")
    private Tessera tessera;
    @OneToMany(mappedBy = "abbonamento")
    private List<ValidazioneAbbonamento> validazioniAbbonamenti;

    public Abbonamento() {
    }

    public Abbonamento(LocalDate dataEmissione, PuntoVendita puntoVendita, TipoAbbonamento tipoAbbonamento, Tessera tessera) {
        super(dataEmissione, puntoVendita);
        this.tipoAbbonamento = tipoAbbonamento;
        this.dataScadenza = dataScadenzaAbbonamento();
        this.tessera = tessera;
    }

    public LocalDate dataScadenzaAbbonamento() {
        if (this.tipoAbbonamento.equals(TipoAbbonamento.SETTIMANALE)) {
            return this.getDataEmissione().plusDays(7);
        } else {
            return this.getDataEmissione().plusMonths(1);
        }
    }

    public LocalDate getDataScadenza() {
        return dataScadenza;
    }

    public void setDataScadenza(LocalDate dataScadenza) {
        this.dataScadenza = dataScadenza;
    }

    public TipoAbbonamento getTipoAbbonamento() {
        return tipoAbbonamento;
    }

    public void setTipoAbbonamento(TipoAbbonamento tipoAbbonamento) {
        this.tipoAbbonamento = tipoAbbonamento;
    }

    public Tessera getTessera() {
        return tessera;
    }

    public void setTessera(Tessera tessera) {
        this.tessera = tessera;
    }

    public List<ValidazioneAbbonamento> getValidazioniAbbonamenti() {
        return validazioniAbbonamenti;
    }

    public void setValidazioniAbbonamenti(List<ValidazioneAbbonamento> validazioniAbbonamenti) {
        this.validazioniAbbonamenti = validazioniAbbonamenti;
    }
}
