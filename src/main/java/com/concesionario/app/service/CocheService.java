package com.concesionario.app.service;

import com.concesionario.app.domain.Coche;
import com.concesionario.app.repository.CocheRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Coche}.
 */
@Service
@Transactional
public class CocheService {

    private final Logger log = LoggerFactory.getLogger(CocheService.class);

    private final CocheRepository cocheRepository;

    public CocheService(CocheRepository cocheRepository) {
        this.cocheRepository = cocheRepository;
    }

    /**
     * Save a coche.
     *
     * @param coche the entity to save.
     * @return the persisted entity.
     */
    public Coche save(Coche coche) {
        log.debug("Request to save Coche : {}", coche);
        return cocheRepository.save(coche);
    }

    /**
     * Update a coche.
     *
     * @param coche the entity to save.
     * @return the persisted entity.
     */
    public Coche update(Coche coche) {
        log.debug("Request to save Coche : {}", coche);
        return cocheRepository.save(coche);
    }

    /**
     * Partially update a coche.
     *
     * @param coche the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Coche> partialUpdate(Coche coche) {
        log.debug("Request to partially update Coche : {}", coche);

        return cocheRepository
            .findById(coche.getId())
            .map(existingCoche -> {
                if (coche.getMarca() != null) {
                    existingCoche.setMarca(coche.getMarca());
                }
                if (coche.getModelo() != null) {
                    existingCoche.setModelo(coche.getModelo());
                }
                if (coche.getKilometraje() != null) {
                    existingCoche.setKilometraje(coche.getKilometraje());
                }
                if (coche.getColor() != null) {
                    existingCoche.setColor(coche.getColor());
                }
                if (coche.getTipoCoche() != null) {
                    existingCoche.setTipoCoche(coche.getTipoCoche());
                }
                if (coche.getEstado() != null) {
                    existingCoche.setEstado(coche.getEstado());
                }
                if (coche.getPrecio() != null) {
                    existingCoche.setPrecio(coche.getPrecio());
                }

                return existingCoche;
            })
            .map(cocheRepository::save);
    }

    /**
     * Get all the coches.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Coche> findAll(Pageable pageable) {
        log.debug("Request to get all Coches");
        return cocheRepository.findAll(pageable);
    }

    /**
     * Get one coche by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Coche> findOne(Long id) {
        log.debug("Request to get Coche : {}", id);
        return cocheRepository.findById(id);
    }

    /**
     * Delete the coche by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Coche : {}", id);
        cocheRepository.deleteById(id);
    }
}
