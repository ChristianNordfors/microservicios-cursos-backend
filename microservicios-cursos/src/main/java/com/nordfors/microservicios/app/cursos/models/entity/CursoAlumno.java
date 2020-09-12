package com.nordfors.microservicios.app.cursos.models.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="cursos_alumnos")
public class CursoAlumno {
	
	// Aca el id de los alumnos no es una llave. Solamente almacena los id de los alumnos
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	// Se indica que tiene que estar registrado una sola vez
	@Column(name="alumno_id", unique = true)
	private Long alumnoId;
	
	// Relacion bidireccional entre cursos y alumnos para evitar que se genere una tabla intermedia
	// Almacenando curso id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="curso_id")
	@JsonIgnoreProperties(value= {"cursoAlumnos"})
	private Curso curso;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAlumnoId() {
		return alumnoId;
	}

	public void setAlumnoId(Long alumnoId) {
		this.alumnoId = alumnoId;
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}
	
	
	public boolean equals(Object obj) {
		
		if(this==obj) {
			return true;
		}
		
		if(!(obj instanceof CursoAlumno)) {
			return false;
		}
		
		CursoAlumno a = (CursoAlumno) obj;
		
		// Validacion con alumnoId 
		return this.alumnoId != null && this.alumnoId.equals(a.getAlumnoId());
	}
}
