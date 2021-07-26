package com.ivanmoreno.app.controllers;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivanmoreno.app.models.TransaccionDto;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class CuentaControllerWebClientTest {

	@Autowired
	private WebTestClient client;
	
	private ObjectMapper objectMapper;
	
	@BeforeEach
	void setUp() {
		objectMapper = new ObjectMapper();
	}
	
	@Test
	void testTransferir() throws JsonProcessingException {
		
		TransaccionDto dto = new TransaccionDto();
		dto.setCuentaOrigenId(1L);
		dto.setCuentaDestionoId(2L);
		dto.setMonto(new BigDecimal("50"));
		dto.setBancoId(1L);
		
		Map<String, Object> response = new HashMap<>();
		response.put("date", LocalDate.now().toString());
		response.put("status", "OK");
		response.put("mensaje", "Transferencia realiza con exito");
		response.put("transaccion", dto);
		
		client.post().uri("/api/cuentas/transferir")
		.contentType(MediaType.APPLICATION_JSON)
		.bodyValue(dto)
		.exchange()
			.expectStatus().isOk()
			.expectBody()
				.jsonPath("$.mensaje").isNotEmpty()
				.jsonPath("$.mensaje").value(Matchers.is("Transferencia realiza con exito"))
				.jsonPath("$.date").value(valor -> assertEquals(LocalDate.now().toString(), valor))
				.jsonPath("$.transaccion.cuentaOrigenId").isEqualTo(dto.getCuentaOrigenId())
				.json(objectMapper.writeValueAsString(response));
				
	}

}
