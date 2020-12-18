package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Match;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Match}.
 */
public interface MatchService {

    /**
     * Save a match.
     *
     * @param match the entity to save.
     * @return the persisted entity.
     */
    Match save(Match match);

    /**
     * Get all the matches.
     *
     * @return the list of entities.
     */
    List<Match> findAll();


    /**
     * Get the "id" match.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Match> findOne(Long id);

    /**
     * Delete the "id" match.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
