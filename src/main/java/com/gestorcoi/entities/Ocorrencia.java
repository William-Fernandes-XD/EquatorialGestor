package com.gestorcoi.entities;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 
 * Classe responsável pela tabela de ocorrências na gestão de turno
 * 
 * @author e21057649
 *
 */
@Entity
@Table(name = "ocorrencia")
public class Ocorrencia {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String ocorrencia;
	private String ponto_defeito;
	private Date date;
	private String recursos_especiais;
	private String observacoes;
	
	@ManyToOne
	@JoinColumn(name = "supervisor_id", nullable = false)
	private Supervisor supervisorEntrandoOcorrencia;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "gestorEntity_id")
	private GestorEntity gestorEntity;
	
	public Ocorrencia() {

	}

	public Long getId() {
		return id;
	}
	
	public GestorEntity getGestorEntity() {
		return gestorEntity;
	}
	public void setGestorEntity(GestorEntity gestorEntity) {
		this.gestorEntity = gestorEntity;
	}
	
	public Supervisor getSupervisorEntrandoOcorrencia() {
		return supervisorEntrandoOcorrencia;
	}
	
	public void setSupervisorEntrandoOcorrencia(Supervisor supervisorEntrandoOcorrencia) {
		this.supervisorEntrandoOcorrencia = supervisorEntrandoOcorrencia;
	}
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date dateTime) throws Exception{
		this.date = dateTime;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOcorrencia() {
		return ocorrencia;
	}

	public void setOcorrencia(String ocorrencia) {
		this.ocorrencia = ocorrencia;
	}

	public String getPonto_defeito() {
		return ponto_defeito;
	}

	public void setPonto_defeito(String pornto_defeito) {
		this.ponto_defeito = pornto_defeito;
	}
	
	public String getRecursos_especiais() {
		return recursos_especiais;
	}

	public void setRecursos_especiais(String recursos_especiais) {
		this.recursos_especiais = recursos_especiais;
	}

	public String getObservacoes() {
		return observacoes;
	}

	public void setObservacoes(String observacoes) {
		this.observacoes = observacoes;
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
		Ocorrencia other = (Ocorrencia) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	@Override
		public String toString() {
			return id + " observação: " + observacoes + 
					" Ocorrencia: " + ocorrencia + " Ponto defeito:" + ponto_defeito + " Recursos: " + recursos_especiais;
		}
}
