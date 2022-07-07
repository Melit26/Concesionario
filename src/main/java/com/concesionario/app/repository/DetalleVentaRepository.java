package com.concesionario.app.repository;

import com.concesionario.app.domain.DetalleVenta;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DetalleVenta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Long> {}
