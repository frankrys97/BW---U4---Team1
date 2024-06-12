package francescocristiano.entities.titoliDiViaggio;

import francescocristiano.entities.puntiVendita.PuntoVendita;
import francescocristiano.entities.validazioni.ValidazioneBiglietto;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;

import java.time.LocalDate;

@Entity
public class Biglietto extends TitoloDiViaggio {
    private boolean valido;
    @OneToOne(mappedBy = "biglietto")
    private ValidazioneBiglietto validazioneBiglietto;

    public Biglietto(){}

    public Biglietto(LocalDate dataEmissione, PuntoVendita puntoVendita) {
        super(dataEmissione, puntoVendita);
        this.valido = true;
    }

    public boolean isValido() {
        return valido;
    }

    public void setValido(boolean valido) {
        this.valido = valido;
    }

    public ValidazioneBiglietto getValidazioneBiglietto() {
        return validazioneBiglietto;
    }

    public void setValidazioneBiglietto(ValidazioneBiglietto validazioneBiglietto) {
        this.validazioneBiglietto = validazioneBiglietto;
    }
}
