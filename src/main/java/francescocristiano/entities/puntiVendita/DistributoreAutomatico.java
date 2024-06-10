package francescocristiano.entities.puntiVendita;

import francescocristiano.enums.StatusDistributore;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Entity
public class DistributoreAutomatico extends PuntoVendita {
    @Enumerated(EnumType.STRING)
    private StatusDistributore stato;
}
