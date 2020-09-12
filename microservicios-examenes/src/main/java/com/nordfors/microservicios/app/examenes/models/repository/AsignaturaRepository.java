package com.nordfors.microservicios.app.examenes.models.repository;

import org.springframework.data.repository.CrudRepository;

import com.nordfors.microservicios.commons.examenes.models.entity.Asignatura;

public interface AsignaturaRepository extends CrudRepository<Asignatura, Long>{

}
