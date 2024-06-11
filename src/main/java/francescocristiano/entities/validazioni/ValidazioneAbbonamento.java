package francescocristiano.entities.validazioni;

import francescocristiano.entities.mezzi.Mezzo;
import francescocristiano.entities.titoliDiViaggio.Abbonamento;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDate;

@Entity
public class ValidazioneAbbonamento extends Validazione {
    @ManyToOne
    @JoinColumn(name = "abbonamento_id")
    private Abbonamento abbonamento;

    public ValidazioneAbbonamento(){}

    public ValidazioneAbbonamento(Mezzo mezzo, LocalDate dataValidazione, Abbonamento abbonamento) {
        super(mezzo, dataValidazione);
        this.abbonamento = abbonamento;
    }

    public Abbonamento getAbbonamento() {
        return abbonamento;
    }

    public void setAbbonamento(Abbonamento abbonamento) {
        this.abbonamento = abbonamento;
    }
}
