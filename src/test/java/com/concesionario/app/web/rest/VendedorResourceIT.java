package com.concesionario.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.concesionario.app.IntegrationTest;
import com.concesionario.app.domain.Vendedor;
import com.concesionario.app.repository.VendedorRepository;
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
 * Integration tests for the {@link VendedorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VendedorResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_PRIMER_APELLIDO = "AAAAAAAAAA";
    private static final String UPDATED_PRIMER_APELLIDO = "BBBBBBBBBB";

    private static final String DEFAULT_SEGUNDO_APELLIDO = "AAAAAAAAAA";
    private static final String UPDATED_SEGUNDO_APELLIDO = "BBBBBBBBBB";

    private static final String DEFAULT_MAIL = "AAAAAAAAAA";
    private static final String UPDATED_MAIL = "BBBBBBBBBB";

    private static final String DEFAULT_TIPO_IDENTIFICACION = "AAAAAAAAAA";
    private static final String UPDATED_TIPO_IDENTIFICACION = "BBBBBBBBBB";

    private static final String DEFAULT_NUM_IDENTIFICACION = "AAAAAAAAAA";
    private static final String UPDATED_NUM_IDENTIFICACION = "BBBBBBBBBB";

    private static final String DEFAULT_DIRECCION = "AAAAAAAAAA";
    private static final String UPDATED_DIRECCION = "BBBBBBBBBB";

    private static final String DEFAULT_CARGO = "AAAAAAAAAA";
    private static final String UPDATED_CARGO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/vendedors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VendedorRepository vendedorRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVendedorMockMvc;

    private Vendedor vendedor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vendedor createEntity(EntityManager em) {
        Vendedor vendedor = new Vendedor()
            .nombre(DEFAULT_NOMBRE)
            .primerApellido(DEFAULT_PRIMER_APELLIDO)
            .segundoApellido(DEFAULT_SEGUNDO_APELLIDO)
            .mail(DEFAULT_MAIL)
            .tipoIdentificacion(DEFAULT_TIPO_IDENTIFICACION)
            .numIdentificacion(DEFAULT_NUM_IDENTIFICACION)
            .direccion(DEFAULT_DIRECCION)
            .cargo(DEFAULT_CARGO);
        return vendedor;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vendedor createUpdatedEntity(EntityManager em) {
        Vendedor vendedor = new Vendedor()
            .nombre(UPDATED_NOMBRE)
            .primerApellido(UPDATED_PRIMER_APELLIDO)
            .segundoApellido(UPDATED_SEGUNDO_APELLIDO)
            .mail(UPDATED_MAIL)
            .tipoIdentificacion(UPDATED_TIPO_IDENTIFICACION)
            .numIdentificacion(UPDATED_NUM_IDENTIFICACION)
            .direccion(UPDATED_DIRECCION)
            .cargo(UPDATED_CARGO);
        return vendedor;
    }

    @BeforeEach
    public void initTest() {
        vendedor = createEntity(em);
    }

    @Test
    @Transactional
    void createVendedor() throws Exception {
        int databaseSizeBeforeCreate = vendedorRepository.findAll().size();
        // Create the Vendedor
        restVendedorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vendedor)))
            .andExpect(status().isCreated());

        // Validate the Vendedor in the database
        List<Vendedor> vendedorList = vendedorRepository.findAll();
        assertThat(vendedorList).hasSize(databaseSizeBeforeCreate + 1);
        Vendedor testVendedor = vendedorList.get(vendedorList.size() - 1);
        assertThat(testVendedor.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testVendedor.getPrimerApellido()).isEqualTo(DEFAULT_PRIMER_APELLIDO);
        assertThat(testVendedor.getSegundoApellido()).isEqualTo(DEFAULT_SEGUNDO_APELLIDO);
        assertThat(testVendedor.getMail()).isEqualTo(DEFAULT_MAIL);
        assertThat(testVendedor.getTipoIdentificacion()).isEqualTo(DEFAULT_TIPO_IDENTIFICACION);
        assertThat(testVendedor.getNumIdentificacion()).isEqualTo(DEFAULT_NUM_IDENTIFICACION);
        assertThat(testVendedor.getDireccion()).isEqualTo(DEFAULT_DIRECCION);
        assertThat(testVendedor.getCargo()).isEqualTo(DEFAULT_CARGO);
    }

    @Test
    @Transactional
    void createVendedorWithExistingId() throws Exception {
        // Create the Vendedor with an existing ID
        vendedor.setId(1L);

        int databaseSizeBeforeCreate = vendedorRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVendedorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vendedor)))
            .andExpect(status().isBadRequest());

        // Validate the Vendedor in the database
        List<Vendedor> vendedorList = vendedorRepository.findAll();
        assertThat(vendedorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = vendedorRepository.findAll().size();
        // set the field null
        vendedor.setNombre(null);

        // Create the Vendedor, which fails.

        restVendedorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vendedor)))
            .andExpect(status().isBadRequest());

        List<Vendedor> vendedorList = vendedorRepository.findAll();
        assertThat(vendedorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrimerApellidoIsRequired() throws Exception {
        int databaseSizeBeforeTest = vendedorRepository.findAll().size();
        // set the field null
        vendedor.setPrimerApellido(null);

        // Create the Vendedor, which fails.

        restVendedorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vendedor)))
            .andExpect(status().isBadRequest());

        List<Vendedor> vendedorList = vendedorRepository.findAll();
        assertThat(vendedorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSegundoApellidoIsRequired() throws Exception {
        int databaseSizeBeforeTest = vendedorRepository.findAll().size();
        // set the field null
        vendedor.setSegundoApellido(null);

        // Create the Vendedor, which fails.

        restVendedorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vendedor)))
            .andExpect(status().isBadRequest());

        List<Vendedor> vendedorList = vendedorRepository.findAll();
        assertThat(vendedorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMailIsRequired() throws Exception {
        int databaseSizeBeforeTest = vendedorRepository.findAll().size();
        // set the field null
        vendedor.setMail(null);

        // Create the Vendedor, which fails.

        restVendedorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vendedor)))
            .andExpect(status().isBadRequest());

        List<Vendedor> vendedorList = vendedorRepository.findAll();
        assertThat(vendedorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTipoIdentificacionIsRequired() throws Exception {
        int databaseSizeBeforeTest = vendedorRepository.findAll().size();
        // set the field null
        vendedor.setTipoIdentificacion(null);

        // Create the Vendedor, which fails.

        restVendedorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vendedor)))
            .andExpect(status().isBadRequest());

        List<Vendedor> vendedorList = vendedorRepository.findAll();
        assertThat(vendedorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNumIdentificacionIsRequired() throws Exception {
        int databaseSizeBeforeTest = vendedorRepository.findAll().size();
        // set the field null
        vendedor.setNumIdentificacion(null);

        // Create the Vendedor, which fails.

        restVendedorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vendedor)))
            .andExpect(status().isBadRequest());

        List<Vendedor> vendedorList = vendedorRepository.findAll();
        assertThat(vendedorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDireccionIsRequired() throws Exception {
        int databaseSizeBeforeTest = vendedorRepository.findAll().size();
        // set the field null
        vendedor.setDireccion(null);

        // Create the Vendedor, which fails.

        restVendedorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vendedor)))
            .andExpect(status().isBadRequest());

        List<Vendedor> vendedorList = vendedorRepository.findAll();
        assertThat(vendedorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCargoIsRequired() throws Exception {
        int databaseSizeBeforeTest = vendedorRepository.findAll().size();
        // set the field null
        vendedor.setCargo(null);

        // Create the Vendedor, which fails.

        restVendedorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vendedor)))
            .andExpect(status().isBadRequest());

        List<Vendedor> vendedorList = vendedorRepository.findAll();
        assertThat(vendedorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVendedors() throws Exception {
        // Initialize the database
        vendedorRepository.saveAndFlush(vendedor);

        // Get all the vendedorList
        restVendedorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vendedor.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].primerApellido").value(hasItem(DEFAULT_PRIMER_APELLIDO)))
            .andExpect(jsonPath("$.[*].segundoApellido").value(hasItem(DEFAULT_SEGUNDO_APELLIDO)))
            .andExpect(jsonPath("$.[*].mail").value(hasItem(DEFAULT_MAIL)))
            .andExpect(jsonPath("$.[*].tipoIdentificacion").value(hasItem(DEFAULT_TIPO_IDENTIFICACION)))
            .andExpect(jsonPath("$.[*].numIdentificacion").value(hasItem(DEFAULT_NUM_IDENTIFICACION)))
            .andExpect(jsonPath("$.[*].direccion").value(hasItem(DEFAULT_DIRECCION)))
            .andExpect(jsonPath("$.[*].cargo").value(hasItem(DEFAULT_CARGO)));
    }

    @Test
    @Transactional
    void getVendedor() throws Exception {
        // Initialize the database
        vendedorRepository.saveAndFlush(vendedor);

        // Get the vendedor
        restVendedorMockMvc
            .perform(get(ENTITY_API_URL_ID, vendedor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vendedor.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.primerApellido").value(DEFAULT_PRIMER_APELLIDO))
            .andExpect(jsonPath("$.segundoApellido").value(DEFAULT_SEGUNDO_APELLIDO))
            .andExpect(jsonPath("$.mail").value(DEFAULT_MAIL))
            .andExpect(jsonPath("$.tipoIdentificacion").value(DEFAULT_TIPO_IDENTIFICACION))
            .andExpect(jsonPath("$.numIdentificacion").value(DEFAULT_NUM_IDENTIFICACION))
            .andExpect(jsonPath("$.direccion").value(DEFAULT_DIRECCION))
            .andExpect(jsonPath("$.cargo").value(DEFAULT_CARGO));
    }

    @Test
    @Transactional
    void getNonExistingVendedor() throws Exception {
        // Get the vendedor
        restVendedorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewVendedor() throws Exception {
        // Initialize the database
        vendedorRepository.saveAndFlush(vendedor);

        int databaseSizeBeforeUpdate = vendedorRepository.findAll().size();

        // Update the vendedor
        Vendedor updatedVendedor = vendedorRepository.findById(vendedor.getId()).get();
        // Disconnect from session so that the updates on updatedVendedor are not directly saved in db
        em.detach(updatedVendedor);
        updatedVendedor
            .nombre(UPDATED_NOMBRE)
            .primerApellido(UPDATED_PRIMER_APELLIDO)
            .segundoApellido(UPDATED_SEGUNDO_APELLIDO)
            .mail(UPDATED_MAIL)
            .tipoIdentificacion(UPDATED_TIPO_IDENTIFICACION)
            .numIdentificacion(UPDATED_NUM_IDENTIFICACION)
            .direccion(UPDATED_DIRECCION)
            .cargo(UPDATED_CARGO);

        restVendedorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVendedor.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedVendedor))
            )
            .andExpect(status().isOk());

        // Validate the Vendedor in the database
        List<Vendedor> vendedorList = vendedorRepository.findAll();
        assertThat(vendedorList).hasSize(databaseSizeBeforeUpdate);
        Vendedor testVendedor = vendedorList.get(vendedorList.size() - 1);
        assertThat(testVendedor.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testVendedor.getPrimerApellido()).isEqualTo(UPDATED_PRIMER_APELLIDO);
        assertThat(testVendedor.getSegundoApellido()).isEqualTo(UPDATED_SEGUNDO_APELLIDO);
        assertThat(testVendedor.getMail()).isEqualTo(UPDATED_MAIL);
        assertThat(testVendedor.getTipoIdentificacion()).isEqualTo(UPDATED_TIPO_IDENTIFICACION);
        assertThat(testVendedor.getNumIdentificacion()).isEqualTo(UPDATED_NUM_IDENTIFICACION);
        assertThat(testVendedor.getDireccion()).isEqualTo(UPDATED_DIRECCION);
        assertThat(testVendedor.getCargo()).isEqualTo(UPDATED_CARGO);
    }

    @Test
    @Transactional
    void putNonExistingVendedor() throws Exception {
        int databaseSizeBeforeUpdate = vendedorRepository.findAll().size();
        vendedor.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVendedorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vendedor.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vendedor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vendedor in the database
        List<Vendedor> vendedorList = vendedorRepository.findAll();
        assertThat(vendedorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVendedor() throws Exception {
        int databaseSizeBeforeUpdate = vendedorRepository.findAll().size();
        vendedor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVendedorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vendedor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vendedor in the database
        List<Vendedor> vendedorList = vendedorRepository.findAll();
        assertThat(vendedorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVendedor() throws Exception {
        int databaseSizeBeforeUpdate = vendedorRepository.findAll().size();
        vendedor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVendedorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vendedor)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vendedor in the database
        List<Vendedor> vendedorList = vendedorRepository.findAll();
        assertThat(vendedorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVendedorWithPatch() throws Exception {
        // Initialize the database
        vendedorRepository.saveAndFlush(vendedor);

        int databaseSizeBeforeUpdate = vendedorRepository.findAll().size();

        // Update the vendedor using partial update
        Vendedor partialUpdatedVendedor = new Vendedor();
        partialUpdatedVendedor.setId(vendedor.getId());

        partialUpdatedVendedor.nombre(UPDATED_NOMBRE).numIdentificacion(UPDATED_NUM_IDENTIFICACION).cargo(UPDATED_CARGO);

        restVendedorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVendedor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVendedor))
            )
            .andExpect(status().isOk());

        // Validate the Vendedor in the database
        List<Vendedor> vendedorList = vendedorRepository.findAll();
        assertThat(vendedorList).hasSize(databaseSizeBeforeUpdate);
        Vendedor testVendedor = vendedorList.get(vendedorList.size() - 1);
        assertThat(testVendedor.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testVendedor.getPrimerApellido()).isEqualTo(DEFAULT_PRIMER_APELLIDO);
        assertThat(testVendedor.getSegundoApellido()).isEqualTo(DEFAULT_SEGUNDO_APELLIDO);
        assertThat(testVendedor.getMail()).isEqualTo(DEFAULT_MAIL);
        assertThat(testVendedor.getTipoIdentificacion()).isEqualTo(DEFAULT_TIPO_IDENTIFICACION);
        assertThat(testVendedor.getNumIdentificacion()).isEqualTo(UPDATED_NUM_IDENTIFICACION);
        assertThat(testVendedor.getDireccion()).isEqualTo(DEFAULT_DIRECCION);
        assertThat(testVendedor.getCargo()).isEqualTo(UPDATED_CARGO);
    }

    @Test
    @Transactional
    void fullUpdateVendedorWithPatch() throws Exception {
        // Initialize the database
        vendedorRepository.saveAndFlush(vendedor);

        int databaseSizeBeforeUpdate = vendedorRepository.findAll().size();

        // Update the vendedor using partial update
        Vendedor partialUpdatedVendedor = new Vendedor();
        partialUpdatedVendedor.setId(vendedor.getId());

        partialUpdatedVendedor
            .nombre(UPDATED_NOMBRE)
            .primerApellido(UPDATED_PRIMER_APELLIDO)
            .segundoApellido(UPDATED_SEGUNDO_APELLIDO)
            .mail(UPDATED_MAIL)
            .tipoIdentificacion(UPDATED_TIPO_IDENTIFICACION)
            .numIdentificacion(UPDATED_NUM_IDENTIFICACION)
            .direccion(UPDATED_DIRECCION)
            .cargo(UPDATED_CARGO);

        restVendedorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVendedor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVendedor))
            )
            .andExpect(status().isOk());

        // Validate the Vendedor in the database
        List<Vendedor> vendedorList = vendedorRepository.findAll();
        assertThat(vendedorList).hasSize(databaseSizeBeforeUpdate);
        Vendedor testVendedor = vendedorList.get(vendedorList.size() - 1);
        assertThat(testVendedor.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testVendedor.getPrimerApellido()).isEqualTo(UPDATED_PRIMER_APELLIDO);
        assertThat(testVendedor.getSegundoApellido()).isEqualTo(UPDATED_SEGUNDO_APELLIDO);
        assertThat(testVendedor.getMail()).isEqualTo(UPDATED_MAIL);
        assertThat(testVendedor.getTipoIdentificacion()).isEqualTo(UPDATED_TIPO_IDENTIFICACION);
        assertThat(testVendedor.getNumIdentificacion()).isEqualTo(UPDATED_NUM_IDENTIFICACION);
        assertThat(testVendedor.getDireccion()).isEqualTo(UPDATED_DIRECCION);
        assertThat(testVendedor.getCargo()).isEqualTo(UPDATED_CARGO);
    }

    @Test
    @Transactional
    void patchNonExistingVendedor() throws Exception {
        int databaseSizeBeforeUpdate = vendedorRepository.findAll().size();
        vendedor.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVendedorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vendedor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vendedor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vendedor in the database
        List<Vendedor> vendedorList = vendedorRepository.findAll();
        assertThat(vendedorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVendedor() throws Exception {
        int databaseSizeBeforeUpdate = vendedorRepository.findAll().size();
        vendedor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVendedorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vendedor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vendedor in the database
        List<Vendedor> vendedorList = vendedorRepository.findAll();
        assertThat(vendedorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVendedor() throws Exception {
        int databaseSizeBeforeUpdate = vendedorRepository.findAll().size();
        vendedor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVendedorMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(vendedor)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vendedor in the database
        List<Vendedor> vendedorList = vendedorRepository.findAll();
        assertThat(vendedorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVendedor() throws Exception {
        // Initialize the database
        vendedorRepository.saveAndFlush(vendedor);

        int databaseSizeBeforeDelete = vendedorRepository.findAll().size();

        // Delete the vendedor
        restVendedorMockMvc
            .perform(delete(ENTITY_API_URL_ID, vendedor.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Vendedor> vendedorList = vendedorRepository.findAll();
        assertThat(vendedorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
