package com.gestorcoi.entities.configGeraEscala;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class GeradorEscalaEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private Date inicio;
	private Date fim;
	private String emergencialFolga;
	private String comercialFolga;
	private String ilhaDeRiscoFolga;
	private String ilhaDeRiscoFolga2;
	private String ilhaDeRiscoFolga3;
	private String triagemFolga;
	private String PtpFolga;
	private String PtpFolga2;
	private String PtpFolga3;
	private String PtpFolga4x4;
	private String avaliacaoFolga;
	private String avaliacaoFolga2;
	private String avaliacaoFolga3;
	private String impactoFolga;
	private String impactoFolga2;
	private String impactoFolga3;
	private String impactoFolga4;
	private String impactoFolga5;
	private String impactoFolga6;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getInicio() {
		return inicio;
	}
	public void setInicio(Date inicio) {
		this.inicio = inicio;
	}
	public Date getFim() {
		return fim;
	}
	public void setFim(Date fim) {
		this.fim = fim;
	}
	public String getEmergencialFolga() {
		return emergencialFolga;
	}
	public void setEmergencialFolga(String emergencialFolga) {
		this.emergencialFolga = emergencialFolga;
	}
	public String getComercialFolga() {
		return comercialFolga;
	}
	public void setComercialFolga(String comercialFolga) {
		this.comercialFolga = comercialFolga;
	}
	public String getIlhaDeRiscoFolga() {
		return ilhaDeRiscoFolga;
	}
	public void setIlhaDeRiscoFolga(String ilhaDeRiscoFolga) {
		this.ilhaDeRiscoFolga = ilhaDeRiscoFolga;
	}
	public String getTriagemFolga() {
		return triagemFolga;
	}
	public void setTriagemFolga(String triagemFolga) {
		this.triagemFolga = triagemFolga;
	}
	public String getPtpFolga() {
		return PtpFolga;
	}
	public void setPtpFolga(String ptpFolga) {
		PtpFolga = ptpFolga;
	}
	public String getPtpFolga4x4() {
		return PtpFolga4x4;
	}
	public void setPtpFolga4x4(String ptpFolga4x4) {
		PtpFolga4x4 = ptpFolga4x4;
	}
	public String getAvaliacaoFolga() {
		return avaliacaoFolga;
	}
	public void setAvaliacaoFolga(String avaliacaoFolga) {
		this.avaliacaoFolga = avaliacaoFolga;
	}
	public String getImpactoFolga() {
		return impactoFolga;
	}
	public void setImpactoFolga(String impactoFolga) {
		this.impactoFolga = impactoFolga;
	}
	public String getImpactoFolga2() {
		return impactoFolga2;
	}
	public void setImpactoFolga2(String impactoFolga2) {
		this.impactoFolga2 = impactoFolga2;
	}
	public String getImpactoFolga3() {
		return impactoFolga3;
	}
	public void setImpactoFolga3(String impactoFolga3) {
		this.impactoFolga3 = impactoFolga3;
	}
	public String getImpactoFolga4() {
		return impactoFolga4;
	}
	public void setImpactoFolga4(String impactoFolga4) {
		this.impactoFolga4 = impactoFolga4;
	}
	public String getImpactoFolga5() {
		return impactoFolga5;
	}
	public void setImpactoFolga5(String impactoFolga5) {
		this.impactoFolga5 = impactoFolga5;
	}
	public String getImpactoFolga6() {
		return impactoFolga6;
	}
	public void setImpactoFolga6(String impactoFolga6) {
		this.impactoFolga6 = impactoFolga6;
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
		GeradorEscalaEntity other = (GeradorEscalaEntity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	public String getIlhaDeRiscoFolga3() {
		return ilhaDeRiscoFolga3;
	}
	public void setIlhaDeRiscoFolga3(String ilhaDeRiscoFolga3) {
		this.ilhaDeRiscoFolga3 = ilhaDeRiscoFolga3;
	}
	public String getIlhaDeRiscoFolga2() {
		return ilhaDeRiscoFolga2;
	}
	public void setIlhaDeRiscoFolga2(String ilhaDeRiscoFolga2) {
		this.ilhaDeRiscoFolga2 = ilhaDeRiscoFolga2;
	}
	public String getPtpFolga2() {
		return PtpFolga2;
	}
	public void setPtpFolga2(String ptpFolga2) {
		PtpFolga2 = ptpFolga2;
	}
	public String getPtpFolga3() {
		return PtpFolga3;
	}
	public void setPtpFolga3(String ptpFolga3) {
		PtpFolga3 = ptpFolga3;
	}
	public String getAvaliacaoFolga2() {
		return avaliacaoFolga2;
	}
	public void setAvaliacaoFolga2(String avaliacaoFolga2) {
		this.avaliacaoFolga2 = avaliacaoFolga2;
	}
	public String getAvaliacaoFolga3() {
		return avaliacaoFolga3;
	}
	public void setAvaliacaoFolga3(String avaliacaoFolga3) {
		this.avaliacaoFolga3 = avaliacaoFolga3;
	}
}
