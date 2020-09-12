package com.nordfors.microservicios.app.respuestas.models.repository;

//import org.springframework.data.jpa.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.nordfors.microservicios.app.respuestas.models.entity.Respuesta;

public interface RespuestaRepository extends MongoRepository<Respuesta, String> {
	
	@Query("{'alumnoId': ?0, 'preguntaId': { $in: ?1}}")
	public Iterable<Respuesta> findRespuestaByAlumnoByPreguntaIds(Long alumnoId, Iterable<Long> preguntaIds);
	
	@Query("{'alumnoId': ?0}")
	public Iterable<Respuesta> findByAlumnoId(Long alumnoId);
	
	
	
	// Con fetch se retorna
	
//	@Query("select r from Respuesta r join fetch r.pregunta p join fetch p.examen e where r.alumnoId=?1 and e.id=?2")
//	public Iterable<Respuesta> findRespuestaByAlumnoByExamen(Long alumnoId, Long examenId);
	
	// En este caso solo nos interesa el id de los examanes entonces el fetch no tiene que ir
	// Al agrupar con group by va a retornar un id del examen sino se retornaria varias veces repetido por cada respuesta, cada pregunta 
//	@Query("select e.id from Respuesta r join r.pregunta p join p.examen e where r.alumnoId=?1 group by e.id")
//	public Iterable<Long> findExamenesIdsConRespuestasByAlumnos(Long alumnoId);
	
	
	// Mongo
	
	@Query("{'alumnoId': ?0, 'pregunta.examen.id': ?1}")
	public Iterable<Respuesta> findRespuestaByAlumnoByExamen(Long alumnoId, Long examenId);

	// Con fields = etc ': 1' se indica que campos mostrar en el json 
	// etorna un iterable de respuesta, despues hay que cambiar el flujo de un iterable a un long con el id del examen
	@Query(value = "{'alumnoId': ?0}", fields = "{'pregunta.examen.id': 1}")
	public Iterable<Respuesta> findExamenesIdsConRespuestasByAlumno(Long alumnoId);
}
