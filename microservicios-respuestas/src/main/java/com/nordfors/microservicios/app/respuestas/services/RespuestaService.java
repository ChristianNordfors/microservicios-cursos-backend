package com.nordfors.microservicios.app.respuestas.services;

import com.nordfors.microservicios.app.respuestas.models.entity.Respuesta;

public interface RespuestaService {
	
	public Iterable<Respuesta> saveAll(Iterable<Respuesta> respuestas);
	
	public Iterable<Respuesta> findRespuestaByAlumnoByExamen(Long alumnoId, Long examenId);
	
	public Iterable<Long> findExamenesIdsConRespuestasByAlumnos(Long alumnoId);
	
	public Iterable<Respuesta> findByAlumnoId(Long alumnoId);

}
