package francescocristiano.entities.mezzi;

import jakarta.persistence.*;

import java.time.LocalTime;
import java.util.UUID;

@Entity
public class Corsa {
    @Id
    @GeneratedValue
    private UUID id;
    @Column(name = "inizio_corsa")
    private LocalTime inizioCorsa;
    @Column(name = "fine_corsa")
    private LocalTime fineCorsa;
    @ManyToOne
    @JoinColumn(name = "tratta_id")
    private Tratta tratta;
    @ManyToOne
    @JoinColumn(name = "mezzo_id")
    private Mezzo mezzo;
}
