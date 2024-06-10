package francescocristiano.entities.mezzi;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
public class Tratta {
    @Id
    @GeneratedValue
    private UUID id;
    @Column(name = "zona_partenza")
    private String zonaPartenza;
    private String capolinea;
    @Column(name = "tempo_medio_percorrenza")
    private int tempo_medio_percorrenza;
    @ManyToMany(mappedBy = "tratte")
    private List<Mezzo> mezzi;
    @OneToMany(mappedBy = "tratta")
    private List<Corsa> corse;
}
