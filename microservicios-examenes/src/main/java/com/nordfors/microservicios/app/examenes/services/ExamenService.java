package com.nordfors.microservicios.app.examenes.services;

import java.util.List;

import com.nordfors.microservicios.commons.examenes.models.entity.Asignatura;
import com.nordfors.microservicios.commons.examenes.models.entity.Examen;
import com.nordfors.microservicios.commons.services.CommonService;

public interface ExamenService extends CommonService<Examen>{
	
	public List<Examen> findByNombre(String term);
	
	public Iterable<Asignatura> findAllAsignaturas();
	
	public Iterable<Long> findExamenesIdsConRespuestasByPreguntaIds(Iterable<Long> preguntaIds);

}
