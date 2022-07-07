package com.concesionario.app.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Vendedor.
 */
@Entity
@Table(name = "vendedor")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Vendedor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @NotNull
    @Column(name = "primer_apellido", nullable = false)
    private String primerApellido;

    @NotNull
    @Column(name = "segundo_apellido", nullable = false)
    private String segundoApellido;

    @NotNull
    @Column(name = "mail", nullable = false)
    private String mail;

    @NotNull
    @Column(name = "tipo_identificacion", nullable = false)
    private String tipoIdentificacion;

    @NotNull
    @Column(name = "num_identificacion", nullable = false)
    private String numIdentificacion;

    @NotNull
    @Column(name = "direccion", nullable = false)
    private String direccion;

    @NotNull
    @Column(name = "cargo", nullable = false)
    private String cargo;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Vendedor id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Vendedor nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPrimerApellido() {
        return this.primerApellido;
    }

    public Vendedor primerApellido(String primerApellido) {
        this.setPrimerApellido(primerApellido);
        return this;
    }

    public void setPrimerApellido(String primerApellido) {
        this.primerApellido = primerApellido;
    }

    public String getSegundoApellido() {
        return this.segundoApellido;
    }

    public Vendedor segundoApellido(String segundoApellido) {
        this.setSegundoApellido(segundoApellido);
        return this;
    }

    public void setSegundoApellido(String segundoApellido) {
        this.segundoApellido = segundoApellido;
    }

    public String getMail() {
        return this.mail;
    }

    public Vendedor mail(String mail) {
        this.setMail(mail);
        return this;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getTipoIdentificacion() {
        return this.tipoIdentificacion;
    }

    public Vendedor tipoIdentificacion(String tipoIdentificacion) {
        this.setTipoIdentificacion(tipoIdentificacion);
        return this;
    }

    public void setTipoIdentificacion(String tipoIdentificacion) {
        this.tipoIdentificacion = tipoIdentificacion;
    }

    public String getNumIdentificacion() {
        return this.numIdentificacion;
    }

    public Vendedor numIdentificacion(String numIdentificacion) {
        this.setNumIdentificacion(numIdentificacion);
        return this;
    }

    public void setNumIdentificacion(String numIdentificacion) {
        this.numIdentificacion = numIdentificacion;
    }

    public String getDireccion() {
        return this.direccion;
    }

    public Vendedor direccion(String direccion) {
        this.setDireccion(direccion);
        return this;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCargo() {
        return this.cargo;
    }

    public Vendedor cargo(String cargo) {
        this.setCargo(cargo);
        return this;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vendedor)) {
            return false;
        }
        return id != null && id.equals(((Vendedor) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Vendedor{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", primerApellido='" + getPrimerApellido() + "'" +
            ", segundoApellido='" + getSegundoApellido() + "'" +
            ", mail='" + getMail() + "'" +
            ", tipoIdentificacion='" + getTipoIdentificacion() + "'" +
            ", numIdentificacion='" + getNumIdentificacion() + "'" +
            ", direccion='" + getDireccion() + "'" +
            ", cargo='" + getCargo() + "'" +
            "}";
    }
}
