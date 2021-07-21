package com.ivanmoreno.app;

import java.math.BigDecimal;

import com.ivanmoreno.app.models.Banco;
import com.ivanmoreno.app.models.Cuenta;

public class DatosTest {

	public static Cuenta crearCuenta001() {
		return new Cuenta(1L, "Ivan", new BigDecimal("200"));
	}
	
	public static Cuenta crearCuenta002() {
		return new Cuenta(2L, "Carlos", new BigDecimal("10"));
	}
	
	public static Banco crearBanco() {
		return new Banco(1L, "Capital Bank", 0);
	}
}
