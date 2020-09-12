package com.nordfors.microservicios.app.respuestas.services;


import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;

import com.nordfors.microservicios.app.respuestas.clients.ExamenFeignClient;
import com.nordfors.microservicios.app.respuestas.models.entity.Respuesta;
import com.nordfors.microservicios.app.respuestas.models.repository.RespuestaRepository;
import com.nordfors.microservicios.commons.examenes.models.entity.Examen;
import com.nordfors.microservicios.commons.examenes.models.entity.Pregunta;

@Service
public class RespuestaServiceImpl implements RespuestaService {

	@Autowired
	private RespuestaRepository repository;
	
	@Autowired
	private ExamenFeignClient examenClient;
	
	@Override
//	@Transactional SE QUITA PORQUE POR DEFECTO MONGODB CON SPRINGDATA NO ES TRANSACCIONAL
	public Iterable<Respuesta> saveAll(Iterable<Respuesta> respuestas) {
		return repository.saveAll(respuestas);
	}

	// Relacion distribuida
	@Override
	//@Transactional(readOnly = true) SE QUITA PORQUE POR DEFECTO MONGODB CON SPRINGDATA NO ES TRANSACCIONAL
	public Iterable<Respuesta> findRespuestaByAlumnoByExamen(Long alumnoId, Long examenId) {
		/*Examen examen = examenClient.obtenerExamenPorId(examenId);
		List<Pregunta> preguntas = examen.getPreguntas();
		List<Long> preguntaIds = preguntas.stream().map(p -> p.getId()).collect(Collectors.toList());
		List<Respuesta> respuestas = (List<Respuesta>) repository.findRespuestaByAlumnoByPreguntaIds(alumnoId, preguntaIds);
		respuestas = respuestas.stream().map(r -> {
			preguntas.forEach(p -> {
				if(p.getId() == r.getPreguntaId()) {
					r.setPregunta(p);
				}
			});
			return r;
		}).collect(Collectors.toList());*/
		
		List<Respuesta> respuestas = (List<Respuesta>) repository.findRespuestaByAlumnoByExamen(alumnoId, examenId);		
		return respuestas;
	}

	@Override
//	@Transactional(readOnly = true) SE QUITA PORQUE POR DEFECTO MONGODB CON SPRINGDATA NO ES TRANSACCIONAL
	public Iterable<Long> findExamenesIdsConRespuestasByAlumnos(Long alumnoId) {
		/*List<Respuesta> respuestasAlumno = (List<Respuesta>) repository.findByAlumnoId(alumnoId);
		List<Long> examenIds = Collections.emptyList();
		
		if(respuestasAlumno.size() > 0) {
			List<Long> preguntaIds = respuestasAlumno.stream().map(r -> r.getPreguntaId()).collect(Collectors.toList());
			examenIds = examenClient.obtenerExamenesIdsPorPreguntasIdRespondidas(preguntaIds);
		}*/
		
		List<Respuesta> respuestasAlumno = (List<Respuesta>) repository.findExamenesIdsConRespuestasByAlumno(alumnoId);
		List<Long> examenIds = respuestasAlumno
				.stream()
				.map(r -> r.getPregunta().getExamen().getId())
				// .distinct() evita que respita varios id de examen por cada respuesta de ese examen
				.distinct()
				.collect(Collectors.toList());
		
		return examenIds;
	}

	@Override
	public Iterable<Respuesta> findByAlumnoId(Long alumnoId) {
		return repository.findByAlumnoId(alumnoId);
	}

}
