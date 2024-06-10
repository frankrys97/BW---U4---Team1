package francescocristiano.entities.mezzi;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "mezzo_di_trasporto")
public abstract class Mezzo {
    @Id
    @GeneratedValue
    private UUID id;
    private int capienza;
    @Column(name = "in_servizio")
    private boolean inServizio;
    @ManyToMany
    @JoinTable(name = "mezzo_tratta", joinColumns = @JoinColumn(name = "mezzo_id"), inverseJoinColumns = @JoinColumn(name = "tratta_id"))
    private List<Tratta> tratte;
    @OneToMany(mappedBy = "mezzo")
    private List<Corsa> corse;
    @OneToMany(mappedBy = "mezzo")
    private List<PeriodoServizioManutenzione> periodiServiziManutenzione;
}
