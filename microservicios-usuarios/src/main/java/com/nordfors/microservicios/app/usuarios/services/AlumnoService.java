package com.nordfors.microservicios.app.usuarios.services;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;

import com.nordfors.microservicios.commons.alumnos.models.entity.Alumno;
import com.nordfors.microservicios.commons.services.CommonService;

public interface AlumnoService extends CommonService<Alumno>{
	
	public List<Alumno> findByNombreOrApellido(String term);
	
	public Iterable<Alumno> findAllById(Iterable<Long> ids);
	
	public void eliminarCursoAlumnoPorId(@PathVariable Long id);

	// Ahora se use desde commons
//	public Iterable<Alumno> findAll();
//	
//	public Optional<Alumno> findById(Long id);
//	
//	public Alumno save(Alumno alumno);
//	
//	public void deleteById(Long id);

}
