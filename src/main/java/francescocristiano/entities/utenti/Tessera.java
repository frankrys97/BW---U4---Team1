package francescocristiano.entities.utenti;

import francescocristiano.entities.titoliDiViaggio.Abbonamento;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
public class Tessera {
    @Id
    @GeneratedValue
    private UUID id;
    @Column(name = "data_scadenza")
    private LocalDate dataScadenza;
    @OneToOne
    @JoinColumn(name = "utente_id")
    private Utente utente;
    @OneToMany(mappedBy = "tessera")
    private List<Abbonamento> abbonamenti;
}
