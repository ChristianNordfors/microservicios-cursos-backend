package com.nordfors.microservicios.app.cursos.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nordfors.microservicios.app.cursos.models.entity.Curso;
import com.nordfors.microservicios.app.cursos.models.entity.CursoAlumno;
import com.nordfors.microservicios.app.cursos.services.CursoService;
import com.nordfors.microservicios.commons.alumnos.models.entity.Alumno;
import com.nordfors.microservicios.commons.controllers.CommonController;
import com.nordfors.microservicios.commons.examenes.models.entity.Examen;

@RestController
public class CursoController extends CommonController<Curso, CursoService>{
	
	// Se va a inyectar en este atributo el valor de la variable de entorno y en caso de no encontrarlo el valor por defecto : ...
	@Value("${config.balanceador.test}")
	private String balanceadorTest;
	
	
	@DeleteMapping("/eliminar-alumno/{id}")
	public ResponseEntity<?> eliminarCursoAlumnoPorId(@PathVariable Long id) {
		service.eliminarCursoAlumnoPorId(id);
		return ResponseEntity.noContent().build();
	}
	
	
	@GetMapping
	@Override
	public ResponseEntity<?> listar() {
		List<Curso> cursos= ((List<Curso>) service.findAll()).stream().map(c -> {
			c.getCursoAlumnos().forEach(ca -> {
				Alumno alumno = new Alumno();
				alumno.setId(ca.getAlumnoId());
				c.addAlumno(alumno);
			});
			return c;
		}).collect(Collectors.toList());
		
		return ResponseEntity.ok().body(cursos);
	}
	
	@Override
	@GetMapping("/pagina")
	public ResponseEntity<?> listar(Pageable pageable) {
		// Page ya contiene stream()
		Page<Curso> cursos = service.findAll(pageable).map(curso ->{
			curso.getCursoAlumnos().forEach(ca -> {
				Alumno alumno = new Alumno();
				alumno.setId(ca.getAlumnoId());
				curso.addAlumno(alumno);
			});
			return curso;
		});
		return ResponseEntity.ok().body(cursos);
	}
	
