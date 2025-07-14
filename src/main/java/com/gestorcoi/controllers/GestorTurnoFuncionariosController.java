package com.gestorcoi.controllers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import com.gestorcoi.entities.Funcionarios;
import com.gestorcoi.implementations.FuncionarioImpl;
import com.gestorcoi.utils.MensagensJSF;

@ManagedBean(name = "gestorTurnoFuncionarios")
@ViewScoped
public class GestorTurnoFuncionariosController {

	private FuncionarioImpl funcionarioImpl = new FuncionarioImpl();

	// Registro de escala

	private String tipo;
	private String folgaEmergencialDia;

	private Date datainicio;
	private Date datafinal;

	private List<LocalDate> dias;

	private List<Funcionarios> funcionarios = new ArrayList<>();

	private Map<String, Map<LocalDate, String>> tabelaDados;

	public List<Funcionarios> carregarFuncionariosTabelaPrincipal() throws Exception {

		funcionarios = funcionarioImpl.findAll(Funcionarios.class);
		return funcionarios;
	}

	public void gerarEscala() {

		if (datainicio != null && datafinal != null) {

			dias = new ArrayList<>();

			LocalDate inicio = new java.sql.Date(datainicio.getTime()).toLocalDate();
			LocalDate fim = new java.sql.Date(datafinal.getTime()).toLocalDate();

			LocalDate atual = inicio;
			long diferencaDias = ChronoUnit.DAYS.between(inicio, fim);

			if (diferencaDias == 35) {
				while (!atual.isAfter(fim)) {
					dias.add(atual);
					atual = atual.plusDays(1);
				}

				tabelaDados = new HashMap<String, Map<LocalDate, String>>();
				
				for (Funcionarios funcionario : funcionarios) {
				    Map<LocalDate, String> mapadias = new HashMap<>();

				    int offset = 0;
				    try {
				        offset = Integer.parseInt(folgaEmergencialDia);
				    } catch (Exception e) {
				        offset = 0;
				    }

				    // Definir offset conforme a seção
				    if ("2".equals(funcionario.getSecao())) {
				        offset += 2;
				    } else if ("3".equals(funcionario.getSecao())) {
				        offset += 4;
				    }

				    int index = 0;

				    for (LocalDate dia : dias) {
				        int ciclo = (index - offset + 6) % 6; // Mantém dentro de [0,5]

				        String valor = "1"; // padrão

				        if ("6666".equals(funcionario.getEscala())) {
				            if (ciclo >= 4) {
				                valor = "X"; // folga
				            } else {
				                valor = "6"; // trabalho
				            }
				        } else if ("15151515".equals(funcionario.getEscala())) {
				            valor = (ciclo >= 4) ? "X" : "15";
				        } else if ("21212121".equals(funcionario.getEscala())) {
				            valor = (ciclo >= 4) ? "X" : "21";
				        }

				        mapadias.put(dia, valor);
				        index++;
				    }

				    tabelaDados.put(funcionario.getNome(), mapadias);
				}} else {
				MensagensJSF.msgSeverityInfo("O limite máximo entre as datas não pode ser diferente de 35 dias",
						"Operação não realizada");
			}
		}
	}

	public String formatarDataCabecalho(LocalDate dia) {

		return dia.format(DateTimeFormatter.ofPattern("dd/MM"));
	}

	public String getValor(String nome, LocalDate dia) {
		return tabelaDados.get(nome).get(dia);
	}

	public List<String> returnTipos() {

		List<String> tipos = new ArrayList<>();
		tipos.add("Emergencial");

		return tipos;
	}

	public Date getDatafinal() {
		return datafinal;
	}

	public Date getDatainicio() {
		return datainicio;
	}

	public String getFolgaEmergencialDia() {
		return folgaEmergencialDia;
	}

	public void setFolgaEmergencialDia(String folgaEmergencialDia) {
		this.folgaEmergencialDia = folgaEmergencialDia;
	}

	public void setDatafinal(Date datafinal) {
		this.datafinal = datafinal;
	}

	public void setDatainicio(Date datainicio) {
		this.datainicio = datainicio;
	}

	public String getTipo() {
		return tipo;
	}

	public List<LocalDate> getDias() {
		return dias;
	}

	public void setDias(List<LocalDate> dias) {
		this.dias = dias;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
}
