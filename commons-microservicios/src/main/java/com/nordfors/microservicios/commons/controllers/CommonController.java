package com.nordfors.microservicios.commons.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nordfors.microservicios.commons.services.CommonService;

//import com.nordfors.microservicios.app.usuarios.models.entity.Alumno;
//import com.nordfors.microservicios.app.usuarios.services.AlumnoService;

// @RestController esta anotado con @ResponseBody para guardar la respuesta como contenido en el body, el json
//@RestController()

//@CrossOrigin({"http://localhost:4200"})
public class CommonController<E, S extends CommonService<E>> {
	
	@Autowired
	protected S service;
	
	// Al no colocar ruta se indica que esta mapeado a la ruta raiz del proyecto
	@GetMapping
	// ResponseEntity permite contruir la respuesta, el httpentity pasando objetos al body del request
	public ResponseEntity<?> listar() {
		
		return ResponseEntity.ok().body(service.findAll());
		// Se pasa al cuerpo de la repuesta una lista de entity del tipo iterable y por debajo se construye el json
	}
	
	// Handler paginador
	@GetMapping("/pagina")
	public ResponseEntity<?> listar(Pageable pageable) {
		return ResponseEntity.ok().body(service.findAll(pageable));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> ver(@PathVariable Long id) {
		Optional<E> o = service.findById(id);
		
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
	public ResponseEntity<?> crear(@Valid @RequestBody E entity, BindingResult result){
		
		if(result.hasErrors()) {
			return this.validar(result);
		}
		E entityDb = service.save(entity);
		return ResponseEntity.status(HttpStatus.CREATED).body(entityDb);
	}
	
	/* Por los getters y setters el put tiene que estar en su propio controlador y no uno generico
	 * 
	@PutMapping("/{id}")
	public ResponseEntity<?> editar(@RequestBody Alumno alumno, @PathVariable Long id){
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
	*/
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> eliminar(@PathVariable Long id){
		service.deleteById(id);
		return ResponseEntity.noContent().build();
	}
	
	
	
	protected ResponseEntity<?> validar(BindingResult result){
		Map<String, Object> errores = new HashMap<>();
		result.getFieldErrors().forEach(err -> {
			errores.put(err.getField(), "El campo "+ err.getField()+ " " + err.getDefaultMessage());
		});
		return ResponseEntity.badRequest().body(errores);
	}

}
