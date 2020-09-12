package com.nordfors.microservicios.app.cursos.models.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.nordfors.microservicios.app.cursos.models.entity.Curso;

public interface CursoRepository extends PagingAndSortingRepository<Curso, Long> {
	
	@Query("select c from Curso c join fetch c.cursoAlumnos a where a.alumnoId=?1")
	public Curso findCursoByAlumnoId(Long id);
	
	// @Modifying cuando se usa un @Query para eliminar o modificar un registro(UPDATE, INSERT, DELETE..)
	@Modifying
	@Query("delete from CursoAlumno ca where ca.alumnoId=?1")
	public void eliminarCursoAlumnoPorId(Long id);

}
