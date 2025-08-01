package com.gestorcoi.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table
public class Funcionarios implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String tipo;
	private String atividadeSuperintendencia;
	private String regional;
	
	private Date feriasInicio;
	private Date feriasFim;
	
	private Date trocaTurnoData;
	private String trocaTurno;
	
	private Date licencaInicio;
	private Date licencaFim;
	
	// Seções e escala de trabalho
	
	private String escala;
	private String secao;
	
	private String nome;
	
	@OneToMany(mappedBy = "funcionarios", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<RegistroAusencia> ausencias;
	
	@OneToMany(mappedBy = "funcionario", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Feedback> feedbacks;
	
	/**
	 * Banco de horas
	 */
	
	@OneToMany(mappedBy = "funcionario", orphanRemoval = true)
	private List<BancosTurno> bancosTurnos;
	
	public Funcionarios() {

	}

	public Funcionarios(String nome, String turno, Date date, String justificativa) {
		this.nome = nome;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Feedback> getFeedbacks() {
		return feedbacks;
	}
	
	public void setFeedbacks(List<Feedback> feedbacks) {
		this.feedbacks = feedbacks;
	}
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public List<RegistroAusencia> getAusencias() {
		return ausencias;
	}
	
	public String getEscala() {
		return escala;
	}
	
	public String getSecao() {
		return secao;
	}
	
	public void setEscala(String escala) {
		this.escala = escala;
	}
	
	public void setSecao(String secao) {
		this.secao = secao;
	}
	
	public void setAusencias(List<RegistroAusencia> ausencias) {
		this.ausencias = ausencias;
	}
	
	public String getAtividadeSuperintendencia() {
		return atividadeSuperintendencia;
	}
	public String getRegional() {
		return regional;
	}
	public String getTipo() {
		return tipo;
	}
	public void setAtividadeSuperintendencia(String atividadeSuperintendencia) {
		this.atividadeSuperintendencia = atividadeSuperintendencia;
	}
	public void setRegional(String regional) {
		this.regional = regional;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	public Date getFeriasFim() {
		return feriasFim;
	}
	
	public Date getFeriasInicio() {
		return feriasInicio;
	}
	
	public void setFeriasFim(Date feriasFim) {
		this.feriasFim = feriasFim;
	}
	
	public void setFeriasInicio(Date feriasInicio) {
		this.feriasInicio = feriasInicio;
	}

	public Date getLicencaFim() {
		return licencaFim;
	}
	
	public Date getLicencaInicio() {
		return licencaInicio;
	}
	
	public void setLicencaFim(Date licencaFim) {
		this.licencaFim = licencaFim;
	}
	
	public String getTrocaTurno() {
		return trocaTurno;
	}
	
	public Date getTrocaTurnoData() {
		return trocaTurnoData;
	}
	
	public void setTrocaTurno(String trocaTurno) {
		this.trocaTurno = trocaTurno;
	}
	
	public void setTrocaTurnoData(Date trocaTurnoData) {
		this.trocaTurnoData = trocaTurnoData;
	}
	
	public void setLicencaInicio(Date licencaInicio) {
		this.licencaInicio = licencaInicio;
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
		Funcionarios other = (Funcionarios) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
