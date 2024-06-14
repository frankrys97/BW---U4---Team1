package francescocristiano.entities.utenti;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class Utente {
    @Id
    @GeneratedValue
    private UUID id;
    private String nome;
    private String cognome;
   /* @OneToOne(mappedBy = "utente")
    private Tessera numeroTessera;*/

    @OneToOne
    @JoinColumn(name = "tessera_id")
    private Tessera numeroTessera;

    public Utente() {
    }

    public Utente(String nome, String cognome) {
        this.nome = nome;
        this.cognome = cognome;
    }

    public UUID getId() {
        return id;
    }


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public Tessera getNumeroTessera() {
        return numeroTessera;
    }

    public void setNumeroTessera(Tessera numeroTessera) {
        this.numeroTessera = numeroTessera;
    }

    @Override
    public String toString() {
        return "Utente: " +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", numeroTessera=" + numeroTessera;
    }
}
