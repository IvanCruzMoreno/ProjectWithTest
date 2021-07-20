package com.ivanmoreno.app.repositories;

import java.util.List;

import com.ivanmoreno.app.models.Cuenta;

public interface CuentaRepository {

	List<Cuenta> findAll();
	
	Cuenta findById(Long id);

	void update(Cuenta cuenta);
}

