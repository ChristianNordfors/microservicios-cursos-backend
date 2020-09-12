package com.nordfors.microservicios.commons.services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

//import com.nordfors.microservicios.app.usuarios.models.entity.Alumno;

public interface CommonService<E> {
	
	public Page<E> findAll(Pageable pageable); 
	
	public Iterable<E> findAll();
	
	public Optional<E> findById(Long id);
	
	public E save(E entity);
	
	public void deleteById(Long id);

}
