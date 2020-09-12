package com.nordfors.microservicios.commons.examenes.models.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="examenes")
public class Examen {
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty
	@Size(min = 4, max = 30)
	private String nombre;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_at")
	private Date createAt;
	
	@JsonIgnoreProperties(value= {"examen"}, allowSetters = true)
	@OneToMany(mappedBy = "examen", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Pregunta> preguntas;
	
	@NotNull
	@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
	@ManyToOne(fetch = FetchType.LAZY)
	private Asignatura asignaturaPadre;
	
	@NotNull
	@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
	@ManyToOne(fetch = FetchType.LAZY)
	private Asignatura asignaturaHija;
	
	// Hay que evitar que se mapee a la base de datos porque quizas no todos los alumnos respondieron el examen
	@Transient
	private boolean respondido;
	
	
	public Examen() {
		this.preguntas = new ArrayList<>();
	}
	
	
	@PrePersist
	public void prePersist() {
		this.createAt = new Date();
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

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}



	public List<Pregunta> getPreguntas() {
		return preguntas;
	}


	
	
	public Asignatura getAsignaturaPadre() {
		return asignaturaPadre;
	}


	public void setAsignaturaPadre(Asignatura asignaturaPadre) {
		this.asignaturaPadre = asignaturaPadre;
	}


	public Asignatura getAsignaturaHija() {
		return asignaturaHija;
	}


	public void setAsignaturaHija(Asignatura asignaturaHija) {
		this.asignaturaHija = asignaturaHija;
	}


	// Tambien debe asignar la pregunta a un examen
	public void setPreguntas(List<Pregunta> preguntas) {
		// Reinicia preguntas. Si se inicia directamente con new ArrayList garbagecollector no va a saber que eliminar
		// Se eliminan y se vuelven a asignar los objetos que se otienen en el request
		this.preguntas.clear();
		
		// Por cada pregunta que se envia de le asigna a la instancia de examan con this
//		preguntas.forEach(p -> this.addPregunta(p));
		preguntas.forEach(this::addPregunta);
	}
	
	
	
	
	public void addPregunta(Pregunta pregunta) {
		// Se agrega pregunta a la lista
		this.preguntas.add(pregunta);
		// Se asigna el examen de esta clase Examen con this a la pregunta
		// Establiendo asi la relacion inversa. Con su fk, sino la fk examen_id en la tabla preguntas seria null
		pregunta.setExamen(this);
	}
	
	public void removePregunta(Pregunta pregunta) {
		this.preguntas.remove(pregunta);
		pregunta.setExamen(null);
		// Para poder eliminar se necesita equals en pregunta para poder comparar y saber si la pregunta que se esta eliminando existe dentro de la lista de preguntas
		// Para eso hay que comparar por el id y si existe se elimina
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}
		
		if(!(obj instanceof Examen)) {
			return false;
		}
		
		Examen a = (Examen) obj;
		
		return this.id != null && this.id.equals(a.getId());
		
	}


	

	public boolean isRespondido() {
		return respondido;
	}


	public void setRespondido(boolean respondido) {
		this.respondido = respondido;
	}
	
	
	
	

}