	@Override
	@GetMapping("/{id}")
	public ResponseEntity<?> ver(@PathVariable Long id) {
		Optional<Curso> o = service.findById(id);
		
		if(o.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		Curso curso = o.get();
		if(curso.getCursoAlumnos().isEmpty() == false) {
			// La relación ahora es distribuida al estar en distintas bd y hay que ir a buscarlo mediante apirest, con el cliente http feign
			
			// Convierte la lista de tipo CursoAlumno a un tipo Long -- curso.getCursoAlumnos()  ->   ca.getAlumnoId() 
			List<Long> ids = curso.getCursoAlumnos().stream().map(ca -> ca.getAlumnoId()).collect(Collectors.toList());
			
			List<Alumno> alumnos = (List<Alumno>) service.obtenerAlumnosPorCurso(ids);
			
			curso.setAlumnos(alumnos);
			
		}
		return ResponseEntity.ok().body(curso);
	}
	
	
	
	@GetMapping("/balanceador-test")
	public ResponseEntity<?> balanceadorTest(){
		// Generar JSON con varios valores personalizados con Map
		Map<String, Object> response = new HashMap<>();
		response.put("balanceador", balanceadorTest);
		response.put("cursos", service.findAll());
		
		// Va a generar un json con dos atributos. por ej, dentro del atributo cursos anida el json de los cursos
		return ResponseEntity.ok(response);
	}
	
	
	
	
	@PutMapping("/{id}")
	public ResponseEntity<?> editar(@Valid @RequestBody Curso curso, BindingResult result, @PathVariable Long id) {
		
		if(result.hasErrors()) {
			return this.validar(result);
		}
		
		Optional<Curso> o = this.service.findById(id);
		if(!o.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		Curso dbCurso = o.get();
		dbCurso.setNombre(curso.getNombre());
		return ResponseEntity.status(HttpStatus.CREATED).body(this.service.save(dbCurso));
	}
	
	@PutMapping("/{id}/asignar-alumnos")
	public ResponseEntity<?> asignarAlumnos(@RequestBody List<Alumno> alumnos, @PathVariable Long id){
		Optional<Curso> o = this.service.findById(id);
		if(!o.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		Curso dbCurso = o.get();
		
		alumnos.forEach(a -> {
			
			// Por cada alumno que se recibe del frontend se obtiene el id del alumno y se le pasa a cursoAlumno
			CursoAlumno cursoAlumno = new CursoAlumno();
			cursoAlumno.setAlumnoId(a.getId());
			cursoAlumno.setCurso(dbCurso);
			dbCurso.addCursoAlumno(cursoAlumno);
		});
		return ResponseEntity.status(HttpStatus.CREATED).body(this.service.save(dbCurso));
	}
	
	@PutMapping("/{id}/eliminar-alumno")
	public ResponseEntity<?> eliminarAlumnos(@RequestBody Alumno alumno, @PathVariable Long id){
		Optional<Curso> o = this.service.findById(id);
		if(!o.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		Curso dbCurso = o.get();
		
		CursoAlumno cursoAlumno = new CursoAlumno();
		cursoAlumno.setAlumnoId(alumno.getId());
		dbCurso.removeCursoAlumno(cursoAlumno);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(this.service.save(dbCurso));
	}
	
	@GetMapping("/alumno/{id}")
	public ResponseEntity<?> buscarPorAlumnoId(@PathVariable Long id){
		Curso curso = service.findCursoByAlumnoId(id);
		
		// Comunicacion con el microservicio respuestas para obtener la lista de examanes id respondidos por el alumno
		if( curso != null) {
			// Al castearlo a List se cuenta con el metodo contains
			List<Long> examenesIds = (List<Long>) service.obtenerExamenesIdsConRespuestasAlumnos(id);
			
			// map permite modificar el flujo de los examenes creando un nuevo stream asignandolo a una nueva variable ya que sea crea una "copia" y el formato original no se modifica
			// dentro del operador map se obtiene y se emite cada elemento cambiado
			if(examenesIds != null && examenesIds.size() > 0) {
			List<Examen> examenes = curso.getExamenes().stream().map(examen ->{
				// NOTA: Buscar en una sola consulta el listado de elemento y manipularlo con stream de java para mejor performance(por ej evitar timeout)
				
				if(examenesIds.contains(examen.getId())) {
					examen.setRespondido(true);
				}
				// map retorna el objeto modificado
				return examen;
				// Se vuleve a convertir a List
			}).collect(Collectors.toList());
			
			// Como el map no modifica la lista original seguiria siendo curso.getExamanes() pero aca se le setea la nueva lista
			// Ahora tienen el "estado" de respondido para manejarlo en el front
			curso.setExamenes(examenes);
			}
		}
		return ResponseEntity.ok(curso);
	}
	
	
	
	@PutMapping("/{id}/asignar-examenes")
	public ResponseEntity<?> asignarExamenes(@RequestBody List<Examen> examenes, @PathVariable Long id){
		Optional<Curso> o = this.service.findById(id);
		if(!o.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		Curso dbCurso = o.get();
		
		examenes.forEach(e -> {
			dbCurso.addExamen(e);
		});
		return ResponseEntity.status(HttpStatus.CREATED).body(this.service.save(dbCurso));
	}
	
	@PutMapping("/{id}/eliminar-examen")
	public ResponseEntity<?> eliminarExamen(@RequestBody Examen examen, @PathVariable Long id){
		Optional<Curso> o = this.service.findById(id);
		if(!o.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		Curso dbCurso = o.get();
		
		dbCurso.removeExamen(examen);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(this.service.save(dbCurso));
	}

}
