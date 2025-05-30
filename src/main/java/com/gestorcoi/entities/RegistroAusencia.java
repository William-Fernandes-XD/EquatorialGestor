package com.gestorcoi.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table
public class RegistroAusencia {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private Date data_ausencia;
	private String turno;
	private String justificativa;
	
	@ManyToOne
	@JoinColumn(name = "funcionario_id")
	private Funcionarios funcionarios;
	
	public RegistroAusencia() {

	}

	public RegistroAusencia(Date data_ausencia, String turno, String justificativa, Funcionarios funcionarios) {
		this.data_ausencia = data_ausencia;
		this.turno = turno;
		this.justificativa = justificativa;
		this.funcionarios = funcionarios;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getData_ausencia() {
		return data_ausencia;
	}

	public void setData_ausencia(Date data_ausencia) {
		this.data_ausencia = data_ausencia;
	}

	public String getTurno() {
		return turno;
	}

	public void setTurno(String turno) {
		this.turno = turno;
	}

	public String getJustificativa() {
		return justificativa;
	}

	public void setJustificativa(String justificativa) {
		this.justificativa = justificativa;
	}

	public Funcionarios getFuncionarios() {
		return funcionarios;
	}

	public void setFuncionarios(Funcionarios funcionarios) {
		this.funcionarios = funcionarios;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RegistroAusencia other = (RegistroAusencia) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "RegistroAusencia [id=" + id + ", data_ausencia=" + data_ausencia + ", turno=" + turno
				+ ", justificativa=" + justificativa + ", funcionarios=" + funcionarios + "]";
	}
}
