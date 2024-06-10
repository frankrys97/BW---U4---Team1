package francescocristiano.entities.puntiVendita;

import jakarta.persistence.Entity;

@Entity
public class RivenditoreAutorizzato extends PuntoVendita {
    private String nome;
    private boolean licenza;
}
