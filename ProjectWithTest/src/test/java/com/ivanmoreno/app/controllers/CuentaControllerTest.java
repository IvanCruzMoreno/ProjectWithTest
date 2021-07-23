package com.ivanmoreno.app.controllers;

import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import static com.ivanmoreno.app.DatosTest.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivanmoreno.app.models.Cuenta;
import com.ivanmoreno.app.models.TransaccionDto;
import com.ivanmoreno.app.services.CuentaService;

@WebMvcTest(CuentaController.class)
class CuentaControllerTest {

	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private CuentaService cuentaService;
	
	ObjectMapper objectMapper;
	
	@BeforeEach
	void setUp() {
		objectMapper = new ObjectMapper();
	}
	
	@Test
	void testDetalle() throws Exception {
		
		when(cuentaService.findById(1L)).thenReturn(crearCuenta001().orElseThrow());
		
		mvc.perform(get("/api/cuentas/1").contentType(MediaType.APPLICATION_JSON))
		
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.persona").value("Ivan"))
		.andExpect(jsonPath("$.saldo").value("200"));
	}
	
	@Test
	void testTransferir() throws Exception {
		
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
		
		
		mvc.perform(post("/api/cuentas/transferir")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)))
		
		
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.date").value(LocalDate.now().toString()))
		.andExpect(jsonPath("$.mensaje").value("Transferencia realiza con exito"))
		.andExpect(jsonPath("$.transaccion.cuentaOrigenId").value(1L))
		.andExpect(content().json(objectMapper.writeValueAsString(response)));
	}
	
	@Test
	void testListar() throws Exception {
		
		List<Cuenta> cuentas = Arrays.asList(crearCuenta001().orElseThrow(), crearCuenta002().orElseThrow());
		when(cuentaService.findAll()).thenReturn(cuentas);
		
		mvc.perform(get("/api/cuentas").contentType(MediaType.APPLICATION_JSON))
		
		.andExpect(status().isOk())
		.andExpect(jsonPath("$[0].persona").value("Ivan"))
		.andExpect(jsonPath("$[0].saldo").value("200"))
		.andExpect(jsonPath("$[1].persona").value("Carlos"))
		.andExpect(jsonPath("$[1].saldo").value("10"))
		.andExpect(jsonPath("$", Matchers.hasSize(2)))
		.andExpect(content().json(objectMapper.writeValueAsString(cuentas)));
	}
	
	@Test
	void testGuardar() throws Exception {
		
		Cuenta cuenta = new Cuenta(null, "Pedro", new BigDecimal("3000"));
		
		when(cuentaService.save(any())).then(invocation -> {
			Cuenta c = invocation.getArgument(0);
			c.setId(3L);
			return c;
		});
		
		mvc.perform(post("/api/cuentas")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(cuenta)))
		
		.andExpect(status().isCreated())
		.andExpect(jsonPath("$.persona").value("Pedro"))
		.andExpect(jsonPath("$.saldo").value("3000"))
		.andExpect(jsonPath("$.id").value("3"));
	}

}
