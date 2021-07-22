package com.ivanmoreno.app.repositories;



import org.springframework.data.jpa.repository.JpaRepository;

import com.ivanmoreno.app.models.Banco;

public interface BancoRepository extends JpaRepository<Banco, Long>{

}
