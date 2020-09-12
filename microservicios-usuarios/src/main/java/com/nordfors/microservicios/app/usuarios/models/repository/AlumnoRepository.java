package com.nordfors.microservicios.app.usuarios.models.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.nordfors.microservicios.commons.alumnos.models.entity.Alumno;

public interface AlumnoRepository extends PagingAndSortingRepository<Alumno, Long> {
	
	// upper(concat('%', ?1, '%')) es necesario para postgre porque es case sensitive y upper no soporta %
	@Query("select a from Alumno a where upper(a.nombre) like upper(concat('%', ?1, '%')) or upper(a.apellido) like upper(concat('%', ?1, '%'))")
	public List<Alumno> findByNombreOrApellido(String term);
	
	
	// Es necesario en Postgres porque al actualizar un registro lo ubica al ultimo como si fuera uno nuevo
	// Metodo para listar ordenado por id con palabras con query methods con palabras claves
	public Iterable<Alumno> findAllByOrderByIdDesc();
	
	// Aca con paginacion
	public Page<Alumno> findAllByOrderByIdDesc(Pageable pageable);

}
