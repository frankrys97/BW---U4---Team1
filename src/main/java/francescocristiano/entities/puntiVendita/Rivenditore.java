package francescocristiano.entities.puntiVendita;

import jakarta.persistence.Entity;

@Entity
public class Rivenditore extends PuntoVendita {
    private String nome;
    private boolean licenza;

    public Rivenditore(){}

    public Rivenditore(String nome, boolean licenza) {
        this.nome = nome;
        this.licenza = licenza;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean isLicenza() {
        return licenza;
    }

    public void setLicenza(boolean licenza) {
        this.licenza = licenza;
    }
}
