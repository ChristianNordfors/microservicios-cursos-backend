package com.nordfors.microservicios.app.usuarios.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nordfors.microservicios.app.usuarios.services.AlumnoService;
import com.nordfors.microservicios.commons.alumnos.models.entity.Alumno;
import com.nordfors.microservicios.commons.controllers.CommonController;

// @RestController esta anotado con @ResponseBody para guardar la respuesta como contenido en el body, el json
@RestController()
public class AlumnoController extends CommonController<Alumno, AlumnoService>{
	
	
	@GetMapping("/alumnos-por-curso")
	public ResponseEntity<?> obtenerAlumnosPorCurso(@RequestParam List<Long> ids){
		
		// findAllById() realiza la consulta "select where id in", en una sola consulta se pasa este conjunto de ids(Iterable-coleccion-) y va a ir a buscar a todos estos por el id
		// usando por debajo el where in - findAllById() metodo propio del repository
		return ResponseEntity.ok(service.findAllById(ids));
	}
	
	
	
	
	@GetMapping("/uploads/img/{id}")
	public ResponseEntity<?> verFoto(@PathVariable Long id){
		
		Optional<Alumno> o = service.findById(id);
		
		if(o.isEmpty() || o.get().getFoto() == null) {
			return ResponseEntity.notFound().build();
		}
		
		Resource imagen = new ByteArrayResource(o.get().getFoto());
		
		return ResponseEntity.ok()
				.contentType(MediaType.IMAGE_JPEG)
				.body(imagen);
	}

	
	@PutMapping("/{id}")
	public ResponseEntity<?> editar(@Valid @RequestBody Alumno alumno, BindingResult result, @PathVariable Long id){
		
		if(result.hasErrors()) {
			return this.validar(result);
		}
		
		Optional<Alumno> o = service.findById(id);
		
		if(o.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		
		Alumno alumnoDb = o.get();
		// Se indican los atributos que se quieran actualizar porque quizas no es necesario o no se quiera actualizar todos
		alumnoDb.setNombre(alumno.getNombre());
		alumnoDb.setApellido(alumno.getApellido());
		alumnoDb.setEmail(alumno.getEmail());
		// Se persiste
		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(alumnoDb));
	}
	
	@GetMapping("/filtrar/{term}")
	public ResponseEntity<?> filtrar(@PathVariable String term){
		return ResponseEntity.ok(service.findByNombreOrApellido(term));
	}
	
	@PostMapping("/crear-con-foto")
	public ResponseEntity<?> crearConFoto(@Valid Alumno alumno, BindingResult result, @RequestParam MultipartFile archivo) throws IOException{
		if(!archivo.isEmpty()) {
			// getBytes retorna el contenido de un archivo en un arreglo de bytes BLOB
			alumno.setFoto(archivo.getBytes());
		}
		return super.crear(alumno, result);
	}
	
	
	@PutMapping("/editar-con-foto/{id}")
	public ResponseEntity<?> editarConFoto(@Valid Alumno alumno, BindingResult result, @PathVariable Long id, 
			@RequestParam MultipartFile archivo) throws IOException{
		
		if(result.hasErrors()) {
			return this.validar(result);
		}
		
		Optional<Alumno> o = service.findById(id);
		
		if(o.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		
		Alumno alumnoDb = o.get();
		// Se indican los atributos que se quieran actualizar porque quizas no es necesario o no se quiera actualizar todos
		alumnoDb.setNombre(alumno.getNombre());
		alumnoDb.setApellido(alumno.getApellido());
		alumnoDb.setEmail(alumno.getEmail());
		
		if(!archivo.isEmpty()) {
			// getBytes retorna el contenido de un archivo en un arreglo de bytes BLOB
			alumnoDb.setFoto(archivo.getBytes());
		}
		
		// Se persiste
		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(alumnoDb));
	}
	
	
	
	
/*	
    //	@Autowired
	//	private AlumnoService service;
	//	
	
	// Al no colocar ruta se indica que esta mapeado a la ruta raiz del proyecto
	 * 
	@GetMapping
	// ResponseEntity permite contruir la respuesta, el httpentity pasando objetos al body del request
	public ResponseEntity<?> listar() {
		
		return ResponseEntity.ok().body(service.findAll());
		// Se pasa al cuerpo de la repuesta una lista de entity del tipo iterable y por debajo se construye el json
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> ver(@PathVariable Long id) {
		Optional<Alumno> o = service.findById(id);
		
		if(o.isEmpty()) {
//			build() construye la respuesta con un body vacio, si no se encuentra no tiene qué pasar
			return ResponseEntity.notFound().build();
		}
		
		// Con el get() retorna el tipo del objeto por o es optional
		return ResponseEntity.ok(o.get());
	}
	
	// Tambien esta en la raiz pero tipo post
	@PostMapping
	// Los atributos y el json que se va a enviar desde angular tienen que ser los mismos nombres por ej alumno
	public ResponseEntity<?> crear(@RequestBody Alumno alumno){
		Alumno alumnoDb = service.save(alumno);
		return ResponseEntity.status(HttpStatus.CREATED).body(alumnoDb);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> eliminar(@PathVariable Long id){
		service.deleteById(id);
		return ResponseEntity.noContent().build();
	}
	*/

}
