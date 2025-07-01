package com.gestorcoi.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table
public class GestorEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String turno;
	private Date date;
	
	@Column(columnDefinition = "TEXT")
	private String pendencias;
	
	@ManyToOne
	@JoinColumn(name = "supervisorEntrando_id")
	private Supervisor supervisorEntrando;
	
	@ManyToOne
	@JoinColumn(name = "supervisorSaindo_id")
	private Supervisor supervisorSaindo;
	
	@OneToMany(mappedBy = "gestorEntity", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Ocorrencia> ocorrencias = new ArrayList<>();
	
	public Long getId() {
		return id;
	}
	
	public List<Ocorrencia> getOcorrencias() {
		return ocorrencias;
	}
	
	public void setOcorrencias(List<Ocorrencia> ocorrencias) {
		this.ocorrencias = ocorrencias;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public String getTurno() {
		return turno;
	}

	public void setTurno(String turno) {
		this.turno = turno;
	}
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date dateTime) throws Exception{
		this.date = dateTime;
	}

	public String getPendencias() {
		return pendencias;
	}

	public void setPendencias(String pendencias) {
		this.pendencias = pendencias;
	}

	public Supervisor getSupervisorEntrando() {
		return supervisorEntrando;
	}

	public void setSupervisorEntrando(Supervisor supervisorEntrando) {
		this.supervisorEntrando = supervisorEntrando;
	}

	public Supervisor getSupervisorSaindo() {
		return supervisorSaindo;
	}

	public void setSupervisorSaindo(Supervisor supervisorSaindo) {
		this.supervisorSaindo = supervisorSaindo;
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
		GestorEntity other = (GestorEntity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
