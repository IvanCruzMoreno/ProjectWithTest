package com.ivanmoreno.app.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import com.ivanmoreno.app.models.Cuenta;

public interface CuentaRepository extends JpaRepository<Cuenta, Long> {

}

