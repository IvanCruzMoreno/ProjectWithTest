package com.ivanmoreno.app.repositories;

import java.util.List;

import com.ivanmoreno.app.models.Banco;

public interface BancoRepository {

	List<Banco> findAll();
	
	Banco findById(Long id);
	
	void update(Banco banco);
}
