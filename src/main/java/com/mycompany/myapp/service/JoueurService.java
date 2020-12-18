package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Joueur;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Joueur}.
 */
public interface JoueurService {

    /**
     * Save a joueur.
     *
     * @param joueur the entity to save.
     * @return the persisted entity.
     */
    Joueur save(Joueur joueur);

    /**
     * Get all the joueurs.
     *
     * @return the list of entities.
     */
    List<Joueur> findAll();


    /**
     * Get the "id" joueur.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Joueur> findOne(Long id);

    /**
     * Delete the "id" joueur.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
