package com.ivanmoreno.app.services;

import java.math.BigDecimal;

import com.ivanmoreno.app.models.Cuenta;

public interface CuentaService {

	Cuenta findById(Long id);
	
	int revisarTotalTransferencias(Long bancoId);
	
	BigDecimal revisarSaldo(Long cuentaId);
	
	void transferir(Long numCuentaOrigen, Long numCuentaDestino, BigDecimal monto, Long BancoId);
}
