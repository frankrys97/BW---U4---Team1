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

    public PeriodoServizioManutenzione(){}

    public PeriodoServizioManutenzione(LocalDate dataInizio, LocalDate dataFine, AttivitaMezzo attivitaMezzo, String descrizione, Mezzo mezzo) {
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.attivitaMezzo = attivitaMezzo;
        this.descrizione = descrizione;
        this.mezzo = mezzo;
    }

    public UUID getId() {
        return id;
    }

    public LocalDate getDataInizio() {
        return dataInizio;
    }

    public void setDataInizio(LocalDate dataInizio) {
        this.dataInizio = dataInizio;
    }

    public LocalDate getDataFine() {
        return dataFine;
    }

    public void setDataFine(LocalDate dataFine) {
        this.dataFine = dataFine;
    }

    public AttivitaMezzo getAttivitaMezzo() {
        return attivitaMezzo;
    }

    public void setAttivitaMezzo(AttivitaMezzo attivitaMezzo) {
        this.attivitaMezzo = attivitaMezzo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Mezzo getMezzo() {
        return mezzo;
    }

    public void setMezzo(Mezzo mezzo) {
        this.mezzo = mezzo;
    }
}
