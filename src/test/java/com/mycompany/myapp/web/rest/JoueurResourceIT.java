package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.MywebApp;
import com.mycompany.myapp.domain.Joueur;
import com.mycompany.myapp.repository.JoueurRepository;
import com.mycompany.myapp.service.JoueurService;

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
 * Integration tests for the {@link JoueurResource} REST controller.
 */
@SpringBootTest(classes = MywebApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class JoueurResourceIT {

    private static final String DEFAULT_NOM_J = "AAAAAAAAAA";
    private static final String UPDATED_NOM_J = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM_J = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM_J = "BBBBBBBBBB";

    private static final Integer DEFAULT_NUM_J = 1;
    private static final Integer UPDATED_NUM_J = 2;

    @Autowired
    private JoueurRepository joueurRepository;

    @Autowired
    private JoueurService joueurService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restJoueurMockMvc;

    private Joueur joueur;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Joueur createEntity(EntityManager em) {
        Joueur joueur = new Joueur()
            .nomJ(DEFAULT_NOM_J)
            .prenomJ(DEFAULT_PRENOM_J)
            .numJ(DEFAULT_NUM_J);
        return joueur;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Joueur createUpdatedEntity(EntityManager em) {
        Joueur joueur = new Joueur()
            .nomJ(UPDATED_NOM_J)
            .prenomJ(UPDATED_PRENOM_J)
            .numJ(UPDATED_NUM_J);
        return joueur;
    }

    @BeforeEach
    public void initTest() {
        joueur = createEntity(em);
    }

    @Test
    @Transactional
    public void createJoueur() throws Exception {
        int databaseSizeBeforeCreate = joueurRepository.findAll().size();
        // Create the Joueur
        restJoueurMockMvc.perform(post("/api/joueurs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(joueur)))
            .andExpect(status().isCreated());

        // Validate the Joueur in the database
        List<Joueur> joueurList = joueurRepository.findAll();
        assertThat(joueurList).hasSize(databaseSizeBeforeCreate + 1);
        Joueur testJoueur = joueurList.get(joueurList.size() - 1);
        assertThat(testJoueur.getNomJ()).isEqualTo(DEFAULT_NOM_J);
        assertThat(testJoueur.getPrenomJ()).isEqualTo(DEFAULT_PRENOM_J);
        assertThat(testJoueur.getNumJ()).isEqualTo(DEFAULT_NUM_J);
    }

    @Test
    @Transactional
    public void createJoueurWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = joueurRepository.findAll().size();

        // Create the Joueur with an existing ID
        joueur.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restJoueurMockMvc.perform(post("/api/joueurs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(joueur)))
            .andExpect(status().isBadRequest());

        // Validate the Joueur in the database
        List<Joueur> joueurList = joueurRepository.findAll();
        assertThat(joueurList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllJoueurs() throws Exception {
        // Initialize the database
        joueurRepository.saveAndFlush(joueur);

        // Get all the joueurList
        restJoueurMockMvc.perform(get("/api/joueurs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(joueur.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomJ").value(hasItem(DEFAULT_NOM_J)))
            .andExpect(jsonPath("$.[*].prenomJ").value(hasItem(DEFAULT_PRENOM_J)))
            .andExpect(jsonPath("$.[*].numJ").value(hasItem(DEFAULT_NUM_J)));
    }
    
    @Test
    @Transactional
    public void getJoueur() throws Exception {
        // Initialize the database
        joueurRepository.saveAndFlush(joueur);

        // Get the joueur
        restJoueurMockMvc.perform(get("/api/joueurs/{id}", joueur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(joueur.getId().intValue()))
            .andExpect(jsonPath("$.nomJ").value(DEFAULT_NOM_J))
            .andExpect(jsonPath("$.prenomJ").value(DEFAULT_PRENOM_J))
            .andExpect(jsonPath("$.numJ").value(DEFAULT_NUM_J));
    }
    @Test
    @Transactional
    public void getNonExistingJoueur() throws Exception {
        // Get the joueur
        restJoueurMockMvc.perform(get("/api/joueurs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJoueur() throws Exception {
        // Initialize the database
        joueurService.save(joueur);

        int databaseSizeBeforeUpdate = joueurRepository.findAll().size();

        // Update the joueur
        Joueur updatedJoueur = joueurRepository.findById(joueur.getId()).get();
        // Disconnect from session so that the updates on updatedJoueur are not directly saved in db
        em.detach(updatedJoueur);
        updatedJoueur
            .nomJ(UPDATED_NOM_J)
            .prenomJ(UPDATED_PRENOM_J)
            .numJ(UPDATED_NUM_J);

        restJoueurMockMvc.perform(put("/api/joueurs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedJoueur)))
            .andExpect(status().isOk());

        // Validate the Joueur in the database
        List<Joueur> joueurList = joueurRepository.findAll();
        assertThat(joueurList).hasSize(databaseSizeBeforeUpdate);
        Joueur testJoueur = joueurList.get(joueurList.size() - 1);
        assertThat(testJoueur.getNomJ()).isEqualTo(UPDATED_NOM_J);
        assertThat(testJoueur.getPrenomJ()).isEqualTo(UPDATED_PRENOM_J);
        assertThat(testJoueur.getNumJ()).isEqualTo(UPDATED_NUM_J);
    }

    @Test
    @Transactional
    public void updateNonExistingJoueur() throws Exception {
        int databaseSizeBeforeUpdate = joueurRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJoueurMockMvc.perform(put("/api/joueurs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(joueur)))
            .andExpect(status().isBadRequest());

        // Validate the Joueur in the database
        List<Joueur> joueurList = joueurRepository.findAll();
        assertThat(joueurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteJoueur() throws Exception {
        // Initialize the database
        joueurService.save(joueur);

        int databaseSizeBeforeDelete = joueurRepository.findAll().size();

        // Delete the joueur
        restJoueurMockMvc.perform(delete("/api/joueurs/{id}", joueur.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Joueur> joueurList = joueurRepository.findAll();
        assertThat(joueurList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
