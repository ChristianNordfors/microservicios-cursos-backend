package com.nordfors.microservicios.commons.alumnos.models.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "alumnos")
public class Alumno implements Serializable{



	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty
	private String nombre;
	
	@NotEmpty
	private String apellido;
	
	@NotEmpty
	@Email
	private String email;
	
	@Temporal(TemporalType.DATE)
	@Column(name="create_at")
	private Date createAt;
	
	
	@Lob
	@JsonIgnore
	private byte[] foto;
	
	
	@PrePersist
	public void prePersist() {
		this.createAt = new Date();
	}
	
	// Metodo para retornar un identificador de la foto
	// Todo metodo 'get' de la clase entity se va a serializar y generarse como un atributo en el json
	public Integer getFotoHashCode() {
		// Todos los object tienen un identificador que lo permite comparar en el metodo equals
		return (this.foto != null) ? this.foto.hashCode() : null;
	}
	
	
	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public String getNombre() {
		return nombre;
	}



	public void setNombre(String nombre) {
		this.nombre = nombre;
	}



	public String getApellido() {
		return apellido;
	}



	public void setApellido(String apellido) {
		this.apellido = apellido;
	}



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}



	public Date getCreateAt() {
		return createAt;
	}



	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}
	
	
	// Metodo para eliminar un objeto de la relacion con curso
	@Override
	public boolean equals(Object obj) {
		// si this, esta instancia, es igual al objeto que se esta pasando por argumento
		if(this == obj) {
			return true;
		}
		
		if(!(obj instanceof Alumno)) {
			return false;
		}
		Alumno a = (Alumno) obj;
		
		// Va a buscar por cada objeto de la lista hasta encontrarlo y lo elimina mientras se cumplan ambas condiciones
		return this.id != null && this.id.equals(a.getId());
	}

	
	


	public byte[] getFoto() {
		return foto;
	}

	public void setFoto(byte[] foto) {
		this.foto = foto;
	}





	private static final long serialVersionUID = 1L;

}
