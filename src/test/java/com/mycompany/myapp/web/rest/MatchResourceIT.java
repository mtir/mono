package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.MywebApp;
import com.mycompany.myapp.domain.Match;
import com.mycompany.myapp.repository.MatchRepository;
import com.mycompany.myapp.service.MatchService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link MatchResource} REST controller.
 */
@SpringBootTest(classes = MywebApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class MatchResourceIT {

    private static final String DEFAULT_NOM_M = "AAAAAAAAAA";
    private static final String UPDATED_NOM_M = "BBBBBBBBBB";

    private static final Integer DEFAULT_NUM_M = 1;
    private static final Integer UPDATED_NUM_M = 2;

    private static final Duration DEFAULT_DUREE_M = Duration.ofHours(6);
    private static final Duration UPDATED_DUREE_M = Duration.ofHours(12);

    private static final LocalDate DEFAULT_DATE_M = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_M = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private MatchService matchService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMatchMockMvc;

    private Match match;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Match createEntity(EntityManager em) {
        Match match = new Match()
            .nomM(DEFAULT_NOM_M)
            .numM(DEFAULT_NUM_M)
            .dureeM(DEFAULT_DUREE_M)
            .dateM(DEFAULT_DATE_M);
        return match;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Match createUpdatedEntity(EntityManager em) {
        Match match = new Match()
            .nomM(UPDATED_NOM_M)
            .numM(UPDATED_NUM_M)
            .dureeM(UPDATED_DUREE_M)
            .dateM(UPDATED_DATE_M);
        return match;
    }

    @BeforeEach
    public void initTest() {
        match = createEntity(em);
    }

    @Test
    @Transactional
    public void createMatch() throws Exception {
        int databaseSizeBeforeCreate = matchRepository.findAll().size();
        // Create the Match
        restMatchMockMvc.perform(post("/api/matches")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(match)))
            .andExpect(status().isCreated());

        // Validate the Match in the database
        List<Match> matchList = matchRepository.findAll();
        assertThat(matchList).hasSize(databaseSizeBeforeCreate + 1);
        Match testMatch = matchList.get(matchList.size() - 1);
        assertThat(testMatch.getNomM()).isEqualTo(DEFAULT_NOM_M);
        assertThat(testMatch.getNumM()).isEqualTo(DEFAULT_NUM_M);
        assertThat(testMatch.getDureeM()).isEqualTo(DEFAULT_DUREE_M);
        assertThat(testMatch.getDateM()).isEqualTo(DEFAULT_DATE_M);
    }

    @Test
    @Transactional
    public void createMatchWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = matchRepository.findAll().size();

        // Create the Match with an existing ID
        match.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMatchMockMvc.perform(post("/api/matches")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(match)))
            .andExpect(status().isBadRequest());

        // Validate the Match in the database
        List<Match> matchList = matchRepository.findAll();
        assertThat(matchList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllMatches() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList
        restMatchMockMvc.perform(get("/api/matches?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(match.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomM").value(hasItem(DEFAULT_NOM_M)))
            .andExpect(jsonPath("$.[*].numM").value(hasItem(DEFAULT_NUM_M)))
            .andExpect(jsonPath("$.[*].dureeM").value(hasItem(DEFAULT_DUREE_M.toString())))
            .andExpect(jsonPath("$.[*].dateM").value(hasItem(DEFAULT_DATE_M.toString())));
    }
    
    @Test
    @Transactional
    public void getMatch() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get the match
        restMatchMockMvc.perform(get("/api/matches/{id}", match.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(match.getId().intValue()))
            .andExpect(jsonPath("$.nomM").value(DEFAULT_NOM_M))
            .andExpect(jsonPath("$.numM").value(DEFAULT_NUM_M))
            .andExpect(jsonPath("$.dureeM").value(DEFAULT_DUREE_M.toString()))
            .andExpect(jsonPath("$.dateM").value(DEFAULT_DATE_M.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingMatch() throws Exception {
        // Get the match
        restMatchMockMvc.perform(get("/api/matches/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMatch() throws Exception {
        // Initialize the database
        matchService.save(match);

        int databaseSizeBeforeUpdate = matchRepository.findAll().size();

        // Update the match
        Match updatedMatch = matchRepository.findById(match.getId()).get();
        // Disconnect from session so that the updates on updatedMatch are not directly saved in db
        em.detach(updatedMatch);
        updatedMatch
            .nomM(UPDATED_NOM_M)
            .numM(UPDATED_NUM_M)
            .dureeM(UPDATED_DUREE_M)
            .dateM(UPDATED_DATE_M);

        restMatchMockMvc.perform(put("/api/matches")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedMatch)))
            .andExpect(status().isOk());

        // Validate the Match in the database
        List<Match> matchList = matchRepository.findAll();
        assertThat(matchList).hasSize(databaseSizeBeforeUpdate);
        Match testMatch = matchList.get(matchList.size() - 1);
        assertThat(testMatch.getNomM()).isEqualTo(UPDATED_NOM_M);
        assertThat(testMatch.getNumM()).isEqualTo(UPDATED_NUM_M);
        assertThat(testMatch.getDureeM()).isEqualTo(UPDATED_DUREE_M);
        assertThat(testMatch.getDateM()).isEqualTo(UPDATED_DATE_M);
    }

    @Test
    @Transactional
    public void updateNonExistingMatch() throws Exception {
        int databaseSizeBeforeUpdate = matchRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMatchMockMvc.perform(put("/api/matches")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(match)))
            .andExpect(status().isBadRequest());

        // Validate the Match in the database
        List<Match> matchList = matchRepository.findAll();
        assertThat(matchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMatch() throws Exception {
        // Initialize the database
        matchService.save(match);

        int databaseSizeBeforeDelete = matchRepository.findAll().size();

        // Delete the match
        restMatchMockMvc.perform(delete("/api/matches/{id}", match.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Match> matchList = matchRepository.findAll();
        assertThat(matchList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
