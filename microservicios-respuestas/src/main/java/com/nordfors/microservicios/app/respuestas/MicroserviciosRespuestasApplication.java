package com.nordfors.microservicios.app.respuestas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
//import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

// Deshabilitar springdatajpa porque lo obtiene a traves de las dependencias commons
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@EnableFeignClients
@EnableEurekaClient
@SpringBootApplication
// Ya no son entity. no se registran
//@EntityScan({"com.nordfors.microservicios.app.respuestas.models.entity",
//	
//	"com.nordfors.microservicios.commons.examenes.models.entity"})
//"com.nordfors.microservicios.commons.alumnos.models.entity", YA NO ES NECESARIO PORQUE ES TRANSIENT
public class MicroserviciosRespuestasApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviciosRespuestasApplication.class, args);
	}

}
