package com.nordfors.microservicios.app.cursos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@EnableEurekaClient
@SpringBootApplication
@EntityScan({"com.nordfors.microservicios.commons.examenes.models.entity",
			 "com.nordfors.microservicios.app.cursos.models.entity"})
//"com.nordfors.microservicios.commons.alumnos.models.entity", YA NO ES NECESARIO PORQUE ES TRANSIENT
public class MicroserviciosCursosApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviciosCursosApplication.class, args);
	}

}
