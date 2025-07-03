package com.gestorcoi.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.PrimeFaces;

import com.gestor.entityCards.GreenCard;
import com.gestor.entityCards.RedCard;
import com.gestor.entityCards.YellowCard;
import com.gestorcoi.implementations.GreenCardsImpl;
import com.gestorcoi.implementations.RedCardImpl;
import com.gestorcoi.implementations.YellowCardsImpl;
import com.gestorcoi.utils.ListaSuperintendenciaAndRegionais;
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
				
				MensagensJSF.msgSeverityInfo("Ao salvar, sempre faça a limpeza primeiro", "Dados no campo ID");
				limpar();
			}else if (this.redCard.getUnidadeInterrompidas() == null || this.redCard.getDataInicio() == null
					|| this.redCard.getRegional() == null) {
				MensagensJSF.msgSeverityInfo("Preencha as informações primeiro", "Dados Incompletos");
			}else {
				
				redCardImpl.merge2(redCard);
				
				MensagensJSF.msgSeverityInfo("Card Salvo com Sucesso", "Salvo");
			}
		}catch(Exception e) {
			MensagensJSF.msgSeverityError("Não foi possível salvar o novo card no banco");
			e.printStackTrace();
		}
	}
	
	public void salvarAmarelo() throws Exception{
		
		this.yellowCard.setDistribuidora(redCard.getDistribuidora());
		this.yellowCard.setRegional(redCard.getRegional());
		this.yellowCard.setCodigoEquipamento(redCard.getCodigoEquipamento());
		this.yellowCard.setNumeroOcorrencia(redCard.getNumeroOcorrencia());
		this.yellowCard.setSuperintendencia(redCard.getSuperintendencia());
		this.yellowCard.setSe(redCard.getSe());
		this.yellowCard.setAlimentador(redCard.getAlimentador());
		
		if(this.yellowCard.getDataInicio() == null || this.yellowCard.getDataParcial() == null
				|| this.yellowCard.getSe() == null || this.yellowCard.getAlimentador() == null
				|| this.yellowCard.getObservacao() == null) {
			
			MensagensJSF.msgSeverityInfo("Preencha os dados", "Dados Incompletos");
		}else {
			this.redCard = redCardImpl.findRedCardById(this.redCard.getId());
			this.redCard.getYellowCards().add(this.yellowCard);
			
			this.yellowCard.setRedCard(this.redCard);
			
			this.yellowCard.setUnidadesInterrompidas(this.redCard.getUnidadeInterrompidas() - this.yellowCard.getUnidadesNormalizadas());
			
			
			this.redCard = redCardImpl.merge2(this.redCard);
			
			// REMOVER O QUE ESTÁ DUPLICADO
			
			this.redCard.getYellowCards().remove(this.yellowCard);
			
			this.redCard = redCardImpl.merge2(this.redCard);
			
			MensagensJSF.msgSeverityInfo("Card amarelo salvo com sucesso!", "Salvo");
		}
	}
	
	public void selecionarRedCardComYellows(RedCard redCard) throws Exception{
	    this.redCard = redCard;
	    carregarYellows();
	}
	
	public void salvarVerde() throws Exception{
		
		try {
			
			this.greenCard.setDistribuidora(redCard.getDistribuidora());
			this.greenCard.setRegional(redCard.getRegional());
			this.greenCard.setCodigoEquipamento(redCard.getCodigoEquipamento());
			this.greenCard.setSuperintendencia(redCard.getSuperintendencia());
			this.greenCard.setSe(redCard.getSe());
			this.greenCard.setAlimentador(redCard.getAlimentador());
			this.greenCard.setUnidadesNormalizadas(redCard.getUnidadeInterrompidas());
			this.greenCard.setNumeroOcorrencia(redCard.getNumeroOcorrencia());
			
			if(this.greenCard.getDataInicio() == null || this.greenCard.getDataNormalizada() == null
				|| this.greenCard.getCausa() == null	) {
				
				MensagensJSF.msgSeverityInfo("Preencha as informações para salvar", "Dados Incompletos");
			}else {
				
				this.redCard = redCardImpl.findRedCardById(this.redCard.getId());
				
				
				this.greenCard.setRedCard(this.redCard);
				this.redCard.setGreenCard(this.greenCard);
				
				this.redCard = redCardImpl.merge2(this.redCard);
				MensagensJSF.msgSeverityInfo("Card verde salvo com sucesso!", "Salvo");
			}
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
				
				MensagensJSF.msgSeverityInfo("Card amarelo removido com sucesso!", "Removido");
				
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
		
	    if (redCard != null && redCard.getGreenCard().getId() != null) {

	        this.redCard = redCardImpl.findRedCardById(redCard.getId());

	        this.greenCard = this.redCard.getGreenCard();

	        this.redCard.setGreenCard(null);
	        this.greenCard.setRedCard(null);

	        this.redCard = redCardImpl.merge2(this.redCard);

	        greenCardsImpl.remove(this.greenCard);

	        limpar();

	        MensagensJSF.msgSeverityInfo("Card verde removido com sucesso!", "Removido");
	    } else {
	        MensagensJSF.msgSeverityError("Não existe esse green card");
	    }
	}
	
	public List<String> pegarDistribuidoras(String query){
		
		List<String> distribuidoras = ListaSuperintendenciaAndRegionais.pegarDistribuidoras();
		
		List<String> listaFiltrada = new ArrayList<>();
		
		for (String obj : distribuidoras) {
			if(obj.toLowerCase().contains(query.toLowerCase())) {
				listaFiltrada.add(obj.toUpperCase());
			}
		}
		
		return listaFiltrada;
	}
	
	public List<String> pegarTensoes(String query){
		
		List<String> tensoeskva = ListaSuperintendenciaAndRegionais.tensoeskVA();
		List<String> listaFiltrada = new ArrayList<>();
		
		for (String obj : tensoeskva) {
			
			if(obj.toLowerCase().contains(query.toLowerCase())) {
				listaFiltrada.add(obj);
			}
		}
		
		return listaFiltrada;
	}
	
	/**
	 * 
	 * Retorna todas as regionais para os campos input
	 * 
	 * @param query
	 * @return
	 */
	public List<String> pegarRegionais(String query){
		
		List<String> regionais = ListaSuperintendenciaAndRegionais.pegarRegionais();
		
		List<String> listaFiltrada = new ArrayList<>();
		
		for (String obj : regionais) {
			if(obj.toLowerCase().contains(query.toLowerCase())) {
				listaFiltrada.add(obj.toUpperCase());
			}
		}
		
		return listaFiltrada;
	}
	
	/**
	 * 
	 * Retorna todas as superintendencias aos campos input
	 * @param query
	 * @return
	 */
	
	public List<String> pegarSuperintendencas(String query){
		
		List<String> superintendencias = ListaSuperintendenciaAndRegionais.pegarSuperintendencias();
		
		List<String> listaFiltrada = new ArrayList<>();
		
		for (String obj : superintendencias) {
			
			if(obj.contains(query)) {
				listaFiltrada.add(obj);
			}
		}
		
		return listaFiltrada;
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
	
	public List<String> pegarNumeroOcorrenciaRedCard() {
		
		List<String> numeroOcorrencia = new ArrayList<>();
		
		numeroOcorrencia.add(redCard.getNumeroOcorrencia());
		
		return numeroOcorrencia;
	}
	
	public List<String> pegarCodigoEquipamentoRedCard() {
		
		List<String> codEquipamento = new ArrayList<>();
		
		codEquipamento.add(redCard.getCodigoEquipamento());
		
		return codEquipamento;
	}
	
	public void validarGreenCard(RedCard redCard) {
		
		if(redCard.getGreenCard() == null) {
			validado = true;
			this.redCard = redCard;
			
			PrimeFaces.current().executeScript("PF('redCardsDialog').show(); PF('adicionarGreenCard').show()");
		}else {
			validado = false;
			PrimeFaces.current().executeScript("PF('redCardsDialog').show();");
			MensagensJSF.msgSeverityInfo("Já existe um card verde para essa ocorrência", "Card existente");
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
