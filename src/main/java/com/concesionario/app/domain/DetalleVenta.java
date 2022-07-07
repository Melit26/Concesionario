package com.concesionario.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DetalleVenta.
 */
@Entity
@Table(name = "detalle_venta")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DetalleVenta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "descuento", nullable = false)
    private String descuento;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "cliente" }, allowSetters = true)
    private Venta venta;

    @OneToOne
    @JoinColumn(unique = true)
    private Coche coche;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DetalleVenta id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescuento() {
        return this.descuento;
    }

    public DetalleVenta descuento(String descuento) {
        this.setDescuento(descuento);
        return this;
    }

    public void setDescuento(String descuento) {
        this.descuento = descuento;
    }

    public Venta getVenta() {
        return this.venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    public DetalleVenta venta(Venta venta) {
        this.setVenta(venta);
        return this;
    }

    public Coche getCoche() {
        return this.coche;
    }

    public void setCoche(Coche coche) {
        this.coche = coche;
    }

    public DetalleVenta coche(Coche coche) {
        this.setCoche(coche);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DetalleVenta)) {
            return false;
        }
        return id != null && id.equals(((DetalleVenta) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DetalleVenta{" +
            "id=" + getId() +
            ", descuento='" + getDescuento() + "'" +
            "}";
    }
}
