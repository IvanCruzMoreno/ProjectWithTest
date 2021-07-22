package com.ivanmoreno.app.repositories;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.ivanmoreno.app.models.Cuenta;

@DataJpaTest
public class IntegracionJpaTest {

	@Autowired
	CuentaRepository cuentaRepository;
	
	@Test
	void testFindById() {
		Optional<Cuenta> cuenta = cuentaRepository.findById(1L);
		
		assertTrue(cuenta.isPresent());
		assertEquals("Ivan", cuenta.get().getPersona());
	}
	
	@Test
	void testFindByPersona() {
		Optional<Cuenta> cuenta = cuentaRepository.findByPersona("Ivan");
		
		assertTrue(cuenta.isPresent());
		assertEquals("Ivan", cuenta.get().getPersona());
		assertEquals("200.00", cuenta.get().getSaldo().toPlainString());
	}
	
	@Test
	void testFindByPersonaThrowException() {
		Optional<Cuenta> cuenta = cuentaRepository.findByPersona("DR X");
		
//		assertThrows(NoSuchElementException.class, () -> cuenta.orElseThrow());
		assertThrows(NoSuchElementException.class, cuenta::orElseThrow);
		assertFalse(cuenta.isPresent());
	}
	
	@Test
	void testFindAll() {
		List<Cuenta> cuentas = cuentaRepository.findAll();
		
		assertFalse(cuentas.isEmpty());
		assertEquals(2, cuentas.size());
		
	}
	
}
