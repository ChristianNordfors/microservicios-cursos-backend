package com.nordfors.microservicios.app.cursos.services;

import com.nordfors.microservicios.app.cursos.models.entity.Curso;
import com.nordfors.microservicios.commons.alumnos.models.entity.Alumno;
import com.nordfors.microservicios.commons.services.CommonService;

public interface CursoService extends CommonService<Curso> {
	
	public Curso findCursoByAlumnoId(Long id);
	
	public Iterable<Long> obtenerExamenesIdsConRespuestasAlumnos(Long alumnoId);
	
	public Iterable<Alumno> obtenerAlumnosPorCurso(Iterable<Long> ids);
	
	public void eliminarCursoAlumnoPorId(Long id);

}
