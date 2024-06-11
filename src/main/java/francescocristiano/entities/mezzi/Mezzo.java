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

    public Mezzo(){}

    public Mezzo(int capienza, boolean inServizio) {
        this.capienza = capienza;
        this.inServizio = inServizio;
    }

    public UUID getId() {
        return id;
    }

    public int getCapienza() {
        return capienza;
    }

    public void setCapienza(int capienza) {
        this.capienza = capienza;
    }

    public boolean isInServizio() {
        return inServizio;
    }

    public void setInServizio(boolean inServizio) {
        this.inServizio = inServizio;
    }

    public List<Tratta> getTratte() {
        return tratte;
    }

    public void setTratte(List<Tratta> tratte) {
        this.tratte = tratte;
    }

    public List<Corsa> getCorse() {
        return corse;
    }

    public void setCorse(List<Corsa> corse) {
        this.corse = corse;
    }

    public List<PeriodoServizioManutenzione> getPeriodiServiziManutenzione() {
        return periodiServiziManutenzione;
    }

    public void setPeriodiServiziManutenzione(List<PeriodoServizioManutenzione> periodiServiziManutenzione) {
        this.periodiServiziManutenzione = periodiServiziManutenzione;
    }
}
