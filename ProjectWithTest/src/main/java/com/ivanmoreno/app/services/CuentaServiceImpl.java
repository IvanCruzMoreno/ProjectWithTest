package com.ivanmoreno.app.services;

import java.math.BigDecimal;

import com.ivanmoreno.app.models.Banco;
import com.ivanmoreno.app.models.Cuenta;
import com.ivanmoreno.app.repositories.BancoRepository;
import com.ivanmoreno.app.repositories.CuentaRepository;

public class CuentaServiceImpl implements CuentaService {

	private CuentaRepository cuentaRepo;
	private BancoRepository bancoRepo;
	
	public CuentaServiceImpl(CuentaRepository cuentaRepo, BancoRepository bancoRepo) {
		this.cuentaRepo = cuentaRepo;
		this.bancoRepo = bancoRepo;
	}
	
	@Override
	public Cuenta findById(Long id) {
		return cuentaRepo.findById(id);
	}

	@Override
	public int revisarTotalTransferencias(Long bancoId) {
		Banco banco = bancoRepo.findById(bancoId);
		return banco.getTotalTransferencias();
	}

	@Override
	public BigDecimal revisarSaldo(Long cuentaId) {
		Cuenta cuenta = cuentaRepo.findById(cuentaId);
		return cuenta.getSaldo();
	}

	@Override
	public void transferir(Long numCuentaOrigen, Long numCuentaDestino, BigDecimal monto, Long BancoId) {
	
		Cuenta cuentaOrigen = cuentaRepo.findById(numCuentaOrigen);
		cuentaOrigen.debito(monto);
		cuentaRepo.update(cuentaOrigen);
		
		Cuenta cuentaDestino = cuentaRepo.findById(numCuentaDestino);
		cuentaDestino.credito(monto);
		cuentaRepo.update(cuentaDestino);
		
		Banco banco = bancoRepo.findById(BancoId);
		int totalTransferencias = banco.getTotalTransferencias();
		banco.setTotalTransferencias(++totalTransferencias);
		
		bancoRepo.update(banco);
	}

}
