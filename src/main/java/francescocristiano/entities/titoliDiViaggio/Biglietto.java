package francescocristiano.entities.titoliDiViaggio;

import francescocristiano.entities.validazioni.ValidazioneBiglietto;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;

@Entity
public class Biglietto extends TitoloDiViaggio {
    private boolean valido;
    @OneToOne(mappedBy = "biglietto")
    private ValidazioneBiglietto validazioneBiglietto;
}
