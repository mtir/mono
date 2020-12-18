package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.MywebApp;
import com.mycompany.myapp.domain.Stade;
import com.mycompany.myapp.repository.StadeRepository;
import com.mycompany.myapp.service.StadeService;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link StadeResource} REST controller.
 */
@SpringBootTest(classes = MywebApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class StadeResourceIT {

    private static final String DEFAULT_NOM_S = "AAAAAAAAAA";
    private static final String UPDATED_NOM_S = "BBBBBBBBBB";

    private static final String DEFAULT_LIEU = "AAAAAAAAAA";
    private static final String UPDATED_LIEU = "BBBBBBBBBB";

    @Autowired
    private StadeRepository stadeRepository;

    @Autowired
    private StadeService stadeService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStadeMockMvc;

    private Stade stade;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Stade createEntity(EntityManager em) {
        Stade stade = new Stade()
            .nomS(DEFAULT_NOM_S)
            .lieu(DEFAULT_LIEU);
        return stade;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Stade createUpdatedEntity(EntityManager em) {
        Stade stade = new Stade()
            .nomS(UPDATED_NOM_S)
            .lieu(UPDATED_LIEU);
        return stade;
    }

    @BeforeEach
    public void initTest() {
        stade = createEntity(em);
    }

    @Test
    @Transactional
    public void createStade() throws Exception {
        int databaseSizeBeforeCreate = stadeRepository.findAll().size();
        // Create the Stade
        restStadeMockMvc.perform(post("/api/stades")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(stade)))
            .andExpect(status().isCreated());

        // Validate the Stade in the database
        List<Stade> stadeList = stadeRepository.findAll();
        assertThat(stadeList).hasSize(databaseSizeBeforeCreate + 1);
        Stade testStade = stadeList.get(stadeList.size() - 1);
        assertThat(testStade.getNomS()).isEqualTo(DEFAULT_NOM_S);
        assertThat(testStade.getLieu()).isEqualTo(DEFAULT_LIEU);
    }

    @Test
    @Transactional
    public void createStadeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = stadeRepository.findAll().size();

        // Create the Stade with an existing ID
        stade.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStadeMockMvc.perform(post("/api/stades")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(stade)))
            .andExpect(status().isBadRequest());

        // Validate the Stade in the database
        List<Stade> stadeList = stadeRepository.findAll();
        assertThat(stadeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllStades() throws Exception {
        // Initialize the database
        stadeRepository.saveAndFlush(stade);

        // Get all the stadeList
        restStadeMockMvc.perform(get("/api/stades?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stade.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomS").value(hasItem(DEFAULT_NOM_S)))
            .andExpect(jsonPath("$.[*].lieu").value(hasItem(DEFAULT_LIEU)));
    }
    
    @Test
    @Transactional
    public void getStade() throws Exception {
        // Initialize the database
        stadeRepository.saveAndFlush(stade);

        // Get the stade
        restStadeMockMvc.perform(get("/api/stades/{id}", stade.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(stade.getId().intValue()))
            .andExpect(jsonPath("$.nomS").value(DEFAULT_NOM_S))
            .andExpect(jsonPath("$.lieu").value(DEFAULT_LIEU));
    }
    @Test
    @Transactional
    public void getNonExistingStade() throws Exception {
        // Get the stade
        restStadeMockMvc.perform(get("/api/stades/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStade() throws Exception {
        // Initialize the database
        stadeService.save(stade);

        int databaseSizeBeforeUpdate = stadeRepository.findAll().size();

        // Update the stade
        Stade updatedStade = stadeRepository.findById(stade.getId()).get();
        // Disconnect from session so that the updates on updatedStade are not directly saved in db
        em.detach(updatedStade);
        updatedStade
            .nomS(UPDATED_NOM_S)
            .lieu(UPDATED_LIEU);

        restStadeMockMvc.perform(put("/api/stades")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedStade)))
            .andExpect(status().isOk());

        // Validate the Stade in the database
        List<Stade> stadeList = stadeRepository.findAll();
        assertThat(stadeList).hasSize(databaseSizeBeforeUpdate);
        Stade testStade = stadeList.get(stadeList.size() - 1);
        assertThat(testStade.getNomS()).isEqualTo(UPDATED_NOM_S);
        assertThat(testStade.getLieu()).isEqualTo(UPDATED_LIEU);
    }

    @Test
    @Transactional
    public void updateNonExistingStade() throws Exception {
        int databaseSizeBeforeUpdate = stadeRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStadeMockMvc.perform(put("/api/stades")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(stade)))
            .andExpect(status().isBadRequest());

        // Validate the Stade in the database
        List<Stade> stadeList = stadeRepository.findAll();
        assertThat(stadeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteStade() throws Exception {
        // Initialize the database
        stadeService.save(stade);

        int databaseSizeBeforeDelete = stadeRepository.findAll().size();

        // Delete the stade
        restStadeMockMvc.perform(delete("/api/stades/{id}", stade.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Stade> stadeList = stadeRepository.findAll();
        assertThat(stadeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
