package francescocristiano.entities.puntiVendita;

import francescocristiano.entities.titoliDiViaggio.TitoloDiViaggio;
import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class PuntoVendita {
    @Id
    @GeneratedValue
    private UUID id;
    @OneToMany(mappedBy = "puntoVendita")
    private List<TitoloDiViaggio> titoliDiViaggio;
}
