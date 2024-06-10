package francescocristiano.entities.titoliDiViaggio;

import francescocristiano.entities.utenti.Tessera;
import francescocristiano.entities.validazioni.ValidazioneAbbonamento;
import francescocristiano.enums.TipoAbbonamento;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Abbonamento extends TitoloDiViaggio {
    @Column(name = "data_scadenza")
    private LocalDate dataScadenza;
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_abbonamento")
    private TipoAbbonamento tipoAbbonamento;
    @ManyToOne
    @JoinColumn(name = "tessera_id")
    private Tessera tessera;
    @OneToMany(mappedBy = "abbonamento")
    private List<ValidazioneAbbonamento> validazioniAbbonamenti;
}
