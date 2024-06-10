package francescocristiano.entities.utenti;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

import java.util.UUID;

@Entity
public class Utente {
    @Id
    @GeneratedValue
    private UUID id;
    private String nome;
    private String cognome;
    @OneToOne(mappedBy = "utente")
    private Tessera numeroTessera;
}
