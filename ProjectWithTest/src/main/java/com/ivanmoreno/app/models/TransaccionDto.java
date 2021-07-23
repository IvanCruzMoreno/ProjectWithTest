package com.ivanmoreno.app.models;

import java.math.BigDecimal;

public class TransaccionDto {

	private Long cuentaOrigenId;
	private Long cuentaDestionoId;
	private BigDecimal monto;
	private Long bancoId;
	
	public Long getCuentaOrigenId() {
		return cuentaOrigenId;
	}
	public void setCuentaOrigenId(Long cuentaOrigenId) {
		this.cuentaOrigenId = cuentaOrigenId;
	}
	public Long getCuentaDestionoId() {
		return cuentaDestionoId;
	}
	public void setCuentaDestionoId(Long cuentaDestionoId) {
		this.cuentaDestionoId = cuentaDestionoId;
	}
	public BigDecimal getMonto() {
		return monto;
	}
	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}
	public Long getBancoId() {
		return bancoId;
	}
	public void setBancoId(Long bancoId) {
		this.bancoId = bancoId;
	}
	
	
}
