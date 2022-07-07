package com.concesionario.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.concesionario.app.IntegrationTest;
import com.concesionario.app.domain.DetalleVenta;
import com.concesionario.app.domain.Venta;
import com.concesionario.app.repository.DetalleVentaRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link DetalleVentaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DetalleVentaResourceIT {

    private static final String DEFAULT_DESCUENTO = "AAAAAAAAAA";
    private static final String UPDATED_DESCUENTO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/detalle-ventas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DetalleVentaRepository detalleVentaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDetalleVentaMockMvc;

    private DetalleVenta detalleVenta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DetalleVenta createEntity(EntityManager em) {
        DetalleVenta detalleVenta = new DetalleVenta().descuento(DEFAULT_DESCUENTO);
        // Add required entity
        Venta venta;
        if (TestUtil.findAll(em, Venta.class).isEmpty()) {
            venta = VentaResourceIT.createEntity(em);
            em.persist(venta);
            em.flush();
        } else {
            venta = TestUtil.findAll(em, Venta.class).get(0);
        }
        detalleVenta.setVenta(venta);
        return detalleVenta;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DetalleVenta createUpdatedEntity(EntityManager em) {
        DetalleVenta detalleVenta = new DetalleVenta().descuento(UPDATED_DESCUENTO);
        // Add required entity
        Venta venta;
        if (TestUtil.findAll(em, Venta.class).isEmpty()) {
            venta = VentaResourceIT.createUpdatedEntity(em);
            em.persist(venta);
            em.flush();
        } else {
            venta = TestUtil.findAll(em, Venta.class).get(0);
        }
        detalleVenta.setVenta(venta);
        return detalleVenta;
    }

    @BeforeEach
    public void initTest() {
        detalleVenta = createEntity(em);
    }

    @Test
    @Transactional
    void createDetalleVenta() throws Exception {
        int databaseSizeBeforeCreate = detalleVentaRepository.findAll().size();
        // Create the DetalleVenta
        restDetalleVentaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(detalleVenta)))
            .andExpect(status().isCreated());

        // Validate the DetalleVenta in the database
        List<DetalleVenta> detalleVentaList = detalleVentaRepository.findAll();
        assertThat(detalleVentaList).hasSize(databaseSizeBeforeCreate + 1);
        DetalleVenta testDetalleVenta = detalleVentaList.get(detalleVentaList.size() - 1);
        assertThat(testDetalleVenta.getDescuento()).isEqualTo(DEFAULT_DESCUENTO);
    }

    @Test
    @Transactional
    void createDetalleVentaWithExistingId() throws Exception {
        // Create the DetalleVenta with an existing ID
        detalleVenta.setId(1L);

        int databaseSizeBeforeCreate = detalleVentaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDetalleVentaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(detalleVenta)))
            .andExpect(status().isBadRequest());

        // Validate the DetalleVenta in the database
        List<DetalleVenta> detalleVentaList = detalleVentaRepository.findAll();
        assertThat(detalleVentaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDescuentoIsRequired() throws Exception {
        int databaseSizeBeforeTest = detalleVentaRepository.findAll().size();
        // set the field null
        detalleVenta.setDescuento(null);

        // Create the DetalleVenta, which fails.

        restDetalleVentaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(detalleVenta)))
            .andExpect(status().isBadRequest());

        List<DetalleVenta> detalleVentaList = detalleVentaRepository.findAll();
        assertThat(detalleVentaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDetalleVentas() throws Exception {
        // Initialize the database
        detalleVentaRepository.saveAndFlush(detalleVenta);

        // Get all the detalleVentaList
        restDetalleVentaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(detalleVenta.getId().intValue())))
            .andExpect(jsonPath("$.[*].descuento").value(hasItem(DEFAULT_DESCUENTO)));
    }

    @Test
    @Transactional
    void getDetalleVenta() throws Exception {
        // Initialize the database
        detalleVentaRepository.saveAndFlush(detalleVenta);

        // Get the detalleVenta
        restDetalleVentaMockMvc
            .perform(get(ENTITY_API_URL_ID, detalleVenta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(detalleVenta.getId().intValue()))
            .andExpect(jsonPath("$.descuento").value(DEFAULT_DESCUENTO));
    }

    @Test
    @Transactional
    void getNonExistingDetalleVenta() throws Exception {
        // Get the detalleVenta
        restDetalleVentaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDetalleVenta() throws Exception {
        // Initialize the database
        detalleVentaRepository.saveAndFlush(detalleVenta);

        int databaseSizeBeforeUpdate = detalleVentaRepository.findAll().size();

        // Update the detalleVenta
        DetalleVenta updatedDetalleVenta = detalleVentaRepository.findById(detalleVenta.getId()).get();
        // Disconnect from session so that the updates on updatedDetalleVenta are not directly saved in db
        em.detach(updatedDetalleVenta);
        updatedDetalleVenta.descuento(UPDATED_DESCUENTO);

        restDetalleVentaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDetalleVenta.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDetalleVenta))
            )
            .andExpect(status().isOk());

        // Validate the DetalleVenta in the database
        List<DetalleVenta> detalleVentaList = detalleVentaRepository.findAll();
        assertThat(detalleVentaList).hasSize(databaseSizeBeforeUpdate);
        DetalleVenta testDetalleVenta = detalleVentaList.get(detalleVentaList.size() - 1);
        assertThat(testDetalleVenta.getDescuento()).isEqualTo(UPDATED_DESCUENTO);
    }

    @Test
    @Transactional
    void putNonExistingDetalleVenta() throws Exception {
        int databaseSizeBeforeUpdate = detalleVentaRepository.findAll().size();
        detalleVenta.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDetalleVentaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, detalleVenta.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(detalleVenta))
            )
            .andExpect(status().isBadRequest());

        // Validate the DetalleVenta in the database
        List<DetalleVenta> detalleVentaList = detalleVentaRepository.findAll();
        assertThat(detalleVentaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDetalleVenta() throws Exception {
        int databaseSizeBeforeUpdate = detalleVentaRepository.findAll().size();
        detalleVenta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDetalleVentaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(detalleVenta))
            )
            .andExpect(status().isBadRequest());

        // Validate the DetalleVenta in the database
        List<DetalleVenta> detalleVentaList = detalleVentaRepository.findAll();
        assertThat(detalleVentaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDetalleVenta() throws Exception {
        int databaseSizeBeforeUpdate = detalleVentaRepository.findAll().size();
        detalleVenta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDetalleVentaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(detalleVenta)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DetalleVenta in the database
        List<DetalleVenta> detalleVentaList = detalleVentaRepository.findAll();
        assertThat(detalleVentaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDetalleVentaWithPatch() throws Exception {
        // Initialize the database
        detalleVentaRepository.saveAndFlush(detalleVenta);

        int databaseSizeBeforeUpdate = detalleVentaRepository.findAll().size();

        // Update the detalleVenta using partial update
        DetalleVenta partialUpdatedDetalleVenta = new DetalleVenta();
        partialUpdatedDetalleVenta.setId(detalleVenta.getId());

        restDetalleVentaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDetalleVenta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDetalleVenta))
            )
            .andExpect(status().isOk());

        // Validate the DetalleVenta in the database
        List<DetalleVenta> detalleVentaList = detalleVentaRepository.findAll();
        assertThat(detalleVentaList).hasSize(databaseSizeBeforeUpdate);
        DetalleVenta testDetalleVenta = detalleVentaList.get(detalleVentaList.size() - 1);
        assertThat(testDetalleVenta.getDescuento()).isEqualTo(DEFAULT_DESCUENTO);
    }

    @Test
    @Transactional
    void fullUpdateDetalleVentaWithPatch() throws Exception {
        // Initialize the database
        detalleVentaRepository.saveAndFlush(detalleVenta);

        int databaseSizeBeforeUpdate = detalleVentaRepository.findAll().size();

        // Update the detalleVenta using partial update
        DetalleVenta partialUpdatedDetalleVenta = new DetalleVenta();
        partialUpdatedDetalleVenta.setId(detalleVenta.getId());

        partialUpdatedDetalleVenta.descuento(UPDATED_DESCUENTO);

        restDetalleVentaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDetalleVenta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDetalleVenta))
            )
            .andExpect(status().isOk());

        // Validate the DetalleVenta in the database
        List<DetalleVenta> detalleVentaList = detalleVentaRepository.findAll();
        assertThat(detalleVentaList).hasSize(databaseSizeBeforeUpdate);
        DetalleVenta testDetalleVenta = detalleVentaList.get(detalleVentaList.size() - 1);
        assertThat(testDetalleVenta.getDescuento()).isEqualTo(UPDATED_DESCUENTO);
    }

    @Test
    @Transactional
    void patchNonExistingDetalleVenta() throws Exception {
        int databaseSizeBeforeUpdate = detalleVentaRepository.findAll().size();
        detalleVenta.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDetalleVentaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, detalleVenta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(detalleVenta))
            )
            .andExpect(status().isBadRequest());

        // Validate the DetalleVenta in the database
        List<DetalleVenta> detalleVentaList = detalleVentaRepository.findAll();
        assertThat(detalleVentaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDetalleVenta() throws Exception {
        int databaseSizeBeforeUpdate = detalleVentaRepository.findAll().size();
        detalleVenta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDetalleVentaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(detalleVenta))
            )
            .andExpect(status().isBadRequest());

        // Validate the DetalleVenta in the database
        List<DetalleVenta> detalleVentaList = detalleVentaRepository.findAll();
        assertThat(detalleVentaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDetalleVenta() throws Exception {
        int databaseSizeBeforeUpdate = detalleVentaRepository.findAll().size();
        detalleVenta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDetalleVentaMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(detalleVenta))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DetalleVenta in the database
        List<DetalleVenta> detalleVentaList = detalleVentaRepository.findAll();
        assertThat(detalleVentaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDetalleVenta() throws Exception {
        // Initialize the database
        detalleVentaRepository.saveAndFlush(detalleVenta);

        int databaseSizeBeforeDelete = detalleVentaRepository.findAll().size();

        // Delete the detalleVenta
        restDetalleVentaMockMvc
            .perform(delete(ENTITY_API_URL_ID, detalleVenta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DetalleVenta> detalleVentaList = detalleVentaRepository.findAll();
        assertThat(detalleVentaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
