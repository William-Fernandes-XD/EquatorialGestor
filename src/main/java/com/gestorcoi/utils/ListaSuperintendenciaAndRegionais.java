package com.gestorcoi.utils;

import java.util.ArrayList;
import java.util.List;

public class ListaSuperintendenciaAndRegionais {
	
	public static List<String> pegarRegionais() {
		
		List<String> listaRegionais = new ArrayList<>();
		listaRegionais.add("Goiânia");
		listaRegionais.add("Metropolitana");
		
		listaRegionais.add("Anápolis");
		listaRegionais.add("Uruaçu");
		
		listaRegionais.add("Luziânia");
		listaRegionais.add("Formosa");
		
		listaRegionais.add("Rio Verde");
		listaRegionais.add("Morrinhos");
		
		listaRegionais.add("Iporá");
		listaRegionais.add("Montes Belos");
		
		return listaRegionais;
	}
	
	public static List<String> pegarSuperintendencias() {
		
		List<String> listaSuperintendencias = new ArrayList<>();
		
		listaSuperintendencias.add("Centro");
		listaSuperintendencias.add("Norte");
		listaSuperintendencias.add("Nordeste");
		listaSuperintendencias.add("Sul");
		listaSuperintendencias.add("Sudoeste");
		
		return listaSuperintendencias;
	}
	
	public static List<String> pegarDistribuidoras(){
		
		List<String> distribuidoras = new ArrayList<>();
		
		distribuidoras.add("EQTL GO");
		
		return distribuidoras;
	}
	
	public static List<String> tensoeskVA(){
		
		List<String> tensoes = new ArrayList<>();
		
		tensoes.add("13.8");
		tensoes.add("69.0");
		tensoes.add("138.0");
		
		return tensoes;
	}
}
