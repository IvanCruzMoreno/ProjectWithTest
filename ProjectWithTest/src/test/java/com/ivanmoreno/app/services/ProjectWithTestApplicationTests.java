package com.ivanmoreno.app.services;



import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.ivanmoreno.app.DatosTest;
import com.ivanmoreno.app.exceptions.DineroInsuficienteException;
import com.ivanmoreno.app.models.Banco;
import com.ivanmoreno.app.models.Cuenta;
import com.ivanmoreno.app.repositories.BancoRepository;
import com.ivanmoreno.app.repositories.CuentaRepository;


@SpringBootTest
class ProjectWithTestApplicationTests {

	@MockBean
	CuentaRepository cuentaRepository;
	
	@MockBean
	BancoRepository bancoRepository;
	
	@Autowired
	CuentaService cuentaService;
	
//	@BeforeEach
//	void setUp() {
//		cuentaRepository = mock(CuentaRepository.class);
//		bancoRepository = mock(BancoRepository.class);
//		
//		cuentaService = new CuentaServiceImpl(cuentaRepository, bancoRepository);
//	}
	
	@Test
	void contextLoads() {
		
		when(cuentaRepository.findById(1L)).thenReturn(DatosTest.crearCuenta001());
		when(cuentaRepository.findById(2L)).thenReturn(DatosTest.crearCuenta002());
		when(bancoRepository.findById(1L)).thenReturn(DatosTest.crearBanco());
		
		BigDecimal saldoOrigen = cuentaService.revisarSaldo(1L);
		BigDecimal saldoDestino = cuentaService.revisarSaldo(2L);
		
		assertEquals("200", saldoOrigen.toPlainString());
		assertEquals("10", saldoDestino.toPlainString());
		
		cuentaService.transferir(1L, 2L, new BigDecimal("100"), 1L);
		
		saldoOrigen = cuentaService.revisarSaldo(1L);
		saldoDestino = cuentaService.revisarSaldo(2L);
		
		assertEquals("100", saldoOrigen.toPlainString());
		assertEquals("110", saldoDestino.toPlainString());
		
		int totalTransferencias = cuentaService.revisarTotalTransferencias(1L);
		assertEquals(1, totalTransferencias);
		
		verify(cuentaRepository, times(3)).findById(1L);
		verify(cuentaRepository, times(3)).findById(2L);
		
		verify(cuentaRepository, times(2)).save(any(Cuenta.class));
		
		verify(bancoRepository, times(2)).findById(1L);
		verify(bancoRepository).save(any(Banco.class));
		
		verify(cuentaRepository, times(6)).findById(anyLong());
		verify(cuentaRepository, never()).findAll();
	}
	
	@Test
	void contextLoads2() {
		
		when(cuentaRepository.findById(1L)).thenReturn(DatosTest.crearCuenta001());
		when(cuentaRepository.findById(2L)).thenReturn(DatosTest.crearCuenta002());
		when(bancoRepository.findById(1L)).thenReturn(DatosTest.crearBanco());
		
		BigDecimal saldoOrigen = cuentaService.revisarSaldo(1L);
		BigDecimal saldoDestino = cuentaService.revisarSaldo(2L);
		
		assertEquals("200", saldoOrigen.toPlainString());
		assertEquals("10", saldoDestino.toPlainString());
		
		assertThrows(DineroInsuficienteException.class, () -> {
			cuentaService.transferir(1L, 2L, new BigDecimal("500"), 1L);
		});
		
		
		saldoOrigen = cuentaService.revisarSaldo(1L);
		saldoDestino = cuentaService.revisarSaldo(2L);
		
		assertEquals("200", saldoOrigen.toPlainString());
		assertEquals("10", saldoDestino.toPlainString());
		
		int totalTransferencias = cuentaService.revisarTotalTransferencias(1L);
		assertEquals(0, totalTransferencias);
		
		verify(cuentaRepository, times(3)).findById(1L);
		verify(cuentaRepository, times(2)).findById(2L);
		
		verify(cuentaRepository, never()).save(any(Cuenta.class));
		
		
		verify(bancoRepository, times(1)).findById(1L);
		verify(bancoRepository, never()).save(any(Banco.class));
		
		verify(cuentaRepository, times(5)).findById(anyLong());
		verify(cuentaRepository, never()).findAll();
	}
	
	@Test
	void contextLoads3() {
		when(cuentaRepository.findById(1L)).thenReturn(DatosTest.crearCuenta001());
		
		Cuenta cuenta1 = cuentaService.findById(1L);
		Cuenta cuenta2 = cuentaService.findById(1L);
		
		assertSame(cuenta1, cuenta2);
		assertTrue(cuenta1 == cuenta2);
	}

}
