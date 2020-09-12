package com.nordfors.microservicios.app.cursos.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nordfors.microservicios.app.cursos.clients.AlumnoFeignClient;
import com.nordfors.microservicios.app.cursos.clients.RespuestaFeignCliente;
import com.nordfors.microservicios.app.cursos.models.entity.Curso;
import com.nordfors.microservicios.app.cursos.models.repository.CursoRepository;
import com.nordfors.microservicios.commons.alumnos.models.entity.Alumno;
import com.nordfors.microservicios.commons.services.CommonServiceImpl;

@Service
public class CursoServiceImpl extends CommonServiceImpl<Curso, CursoRepository> implements CursoService {

	// Se puede inyectar por las anotaciones propias de client @Enable.. y @FeignClient
	@Autowired
	private RespuestaFeignCliente client;
	
	@Autowired
	private AlumnoFeignClient clientAlumno;
	
	@Override
	@Transactional(readOnly = true)
	public Curso findCursoByAlumnoId(Long id) {
		return repository.findCursoByAlumnoId(id);
	}

	// No lleva @Transactional -los de feign
	@Override
	public Iterable<Long> obtenerExamenesIdsConRespuestasAlumnos(Long alumnoId) {
		return client.obtenerExamenesIdsConRespuestasAlumnos(alumnoId);
	}

	@Override
	public Iterable<Alumno> obtenerAlumnosPorCurso(Iterable<Long> ids) {
		return clientAlumno.obtenerAlumnosPorCurso(ids);
	}

	@Override
	@Transactional
	public void eliminarCursoAlumnoPorId(Long id) {
		repository.eliminarCursoAlumnoPorId(id);
	}



}
