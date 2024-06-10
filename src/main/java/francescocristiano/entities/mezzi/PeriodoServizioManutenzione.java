package francescocristiano.entities.mezzi;

import francescocristiano.enums.AttivitaMezzo;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
public class PeriodoServizioManutenzione {
    @Id
    @GeneratedValue
    private UUID id;
    @Column(name = "data_inizio")
    private LocalDate dataInizio;
    @Column(name = "data_fine")
    private LocalDate dataFine;
    @Enumerated(EnumType.STRING)
    private AttivitaMezzo attivitaMezzo;
    private String descrizione;
    @ManyToOne
    @JoinColumn(name = "mezzo_id")
    private Mezzo mezzo;
}
