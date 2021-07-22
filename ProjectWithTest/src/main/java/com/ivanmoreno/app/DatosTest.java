package com.ivanmoreno.app;

import java.math.BigDecimal;
import java.util.Optional;

import com.ivanmoreno.app.models.Banco;
import com.ivanmoreno.app.models.Cuenta;

public class DatosTest {

	public static Optional<Cuenta> crearCuenta001() {
		return Optional.of(new Cuenta(1L, "Ivan", new BigDecimal("200")));
	}
	
	public static Optional<Cuenta>  crearCuenta002() {
		return  Optional.of(new Cuenta(2L, "Carlos", new BigDecimal("10")));
	}
	
	public static Optional<Banco> crearBanco() {
		return Optional.of(new Banco(1L, "Capital Bank", 0));
	}
}
