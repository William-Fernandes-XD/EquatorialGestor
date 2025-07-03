package com.gestorcoi.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table
public class Feedback {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String feedback;
	private Date data;
	
	@ManyToOne
	@JoinColumn(name = "supervisor_id")
	private Supervisor avaliador;
	
	private String positivoOrNegative;
	
	@ManyToOne
	@JoinColumn(name = "funcionario_id")
	private Funcionarios funcionario;
	
	public Feedback() {

	}

	public Feedback(String feedback, Date data, Funcionarios funcionario) {
		this.feedback = feedback;
		this.data = data;
		this.funcionario = funcionario;
	}
	
	public Supervisor getAvaliador() {
		return avaliador;
	}
	
	public String getPositivoOrNegative() {
		return positivoOrNegative;
	}
	
	public void setPositivoOrNegative(String positivoOrNegative) {
		this.positivoOrNegative = positivoOrNegative;
	}
	
	public void setAvaliador(Supervisor avaliador) {
		this.avaliador = avaliador;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFeedback() {
		return feedback;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Funcionarios getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionarios funcionario) {
		this.funcionario = funcionario;
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
		Feedback other = (Feedback) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Feedback [id=" + id + ", feedback=" + feedback + ", data=" + data + ", funcionario=" + funcionario
				+ "]";
	}
}
