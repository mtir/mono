package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A Joueur.
 */
@Entity
@Table(name = "joueur")
public class Joueur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom_j")
    private String nomJ;

    @Column(name = "prenom_j")
    private String prenomJ;

    @Column(name = "num_j")
    private Integer numJ;

    @ManyToOne
    @JsonIgnoreProperties(value = "joueurs", allowSetters = true)
    private Match match;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomJ() {
        return nomJ;
    }

    public Joueur nomJ(String nomJ) {
        this.nomJ = nomJ;
        return this;
    }

    public void setNomJ(String nomJ) {
        this.nomJ = nomJ;
    }

    public String getPrenomJ() {
        return prenomJ;
    }

    public Joueur prenomJ(String prenomJ) {
        this.prenomJ = prenomJ;
        return this;
    }

    public void setPrenomJ(String prenomJ) {
        this.prenomJ = prenomJ;
    }

    public Integer getNumJ() {
        return numJ;
    }

    public Joueur numJ(Integer numJ) {
        this.numJ = numJ;
        return this;
    }

    public void setNumJ(Integer numJ) {
        this.numJ = numJ;
    }

    public Match getMatch() {
        return match;
    }

    public Joueur match(Match match) {
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
        if (!(o instanceof Joueur)) {
            return false;
        }
        return id != null && id.equals(((Joueur) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Joueur{" +
            "id=" + getId() +
            ", nomJ='" + getNomJ() + "'" +
            ", prenomJ='" + getPrenomJ() + "'" +
            ", numJ=" + getNumJ() +
            "}";
    }
}
