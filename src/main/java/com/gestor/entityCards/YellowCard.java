package com.gestor.entityCards;

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
public class YellowCard {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String distribuidora;
	private String regional;
	private String codigoEquipamento;
	private Date dataInicio;
	private Date dataParcial;
	private String seTransf;
	private String numeroOcorrencia;
	
	private String superintendencia;
	private String se;
	private String alimentador;
	private Integer unidadesNormalizadas;
	private Integer unidadesInterrompidas;
	private String alimTransf;
	
	private String observacao;
	
	// Relacionamento entidades
	
	@ManyToOne
	@JoinColumn(name = "redCard_id")
	private RedCard redCard;
	
	public YellowCard() {

	}
	
	public YellowCard(String distribuidora, String regional, String codigoEquipamento, Date dataInicio,
			Date dataParcial, String seTransf, String numeroOcorrencia, String superintendencia, String se,
			String alimentador, Integer unidadesNormalizadas, Integer unidadesInterrompidas, String alimTransf,
			String observacao, RedCard redCard) {
		
		this.distribuidora = distribuidora;
		this.regional = regional;
		this.codigoEquipamento = codigoEquipamento;
		this.dataInicio = dataInicio;
		this.dataParcial = dataParcial;
		this.seTransf = seTransf;
		this.numeroOcorrencia = numeroOcorrencia;
		this.superintendencia = superintendencia;
		this.se = se;
		this.alimentador = alimentador;
		this.unidadesNormalizadas = unidadesNormalizadas;
		this.unidadesInterrompidas = unidadesInterrompidas;
		this.alimTransf = alimTransf;
		this.observacao = observacao;
		this.redCard = redCard;
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

	public Date getDataParcial() {
		return dataParcial;
	}

	public void setDataParcial(Date dataParcial) {
		this.dataParcial = dataParcial;
	}

	public String getSeTransf() {
		return seTransf;
	}

	public void setSeTransf(String seTransf) {
		this.seTransf = seTransf;
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

	public Integer getUnidadesNormalizadas() {
		return unidadesNormalizadas;
	}

	public void setUnidadesNormalizadas(Integer unidadesNormalizadas) {
		this.unidadesNormalizadas = unidadesNormalizadas;
	}

	public Integer getUnidadesInterrompidas() {
		return unidadesInterrompidas;
	}

	public void setUnidadesInterrompidas(Integer unidadesInterrompidas) {
		this.unidadesInterrompidas = unidadesInterrompidas;
	}

	public String getAlimTransf() {
		return alimTransf;
	}

	public void setAlimTransf(String alimTransf) {
		this.alimTransf = alimTransf;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public RedCard getRedCard() {
		return redCard;
	}

	public void setRedCard(RedCard redCard) {
		this.redCard = redCard;
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
		YellowCard other = (YellowCard) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
