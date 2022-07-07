package com.concesionario.app.service;

import com.concesionario.app.domain.Cliente;
import com.concesionario.app.repository.ClienteRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Cliente}.
 */
@Service
@Transactional
public class ClienteService {

    private final Logger log = LoggerFactory.getLogger(ClienteService.class);

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    /**
     * Save a cliente.
     *
     * @param cliente the entity to save.
     * @return the persisted entity.
     */
    public Cliente save(Cliente cliente) {
        log.debug("Request to save Cliente : {}", cliente);
        return clienteRepository.save(cliente);
    }

    /**
     * Update a cliente.
     *
     * @param cliente the entity to save.
     * @return the persisted entity.
     */
    public Cliente update(Cliente cliente) {
        log.debug("Request to save Cliente : {}", cliente);
        return clienteRepository.save(cliente);
    }

    /**
     * Partially update a cliente.
     *
     * @param cliente the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Cliente> partialUpdate(Cliente cliente) {
        log.debug("Request to partially update Cliente : {}", cliente);

        return clienteRepository
            .findById(cliente.getId())
            .map(existingCliente -> {
                if (cliente.getNombre() != null) {
                    existingCliente.setNombre(cliente.getNombre());
                }
                if (cliente.getPrimerApellido() != null) {
                    existingCliente.setPrimerApellido(cliente.getPrimerApellido());
                }
                if (cliente.getSegundoApellido() != null) {
                    existingCliente.setSegundoApellido(cliente.getSegundoApellido());
                }
                if (cliente.getTipoIdentificacion() != null) {
                    existingCliente.setTipoIdentificacion(cliente.getTipoIdentificacion());
                }
                if (cliente.getNumIdentificacion() != null) {
                    existingCliente.setNumIdentificacion(cliente.getNumIdentificacion());
                }
                if (cliente.getTelefono() != null) {
                    existingCliente.setTelefono(cliente.getTelefono());
                }
                if (cliente.getMail() != null) {
                    existingCliente.setMail(cliente.getMail());
                }
                if (cliente.getDireccion() != null) {
                    existingCliente.setDireccion(cliente.getDireccion());
                }
                if (cliente.getOcupacion() != null) {
                    existingCliente.setOcupacion(cliente.getOcupacion());
                }

                return existingCliente;
            })
            .map(clienteRepository::save);
    }

    /**
     * Get all the clientes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Cliente> findAll(Pageable pageable) {
        log.debug("Request to get all Clientes");
        return clienteRepository.findAll(pageable);
    }

    /**
     * Get one cliente by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Cliente> findOne(Long id) {
        log.debug("Request to get Cliente : {}", id);
        return clienteRepository.findById(id);
    }

    /**
     * Delete the cliente by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Cliente : {}", id);
        clienteRepository.deleteById(id);
    }
}
