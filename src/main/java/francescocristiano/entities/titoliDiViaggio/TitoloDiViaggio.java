package francescocristiano.entities.titoliDiViaggio;

import francescocristiano.entities.puntiVendita.PuntoVendita;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class TitoloDiViaggio {
    @Id
    @GeneratedValue
    private UUID id;
    @Column(name = "data_emissione")
    private LocalDate dataEmissione;
    @ManyToOne
    @JoinColumn(name = "punto_vendita_id")
    private PuntoVendita puntoVendita;
}
