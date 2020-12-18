package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.StadeService;
import com.mycompany.myapp.domain.Stade;
import com.mycompany.myapp.repository.StadeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Stade}.
 */
@Service
@Transactional
public class StadeServiceImpl implements StadeService {

    private final Logger log = LoggerFactory.getLogger(StadeServiceImpl.class);

    private final StadeRepository stadeRepository;

    public StadeServiceImpl(StadeRepository stadeRepository) {
        this.stadeRepository = stadeRepository;
    }

    @Override
    public Stade save(Stade stade) {
        log.debug("Request to save Stade : {}", stade);
        return stadeRepository.save(stade);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Stade> findAll() {
        log.debug("Request to get all Stades");
        return stadeRepository.findAll();
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<Stade> findOne(Long id) {
        log.debug("Request to get Stade : {}", id);
        return stadeRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Stade : {}", id);
        stadeRepository.deleteById(id);
    }
}
