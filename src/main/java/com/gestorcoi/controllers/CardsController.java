package com.gestorcoi.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import com.gestor.entityCards.GreenCard;
import com.gestor.entityCards.RedCard;
import com.gestor.entityCards.YellowCard;
import com.gestorcoi.implementations.GreenCardsImpl;
import com.gestorcoi.implementations.RedCardImpl;
import com.gestorcoi.implementations.YellowCardsImpl;
import com.gestorcoi.utils.MensagensJSF;

@ManagedBean(name = "cardsController")
@ViewScoped
public class CardsController {

	private RedCardImpl redCardImpl = new RedCardImpl();
	private YellowCardsImpl yellowCardsImpl = new YellowCardsImpl();
	private GreenCardsImpl greenCardsImpl = new GreenCardsImpl();
	
	private String filtrarRedCardPorOcorrencia = "";
	private Date filterRedCardPorData;
	
	private List<RedCard> redCardsAll = new ArrayList<>();
	
	private List<YellowCard> listaYellows = new ArrayList<>();
	
	
	private boolean validado = false;
	
	private GreenCard greenCard = new GreenCard();
	
	private YellowCard yellowCard = new YellowCard();
	
	private RedCard redCard = new RedCard();
	
	@PostConstruct
	private void init() throws Exception{
		redCard.setGreenCard(new GreenCard());
		yellowCard = new YellowCard();
		greenCard = new GreenCard();
	}
	
	public void salvar() throws Exception{
		
		try {
			
			if(this.redCard.getId() != null) {
				
				MensagensJSF.msgSeverityInfo("Ao salvar, sempre faça a limpeza primeiro");
				limpar();
			}else {
				
				redCardImpl.merge2(redCard);
				
				MensagensJSF.msgSeverityInfo("Card Salvo com Sucesso");
			}
		}catch(Exception e) {
			MensagensJSF.msgSeverityError("Não foi possível salvar o novo card no banco");
			e.printStackTrace();
		}
	}
	
	public void salvarAmarelo() throws Exception{
		
		if(this.yellowCard.getNumeroOcorrencia() != null || this.yellowCard.getNumeroOcorrencia().trim() != ""
				|| this.yellowCard.getUnidadesNormalizadas() != null) {
			
			this.redCard = redCardImpl.findRedCardById(this.redCard.getId());
			this.redCard.getYellowCards().add(this.yellowCard);
			
			this.yellowCard.setRedCard(this.redCard);
			
			this.yellowCard.setUnidadesInterrompidas(this.redCard.getUnidadeInterrompidas() - this.yellowCard.getUnidadesNormalizadas());
			
			this.redCard = redCardImpl.merge2(this.redCard);
			
			// REMOVER O QUE ESTÁ DUPLICADO
			
			this.redCard.getYellowCards().remove(this.yellowCard);
			
			this.redCard = redCardImpl.merge2(this.redCard);
			
			MensagensJSF.msgSeverityInfo("Salvo com sucesso!");
		}else {
			MensagensJSF.msgSeverityInfo("Preencha os dados");
		}
	}
	
	public void selecionarRedCardComYellows(RedCard redCard) throws Exception{
	    this.redCard = redCard;
	    carregarYellows();
	}
	
	public void salvarVerde() throws Exception{
		
		try {
			
			this.redCard = redCardImpl.findRedCardById(this.redCard.getId());
			
			this.greenCard.setRedCard(this.redCard);
			this.redCard.setGreenCard(this.greenCard);
			
			this.redCard = redCardImpl.merge2(this.redCard);
			MensagensJSF.msgSeverityInfo("Salvo com sucesso! MALDITO");
		}catch(Exception e) {
			MensagensJSF.msgSeverityError("Ocorreu um erro ao tentar salvar um card verde");
			e.printStackTrace();
		}
	}
	
	public void carregarYellows() throws Exception{
		
		if(redCard.getId() != null) {
			
			List<YellowCard> yellowCards = yellowCardsImpl.findByRedCardId(this.redCard.getId());
			
			Collections.sort(yellowCards, new Comparator<YellowCard>() {

				@Override
				public int compare(YellowCard o1, YellowCard o2) {
					return o2.getDataParcial().compareTo(o2.getDataParcial());
				}
			});
			
			listaYellows = yellowCards;
			
		}else {
			MensagensJSF.msgSeverityError("Tente reiniciar a página, não foi possível identificar o cartão vermelho");
		}
	}
	
	public void removerAmarelo(YellowCard yellowCard) throws Exception{
		
		if(yellowCard.getRedCard().getId() != null) {
			
			try {
				
				this.redCard = redCardImpl.findRedCardById(yellowCard.getRedCard().getId());
				
				this.redCard.getYellowCards().remove(yellowCard);
				
				this.redCard = redCardImpl.merge2(this.redCard);
				
				MensagensJSF.msgSeverityInfo("Removido com sucesso!");
				
			}catch(Exception e) {
				e.printStackTrace();
				MensagensJSF.erroNaOperacao();
			}
			
		}else {
			MensagensJSF.erroNaOperacao();
		}
	}
	
	public void deletarRedCard(RedCard redCard) throws Exception{
		
		if(redCard != null) {
			
			redCardImpl.remove(redCard);
		}
	}
	
