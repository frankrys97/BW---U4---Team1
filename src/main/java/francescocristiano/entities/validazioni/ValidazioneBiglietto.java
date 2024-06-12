package francescocristiano.entities.validazioni;

import francescocristiano.entities.mezzi.Mezzo;
import francescocristiano.entities.titoliDiViaggio.Biglietto;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

import java.time.LocalDate;

@Entity
public class ValidazioneBiglietto extends Validazione {
    @OneToOne
    @JoinColumn(name = "biglietto_id")
    private Biglietto biglietto;

    public ValidazioneBiglietto(){}

    public ValidazioneBiglietto(Mezzo mezzo, LocalDate dataValidazione, Biglietto biglietto) {
        super(mezzo, dataValidazione);
        this.biglietto = biglietto;
    }

    public Biglietto getBiglietto() {
        return biglietto;
    }

    public void setBiglietto(Biglietto biglietto) {
        this.biglietto = biglietto;
    }
}
