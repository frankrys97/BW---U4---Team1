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

    public Validazione(){}

    public Validazione(Mezzo mezzo, LocalDate dataValidazione) {
        this.mezzo = mezzo;
        this.dataValidazione = dataValidazione;
    }

    public UUID getId() {
        return id;
    }

    public Mezzo getMezzo() {
        return mezzo;
    }

    public void setMezzo(Mezzo mezzo) {
        this.mezzo = mezzo;
    }

    public LocalDate getDataValidazione() {
        return dataValidazione;
    }

    public void setDataValidazione(LocalDate dataValidazione) {
        this.dataValidazione = dataValidazione;
    }
}
