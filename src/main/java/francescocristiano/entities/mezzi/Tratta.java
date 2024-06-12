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

    public Tratta() {
    }

    public Tratta(String zonaPartenza, String capolinea) {
        this.zonaPartenza = zonaPartenza;
        this.capolinea = capolinea;
        this.tempo_medio_percorrenza = 0;
    }

    public UUID getId() {
        return id;
    }

    public String getZonaPartenza() {
        return zonaPartenza;
    }

    public void setZonaPartenza(String zonaPartenza) {
        this.zonaPartenza = zonaPartenza;
    }

    public String getCapolinea() {
        return capolinea;
    }

    public void setCapolinea(String capolinea) {
        this.capolinea = capolinea;
    }

    public int getTempo_medio_percorrenza() {
        return tempo_medio_percorrenza;
    }

    public void setTempo_medio_percorrenza(int tempo_medio_percorrenza) {
        this.tempo_medio_percorrenza = tempo_medio_percorrenza;
    }

    public List<Mezzo> getMezzi() {
        return mezzi;
    }

    public void setMezzi(List<Mezzo> mezzi) {
        this.mezzi = mezzi;
    }

    public List<Corsa> getCorse() {
        return corse;
    }

    public void setCorse(List<Corsa> corse) {
        this.corse = corse;
    }
}
