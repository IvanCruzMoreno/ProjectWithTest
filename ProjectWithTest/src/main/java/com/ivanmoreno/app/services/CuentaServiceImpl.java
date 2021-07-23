package com.ivanmoreno.app.services;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ivanmoreno.app.models.Banco;
import com.ivanmoreno.app.models.Cuenta;
import com.ivanmoreno.app.repositories.BancoRepository;
import com.ivanmoreno.app.repositories.CuentaRepository;

@Service
public class CuentaServiceImpl implements CuentaService {

	private CuentaRepository cuentaRepo;
	private BancoRepository bancoRepo;
	
	public CuentaServiceImpl(CuentaRepository cuentaRepo, BancoRepository bancoRepo) {
		this.cuentaRepo = cuentaRepo;
		this.bancoRepo = bancoRepo;
	}
	
	@Override
	@Transactional(readOnly = true)
	public Cuenta findById(Long id) {
		return cuentaRepo.findById(id).orElseThrow();
	}

	@Override
	@Transactional(readOnly = true)
	public int revisarTotalTransferencias(Long bancoId) {
		Banco banco = bancoRepo.findById(bancoId).orElseThrow();
		return banco.getTotalTransferencias();
	}

	@Override
	@Transactional(readOnly = true)
	public BigDecimal revisarSaldo(Long cuentaId) {
		Cuenta cuenta = cuentaRepo.findById(cuentaId).orElseThrow();
		return cuenta.getSaldo();
	}

	@Override
	@Transactional
	public void transferir(Long numCuentaOrigen, Long numCuentaDestino, BigDecimal monto, Long BancoId) {
	
		Cuenta cuentaOrigen = cuentaRepo.findById(numCuentaOrigen).orElseThrow();
		cuentaOrigen.debito(monto);
		cuentaRepo.save(cuentaOrigen);
		
		Cuenta cuentaDestino = cuentaRepo.findById(numCuentaDestino).orElseThrow();
		cuentaDestino.credito(monto);
		cuentaRepo.save(cuentaDestino);
		
		Banco banco = bancoRepo.findById(BancoId).orElseThrow();
		int totalTransferencias = banco.getTotalTransferencias();
		banco.setTotalTransferencias(++totalTransferencias);
		
		bancoRepo.save(banco);
	}

	@Override
	public List<Cuenta> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cuenta save(Cuenta cuenta) {
		// TODO Auto-generated method stub
		return null;
	}

}
