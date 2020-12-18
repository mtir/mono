package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Stade;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Stade}.
 */
public interface StadeService {

    /**
     * Save a stade.
     *
     * @param stade the entity to save.
     * @return the persisted entity.
     */
    Stade save(Stade stade);

    /**
     * Get all the stades.
     *
     * @return the list of entities.
     */
    List<Stade> findAll();


    /**
     * Get the "id" stade.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Stade> findOne(Long id);

    /**
     * Delete the "id" stade.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
