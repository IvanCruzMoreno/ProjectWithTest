package com.ivanmoreno.app.controllers;

import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import static com.ivanmoreno.app.DatosTest.*;

import com.fasterxml.jackson.databind.ObjectMapper;
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

}
