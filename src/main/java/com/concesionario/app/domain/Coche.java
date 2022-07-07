package com.concesionario.app.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Coche.
 */
@Entity
@Table(name = "coche")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Coche implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "marca", nullable = false)
    private String marca;

    @NotNull
    @Column(name = "modelo", nullable = false)
    private String modelo;

    @NotNull
    @Column(name = "kilometraje", nullable = false)
    private Double kilometraje;

    @NotNull
    @Column(name = "color", nullable = false)
    private String color;

    @NotNull
    @Column(name = "tipo_coche", nullable = false)
    private String tipoCoche;

    @NotNull
    @Column(name = "estado", nullable = false)
    private String estado;

    @NotNull
    @Column(name = "precio", nullable = false)
    private Double precio;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Coche id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMarca() {
        return this.marca;
    }

    public Coche marca(String marca) {
        this.setMarca(marca);
        return this;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return this.modelo;
    }

    public Coche modelo(String modelo) {
        this.setModelo(modelo);
        return this;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Double getKilometraje() {
        return this.kilometraje;
    }

    public Coche kilometraje(Double kilometraje) {
        this.setKilometraje(kilometraje);
        return this;
    }

    public void setKilometraje(Double kilometraje) {
        this.kilometraje = kilometraje;
    }

    public String getColor() {
        return this.color;
    }

    public Coche color(String color) {
        this.setColor(color);
        return this;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTipoCoche() {
        return this.tipoCoche;
    }

    public Coche tipoCoche(String tipoCoche) {
        this.setTipoCoche(tipoCoche);
        return this;
    }

    public void setTipoCoche(String tipoCoche) {
        this.tipoCoche = tipoCoche;
    }

    public String getEstado() {
        return this.estado;
    }

    public Coche estado(String estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Double getPrecio() {
        return this.precio;
    }

    public Coche precio(Double precio) {
        this.setPrecio(precio);
        return this;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Coche)) {
            return false;
        }
        return id != null && id.equals(((Coche) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Coche{" +
            "id=" + getId() +
            ", marca='" + getMarca() + "'" +
            ", modelo='" + getModelo() + "'" +
            ", kilometraje=" + getKilometraje() +
            ", color='" + getColor() + "'" +
            ", tipoCoche='" + getTipoCoche() + "'" +
            ", estado='" + getEstado() + "'" +
            ", precio=" + getPrecio() +
            "}";
    }
}
