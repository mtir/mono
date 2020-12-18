package com.mycompany.myapp.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

/**
 * A Match.
 */
@Entity
@Table(name = "jhi_match")
public class Match implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom_m")
    private String nomM;

    @Column(name = "num_m")
    private Integer numM;

    @Column(name = "duree_m")
    private Duration dureeM;

    @Column(name = "date_m")
    private LocalDate dateM;

    @OneToMany(mappedBy = "match")
    private Set<Joueur> joueurs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomM() {
        return nomM;
    }

    public Match nomM(String nomM) {
        this.nomM = nomM;
        return this;
    }

    public void setNomM(String nomM) {
        this.nomM = nomM;
    }

    public Integer getNumM() {
        return numM;
    }

    public Match numM(Integer numM) {
        this.numM = numM;
        return this;
    }

    public void setNumM(Integer numM) {
        this.numM = numM;
    }

    public Duration getDureeM() {
        return dureeM;
    }

    public Match dureeM(Duration dureeM) {
        this.dureeM = dureeM;
        return this;
    }

    public void setDureeM(Duration dureeM) {
        this.dureeM = dureeM;
    }

    public LocalDate getDateM() {
        return dateM;
    }

    public Match dateM(LocalDate dateM) {
        this.dateM = dateM;
        return this;
    }

    public void setDateM(LocalDate dateM) {
        this.dateM = dateM;
    }

    public Set<Joueur> getJoueurs() {
        return joueurs;
    }

    public Match joueurs(Set<Joueur> joueurs) {
        this.joueurs = joueurs;
        return this;
    }

    public Match addJoueur(Joueur joueur) {
        this.joueurs.add(joueur);
        joueur.setMatch(this);
        return this;
    }

    public Match removeJoueur(Joueur joueur) {
        this.joueurs.remove(joueur);
        joueur.setMatch(null);
        return this;
    }

    public void setJoueurs(Set<Joueur> joueurs) {
        this.joueurs = joueurs;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Match)) {
            return false;
        }
        return id != null && id.equals(((Match) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Match{" +
            "id=" + getId() +
            ", nomM='" + getNomM() + "'" +
            ", numM=" + getNumM() +
            ", dureeM='" + getDureeM() + "'" +
            ", dateM='" + getDateM() + "'" +
            "}";
    }
}
