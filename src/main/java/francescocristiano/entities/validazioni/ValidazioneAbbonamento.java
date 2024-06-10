package francescocristiano.entities.validazioni;

import francescocristiano.entities.titoliDiViaggio.Abbonamento;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class ValidazioneAbbonamento extends Validazione {
    @ManyToOne
    @JoinColumn(name = "abbonamento_id")
    private Abbonamento abbonamento;
}
