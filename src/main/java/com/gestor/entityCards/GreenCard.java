package com.gestor.entityCards;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table
public class GreenCard {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String distribuidora;
	private String regional;
	private String codigoEquipamento;
	private Date dataInicio;
	private Date dataNormalizada;
	
	private String superintendencia;
	private String se;
	private String alimentador;
	private Integer unidadesNormalizadas;
	private String numeroOcorrencia;
	
	private String causa;
	
	@OneToOne(mappedBy = "greenCard")
	private RedCard redCard;
	
	public GreenCard() {

	}
	
	public GreenCard(String distribuidora, String regional, String codigoEquipamento, Date dataInicio,
			Date dataNormalizada, String superintendencia, String se, String alimentador, Integer unidadesNormalizadas,
			String numeroOcorrencia, String causa, RedCard redCard) {
		
		this.distribuidora = distribuidora;
		this.regional = regional;
		this.codigoEquipamento = codigoEquipamento;
		this.dataInicio = dataInicio;
		this.dataNormalizada = dataNormalizada;
		this.superintendencia = superintendencia;
		this.se = se;
		this.alimentador = alimentador;
		this.unidadesNormalizadas = unidadesNormalizadas;
		this.numeroOcorrencia = numeroOcorrencia;
		this.causa = causa;
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

	public Date getDataNormalizada() {
		return dataNormalizada;
	}

	public void setDataNormalizada(Date dataNormalizada) {
		this.dataNormalizada = dataNormalizada;
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

	public String getNumeroOcorrencia() {
		return numeroOcorrencia;
	}

	public void setNumeroOcorrencia(String numeroOcorrencia) {
		this.numeroOcorrencia = numeroOcorrencia;
	}

	public String getCausa() {
		return causa;
	}

	public void setCausa(String causa) {
		this.causa = causa;
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
		GreenCard other = (GreenCard) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
