package com.ivanmoreno.app.services;

import java.math.BigDecimal;
import java.util.List;

import com.ivanmoreno.app.models.Cuenta;

public interface CuentaService {

	Cuenta findById(Long id);
	
	int revisarTotalTransferencias(Long bancoId);
	
	BigDecimal revisarSaldo(Long cuentaId);
	
	void transferir(Long numCuentaOrigen, Long numCuentaDestino, BigDecimal monto, Long BancoId);
	
	List<Cuenta> findAll();
	
	Cuenta save(Cuenta cuenta);
	
	void deleteById(Long id);
}
