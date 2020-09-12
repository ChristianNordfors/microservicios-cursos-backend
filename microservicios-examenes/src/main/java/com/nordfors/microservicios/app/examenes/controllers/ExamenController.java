package com.nordfors.microservicios.app.examenes.controllers;

import java.util.List;
//import java.util.ArrayList;
//import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//import com.nordfors.microservicios.app.examenes.models.entity.Pregunta;
import com.nordfors.microservicios.app.examenes.services.ExamenService;
import com.nordfors.microservicios.commons.controllers.CommonController;
import com.nordfors.microservicios.commons.examenes.models.entity.Examen;
import com.nordfors.microservicios.commons.examenes.models.entity.Pregunta;

@RestController
public class ExamenController extends CommonController<Examen, ExamenService>{
	
	// Esto se solicita desde microservicio respuestas, examenfeignclient
	@GetMapping("/respondidos-por-preguntas")
	public ResponseEntity<?> obtenerExamenesIdsPorPreguntasIdRespondidas(@RequestParam List<Long> preguntaIds){
		return ResponseEntity.ok().body(service.findExamenesIdsConRespuestasByPreguntaIds(preguntaIds));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> editar(@Valid @RequestBody Examen examen, BindingResult result, @PathVariable Long id){
		
		if(result.hasErrors()) {
			return this.validar(result);
		}
		
		Optional<Examen> o = service.findById(id);
		
		if(!o.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		
		Examen examenDb = o.get();
		examenDb.setNombre(examen.getNombre());
		
		//
		// Preguntar si las preguntas existen en el json que nos estan enviando y si no existen se eliminan
//		List<Pregunta> eliminadas = new ArrayList<>();
		
//		examenDb.getPreguntas().forEach(pdb -> {
			// contains usa equals en Pregunta
//			if(!examen.getPreguntas().contains(pdb)) {
//				eliminadas.add(pdb);
//			}
//		});

		
	/////////////////
	// Se separa el forEach del stream por posible error 500 al eliminar las preguntas vacias desde el front
	/////////////////
		
		List<Pregunta> eliminadas = examenDb.getPreguntas()
		.stream()
		.filter(pdb -> !examen.getPreguntas().contains(pdb))
		.collect(Collectors.toList());
		
		eliminadas.forEach(examenDb::removePregunta);
//		examenDb.getPreguntas()
//		.stream()
//		.filter(pdb -> !examen.getPreguntas().contains(pdb))
//		.forEach(examenDb::removePregunta);

	/////////////////
	// Hasta aca
	/////////////////
	
		
		
//		eliminadas.forEach(examenDb::removePregunta);
//		eliminadas.forEach(p -> {
			// Elimina con metodo de Examen removePregunta() + orphanRemoval
//			examenDb.removePregunta(p);
//		});
		
		// Se guardan las preguntas dejando las que no fueron eliminadas junto con las nuevas
		examenDb.setPreguntas(examen.getPreguntas());
		// Probar sin estas dos que tambien funcionaba al parecer
		examenDb.setAsignaturaHija(examen.getAsignaturaHija());
		examenDb.setAsignaturaPadre(examen.getAsignaturaPadre());
		
		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(examenDb));
	}
	
	@GetMapping("/filtrar/{term}")
	public ResponseEntity<?> filtrar(@PathVariable String term){
		return ResponseEntity.ok(service.findByNombre(term));
	}
	
	@GetMapping("/asignaturas")
	public ResponseEntity<?> listarAsignaturas(){
		return ResponseEntity.ok(service.findAllAsignaturas());
	}
}
