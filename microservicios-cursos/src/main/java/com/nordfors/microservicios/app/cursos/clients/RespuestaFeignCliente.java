package com.nordfors.microservicios.app.cursos.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="microservicio-respuestas")
public interface RespuestaFeignCliente {
	
	// Feign usa anotaciones como los controllers. Usa balanceo de cargas y busca la mejor instancia como zuul usando ribbon
	
	@GetMapping("/alumno/{alumnoId}/examenes-respondidos")
	public Iterable<Long> obtenerExamenesIdsConRespuestasAlumnos(@PathVariable Long alumnoId);

}
