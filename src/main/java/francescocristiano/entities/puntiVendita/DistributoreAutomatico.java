package francescocristiano.entities.puntiVendita;

import francescocristiano.entities.titoliDiViaggio.TitoloDiViaggio;
import francescocristiano.enums.StatusDistributore;
import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
public class DistributoreAutomatico extends PuntoVendita {
    @Enumerated(EnumType.STRING)
    private StatusDistributore stato;

    public DistributoreAutomatico(){}

    public DistributoreAutomatico(StatusDistributore stato) {
        this.stato = stato;
    }

    public StatusDistributore getStato() {
        return stato;
    }

    public void setStato(StatusDistributore stato) {
        this.stato = stato;
    }
}
