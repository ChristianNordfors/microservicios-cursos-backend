package com.nordfors.microservicios.app.usuarios.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

//import java.util.Optional;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Transactional;

import com.nordfors.microservicios.app.usuarios.client.CursoFeignClient;
import com.nordfors.microservicios.app.usuarios.models.repository.AlumnoRepository;
import com.nordfors.microservicios.commons.alumnos.models.entity.Alumno;
import com.nordfors.microservicios.commons.services.CommonServiceImpl;

@Service
public class AlumnoServiceImpl extends CommonServiceImpl<Alumno, AlumnoRepository> implements AlumnoService {

	@Autowired
	private CursoFeignClient clientCurso;
	
	@Override
	@Transactional(readOnly = true)
	public List<Alumno> findByNombreOrApellido(String term) {
		return repository.findByNombreOrApellido(term);
	}

	@Override
	@Transactional(readOnly = true)
	public Iterable<Alumno> findAllById(Iterable<Long> ids) {
		return repository.findAllById(ids);
	}

	@Override
	public void eliminarCursoAlumnoPorId(Long id) {
		clientCurso.eliminarCursoAlumnoPorId(id);
	}

	// La integridad de los datos es segura ya que si no se puede comunicar no eliminar por id, al estar dentro de la mismoa transaction
	// Si se cumple todo el proceso realiza el commit
	@Override
	@Transactional
	public void deleteById(Long id) {
		super.deleteById(id);
		// Despues de eliminar por id elimina en CursoAlumno mediante apirest usando feign, invocando el metodo:
		this.eliminarCursoAlumnoPorId(id);
	}

	
	// Para evitar que con postgres los editados vayan al ultimo lugar
	
	@Override
	@Transactional(readOnly = true)
	public Iterable<Alumno> findAll() {
		return repository.findAllByOrderByIdDesc();
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Alumno> findAll(Pageable pageable) {
		return repository.findAllByOrderByIdDesc(pageable);
	}
	
	
	
	
	

// ya esta todo en la clase generica	
	
//	@Autowired
//	private AlumnoRepository repository;

//	@Override
//	@Transactional(readOnly = true)
//	public Iterable<Alumno> findAll() {
//		return repository.findAll();
//	}
//
//	@Override
//	@Transactional(readOnly = true)
//	public Optional<Alumno> findById(Long id) {
//		return repository.findById(id);
//	}
//
//	@Override
//	@Transactional
//	public Alumno save(Alumno alumno) {
//		return repository.save(alumno);
//	}
//
//	@Override
//	@Transactional
//	public void deleteById(Long id) {
//		repository.deleteById(id);
//	}

}