	public void removerVerde(RedCard redCard) throws Exception {
		
	    if (redCard != null) {

	        this.redCard = redCardImpl.findRedCardById(redCard.getId());

	        this.greenCard = this.redCard.getGreenCard();

	        this.redCard.setGreenCard(null);
	        this.greenCard.setRedCard(null);

	        this.redCard = redCardImpl.merge2(this.redCard);

	        greenCardsImpl.remove(this.greenCard);

	        limpar();

	        MensagensJSF.msgSeverityInfo("Removido com sucesso");
	    } else {
	        MensagensJSF.msgSeverityError("Não existe redCard referenciado");
	    }
	}
	
	@SuppressWarnings("deprecation")
	public void findAll() throws Exception{
		
		List<RedCard> redCards = redCardImpl.findAll(RedCard.class);
		List<RedCard> listaFiltradaEtapa1 = new ArrayList<>();
		List<RedCard> listaFiltrada = new ArrayList<>();
		
		if(!this.filtrarRedCardPorOcorrencia.trim().equalsIgnoreCase("")) {
			
			for (RedCard redCard : redCards) {
				if(redCard.getNumeroOcorrencia().equalsIgnoreCase(this.filtrarRedCardPorOcorrencia)) {
					listaFiltradaEtapa1.add(redCard);
				}
			}
		}else {
			listaFiltradaEtapa1 = redCards;
		}
		
		if(this.filterRedCardPorData != null && !this.filterRedCardPorData.toString().trim().equalsIgnoreCase("")){
			
			for(RedCard redCard : listaFiltradaEtapa1) {
				if(redCard.getDataInicio().getDay() == this.filterRedCardPorData.getDay() && 
						redCard.getDataInicio().getMonth() == this.filterRedCardPorData.getMonth() && redCard.getDataInicio().getYear()
						== this.filterRedCardPorData.getYear()) {
					listaFiltrada.add(redCard);
				}
			}
		}else {
			listaFiltrada = listaFiltradaEtapa1;
		}
			
		Collections.sort(listaFiltrada, new Comparator<RedCard>() {

			@Override
			public int compare(RedCard o1, RedCard o2) {
				return o2.getDataInicio().compareTo(o1.getDataInicio());
			}
		});
		
		
		redCardsAll = listaFiltrada;
	}
	
	public void limparVerdeOuAmarelo() {
		
		if(yellowCard != null) {
			yellowCard = new YellowCard();
		}
		if(greenCard != null) {
			greenCard = new GreenCard();
		}
	}
	
	public void limpar() {
		
		redCard = new RedCard();
		
		if (redCard.getYellowCards() != null) {
			redCard.getYellowCards().clear(); 
		}
		if (redCard.getGreenCard() != null) {
			redCard.setGreenCard(null); 
		}
		greenCard = new GreenCard();
	}
	
	public void validarGreenCard(RedCard redCard) {
		
		if(redCard.getGreenCard() == null) {
			validado = true;
			this.redCard = redCard;
		}else {
			validado = false;
			MensagensJSF.msgSeverityInfo("Já existe um card verde para essa ocorrência");
		}
	}
	
	public List<String> buscarNumerosOcorrencia(String query) throws Exception{
		
		List<RedCard> redCardsListOcorrencias = redCardImpl.findAll(RedCard.class);
		List<String> ocorrenciaFiltradas = new ArrayList<>();
		
		for (RedCard redcard : redCardsListOcorrencias) {
			if(redcard.getNumeroOcorrencia().contains(query)) {
				ocorrenciaFiltradas.add(redcard.getNumeroOcorrencia().toLowerCase());
			}
		}

		return ocorrenciaFiltradas;
	}
	
	public YellowCard getYellowCard() {
		return yellowCard;
	}
	
	public void setYellowCard(YellowCard yellowCard) {
		this.yellowCard = yellowCard;
	}
	
	public boolean isValidado() {
	    return validado;
	}

	public void setValidado(boolean validado) {
	    this.validado = validado;
	}
	
	public RedCard getRedCard() {
		return redCard;
	}
	
	public void setRedCard(RedCard redCard) {
		this.redCard = redCard;
	}
	
	public List<YellowCard> getListaYellows() {
		return listaYellows;
	}
	
	public void setListaYellows(List<YellowCard> listaYellows) {
		this.listaYellows = listaYellows;
	}
	
	public GreenCard getGreenCard() {
		return greenCard;
	}
	
	public List<RedCard> getRedCardsAll() {
		return redCardsAll;
	}
	
	public void setRedCardsAll(List<RedCard> redCardsAll) {
		this.redCardsAll = redCardsAll;
	}

	public String getFiltrarRedCardPorOcorrencia() {
		return filtrarRedCardPorOcorrencia;
	}
	
	public Date getFilterRedCardPorData() {
		return filterRedCardPorData;
	}
	
	public void setFilterRedCardPorData(Date filterRedCardPorData) {
		this.filterRedCardPorData = filterRedCardPorData;
	}
	
	public void setFiltrarRedCardPorOcorrencia(String filtrarRedCardPorOcorrencia) {
		this.filtrarRedCardPorOcorrencia = filtrarRedCardPorOcorrencia;
	}
	
	public void setGreenCard(GreenCard greenCard) {
		this.greenCard = greenCard;
	}
}
