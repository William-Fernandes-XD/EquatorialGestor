package com.gestor.entityCards;


import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table
public class RedCard {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String distribuidora;
	private String regional;
	private String codigoEquipamento;
	private Date dataInicio;
	private Integer unidadeInterrompidas;
	private String turmaAcionada;
	private String numeroOcorrencia;
	
	private String superintendencia;
	private String se;
	private String alimentador;
	private Double tensaoKva;
	private String protecao;
	private String localDistancia;
	private String previsaoManobra;
	
	@Column(length = 10000)
	private String observacao;
	
	@Column(length = 10000)
	private String locaisAtingidos;
	
	// Relacionamentos de entidades
	
	@OneToMany(mappedBy = "redCard", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<YellowCard> yellowCards;
	
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "greencard_id")
	private GreenCard greenCard;
	
	public RedCard() {

	}
	
	public RedCard(String distribuidora, String regional, String codigoEquipamento, Date dataInicio,
			Integer unidadeInterrompidas, String turmaAcionada, String numeroOcorrencia, String superintendencia,
			String se, String alimentador, Double tensaoKva, String protecao, String localDistancia,
			String previsaoManobra, String observacao, String locaisAtingidos, List<YellowCard> yellowCards,
			GreenCard greenCard) {
		
		this.distribuidora = distribuidora;
		this.regional = regional;
		this.codigoEquipamento = codigoEquipamento;
		this.dataInicio = dataInicio;
		this.unidadeInterrompidas = unidadeInterrompidas;
		this.turmaAcionada = turmaAcionada;
		this.numeroOcorrencia = numeroOcorrencia;
		this.superintendencia = superintendencia;
		this.se = se;
		this.alimentador = alimentador;
		this.tensaoKva = tensaoKva;
		this.protecao = protecao;
		this.localDistancia = localDistancia;
		this.previsaoManobra = previsaoManobra;
		this.observacao = observacao;
		this.locaisAtingidos = locaisAtingidos;
		this.yellowCards = yellowCards;
		this.greenCard = greenCard;
	}



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDistribuidora() {
		return distribuidora;
	}

	public void setDistribuidora(String distribuidora) {
		this.distribuidora = distribuidora;
	}

	public String getRegional() {
		return regional;
	}

	public void setRegional(String regional) {
		this.regional = regional;
	}

	public String getCodigoEquipamento() {
		return codigoEquipamento;
	}

	public void setCodigoEquipamento(String codigoEquipamento) {
		this.codigoEquipamento = codigoEquipamento;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Integer getUnidadeInterrompidas() {
		return unidadeInterrompidas;
	}

	public void setUnidadeInterrompidas(Integer unidadeInterrompidas) {
		this.unidadeInterrompidas = unidadeInterrompidas;
	}

	public String getTurmaAcionada() {
		return turmaAcionada;
	}

	public void setTurmaAcionada(String turmaAcionada) {
		this.turmaAcionada = turmaAcionada;
	}

	public String getNumeroOcorrencia() {
		return numeroOcorrencia;
	}

	public void setNumeroOcorrencia(String numeroOcorrencia) {
		this.numeroOcorrencia = numeroOcorrencia;
	}

	public String getSuperintendencia() {
		return superintendencia;
	}

	public void setSuperintendencia(String superintendencia) {
		this.superintendencia = superintendencia;
	}

	public String getSe() {
		return se;
	}

	public void setSe(String se) {
		this.se = se;
	}

	public String getAlimentador() {
		return alimentador;
	}

	public void setAlimentador(String alimentador) {
		this.alimentador = alimentador;
	}

	public Double getTensaoKva() {
		return tensaoKva;
	}

	public void setTensaoKva(Double tensaoKva) {
		this.tensaoKva = tensaoKva;
	}

	public String getProtecao() {
		return protecao;
	}

	public void setProtecao(String protecao) {
		this.protecao = protecao;
	}

	public String getLocalDistancia() {
		return localDistancia;
	}

	public void setLocalDistancia(String localDistancia) {
		this.localDistancia = localDistancia;
	}

	public String getPrevisaoManobra() {
		return previsaoManobra;
	}

	public void setPrevisaoManobra(String previsaoManobra) {
		this.previsaoManobra = previsaoManobra;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public String getLocaisAtingidos() {
		return locaisAtingidos;
	}

	public void setLocaisAtingidos(String locaisAtingidos) {
		this.locaisAtingidos = locaisAtingidos;
	}

	public List<YellowCard> getYellowCards() {
		return yellowCards;
	}

	public void setYellowCards(List<YellowCard> yellowCards) {
		this.yellowCards = yellowCards;
	}

	public GreenCard getGreenCard() {
		return greenCard;
	}

	public void setGreenCard(GreenCard greenCard) {
		this.greenCard = greenCard;
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
		RedCard other = (RedCard) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
