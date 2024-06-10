package francescocristiano.entities.validazioni;

import francescocristiano.entities.titoliDiViaggio.Biglietto;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class ValidazioneBiglietto extends Validazione {
    @OneToOne
    @JoinColumn(name = "biglietto_id")
    private Biglietto biglietto;
}
