package com.ivanmoreno.app.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivanmoreno.app.models.Cuenta;
import com.ivanmoreno.app.models.TransaccionDto;

@TestMethodOrder(OrderAnnotation.class)
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
	@Order(2)
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
	
	@Test
	@Order(1)
	void testDetalle() {
		
		client.get().uri("/api/cuentas/1")
		.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(MediaType.APPLICATION_JSON)
			.expectBody()
				.jsonPath("$.persona").isEqualTo("Ivan")
				.jsonPath("$.saldo").isEqualTo(200);
	}

	@Test
	@Order(3)
	void testDetalle2() {
		
		client.get().uri("/api/cuentas/2")
		.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(MediaType.APPLICATION_JSON)
			.expectBody(Cuenta.class)
			.consumeWith(response -> {
				Cuenta cuentaRespuesta = response.getResponseBody();
				assertEquals("Carlos", cuentaRespuesta.getPersona());
				assertEquals("60.00", cuentaRespuesta.getSaldo().toPlainString());
			});
	}
	
	@Test
	@Order(4)
	void testListar() {
		
		client.get().uri("/api/cuentas")
		.exchange()
			.expectStatus().isOk()
			.expectBody()
				.jsonPath("$[0].persona").isEqualTo("Ivan")
				.jsonPath("$[0].saldo").isEqualTo(150)
				.jsonPath("$[1].persona").isEqualTo("Carlos")
				.jsonPath("$[1].saldo").isEqualTo(60)
				.jsonPath("$", Matchers.hasSize(2));
				
				
	}
	
	@Test
	@Order(5)
	void testLista2() {
		
		client.get().uri("/api/cuentas")
		.exchange()
			.expectStatus().isOk()
			.expectBodyList(Cuenta.class)
			.consumeWith(response -> {
				List<Cuenta> cuentas = response.getResponseBody();
				
				assertNotNull(cuentas);
				assertEquals(2, cuentas.size());
				assertEquals(1L, cuentas.get(0).getId());
				assertEquals("Ivan", cuentas.get(0).getPersona());
				assertEquals("150.0", cuentas.get(0).getSaldo().toPlainString());
				assertEquals(2L, cuentas.get(1).getId());
				assertEquals("Carlos", cuentas.get(1).getPersona());
				assertEquals("60.0", cuentas.get(1).getSaldo().toPlainString());
			});
	}
	
	@Test
	@Order(6)
	void testGuardar() {
		
		Cuenta cuenta = new Cuenta(null, "Pedro", new BigDecimal("3000"));
		
		client.post().uri("/api/cuentas")
		.contentType(MediaType.APPLICATION_JSON)
		.bodyValue(cuenta)
		.exchange()
			.expectStatus().isCreated()
			.expectBody()
				.jsonPath("$.id").isEqualTo(3)
				.jsonPath("$.persona").isEqualTo("Pedro")
				.jsonPath("$.saldo").isEqualTo(3000);
	}
	
	@Test
	@Order(7)
	void testGuardar2() {
		
		Cuenta cuenta = new Cuenta(null, "Pedra", new BigDecimal("4000"));
		
		client.post().uri("/api/cuentas")
		.contentType(MediaType.APPLICATION_JSON)
		.bodyValue(cuenta)
		.exchange()
			.expectStatus().isCreated()
			.expectHeader().contentType(MediaType.APPLICATION_JSON)
			.expectBody(Cuenta.class)
			.consumeWith(response -> {
				Cuenta cuentaRespuesta = response.getResponseBody();
				assertNotNull(cuentaRespuesta);
				assertEquals(4L, cuentaRespuesta.getId());
				assertEquals("Pedra", cuentaRespuesta.getPersona());
				assertEquals("4000", cuentaRespuesta.getSaldo().toPlainString());
			});
	}
	
	@Test
	@Order(8)
	void testDelete() {
		
		client.get().uri("/api/cuentas")
		.exchange()
			.expectStatus().isOk()
			.expectBodyList(Cuenta.class)
			.hasSize(4);
		
		client.delete().uri("/api/cuentas/3")
		.exchange()
			.expectStatus().isNoContent()
			.expectBody().isEmpty();
		
		client.get().uri("/api/cuentas")
		.exchange()
			.expectStatus().isOk()
			.expectBodyList(Cuenta.class)
			.hasSize(3);
		
		client.get().uri("/api/cuentas/3")
		.exchange()
//			.expectStatus().is5xxServerError();
			.expectStatus().isNotFound()
			.expectBody()
				.isEmpty();
	}
	
	
}
