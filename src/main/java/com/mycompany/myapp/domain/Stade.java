package com.mycompany.myapp.domain;


import javax.persistence.*;

import java.io.Serializable;

/**
 * A Stade.
 */
@Entity
@Table(name = "stade")
public class Stade implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom_s")
    private String nomS;

    @Column(name = "lieu")
    private String lieu;

    @OneToOne
    @JoinColumn(unique = true)
    private Match match;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomS() {
        return nomS;
    }

    public Stade nomS(String nomS) {
        this.nomS = nomS;
        return this;
    }

    public void setNomS(String nomS) {
        this.nomS = nomS;
    }

    public String getLieu() {
        return lieu;
    }

    public Stade lieu(String lieu) {
        this.lieu = lieu;
        return this;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public Match getMatch() {
        return match;
    }

    public Stade match(Match match) {
        this.match = match;
        return this;
    }

    public void setMatch(Match match) {
        this.match = match;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Stade)) {
            return false;
        }
        return id != null && id.equals(((Stade) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Stade{" +
            "id=" + getId() +
            ", nomS='" + getNomS() + "'" +
            ", lieu='" + getLieu() + "'" +
            "}";
    }
}
