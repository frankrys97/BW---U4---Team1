package francescocristiano.entities.puntiVendita;

import francescocristiano.entities.titoliDiViaggio.TitoloDiViaggio;
import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class PuntoVendita {
    @Id
    @GeneratedValue
    private UUID id;
    @OneToMany(mappedBy = "puntoVendita")
    private List<TitoloDiViaggio> titoliDiViaggio;

    public PuntoVendita(){}

    public UUID getId() {
        return id;
    }

    public List<TitoloDiViaggio> getTitoliDiViaggio() {
        return titoliDiViaggio;
    }

    public void setTitoliDiViaggio(List<TitoloDiViaggio> titoliDiViaggio) {
        this.titoliDiViaggio = titoliDiViaggio;
    }
}
