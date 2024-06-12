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

    public Corsa(){}

    public Corsa(LocalTime inizioCorsa, LocalTime fineCorsa, Tratta tratta, Mezzo mezzo) {
        this.inizioCorsa = inizioCorsa;
        this.fineCorsa = fineCorsa;
        this.tratta = tratta;
        this.mezzo = mezzo;
    }

    public UUID getId() {
        return id;
    }

    public LocalTime getInizioCorsa() {
        return inizioCorsa;
    }

    public void setInizioCorsa(LocalTime inizioCorsa) {
        this.inizioCorsa = inizioCorsa;
    }

    public LocalTime getFineCorsa() {
        return fineCorsa;
    }

    public void setFineCorsa(LocalTime fineCorsa) {
        this.fineCorsa = fineCorsa;
    }

    public Tratta getTratta() {
        return tratta;
    }

    public void setTratta(Tratta tratta) {
        this.tratta = tratta;
    }

    public Mezzo getMezzo() {
        return mezzo;
    }

    public void setMezzo(Mezzo mezzo) {
        this.mezzo = mezzo;
    }
}
