package francescocristiano.entities.puntiVendita;

import francescocristiano.enums.StatusDistributore;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Entity
public class DistributoreAutomatico extends PuntoVendita {
    @Enumerated(EnumType.STRING)
    private StatusDistributore stato;

    public DistributoreAutomatico() {
    }

    public DistributoreAutomatico(StatusDistributore stato) {
        this.stato = stato;
    }

    public StatusDistributore getStato() {
        return stato;
    }

    public void setStato(StatusDistributore stato) {
        this.stato = stato;
    }

    @Override
    public String toString() {
        return "DistributoreAutomatico{" +
                "stato=" + stato +
                '}';
    }
}
