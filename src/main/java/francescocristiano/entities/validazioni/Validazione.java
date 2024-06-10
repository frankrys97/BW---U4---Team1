package francescocristiano.entities.validazioni;

import francescocristiano.entities.mezzi.Mezzo;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Validazione {
    @Id
    @GeneratedValue
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "mezzo_id")
    private Mezzo mezzo;
    @Column(name = "data_validazione")
    private LocalDate dataValidazione;
}
