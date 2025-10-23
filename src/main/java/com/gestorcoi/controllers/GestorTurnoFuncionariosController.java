package com.gestorcoi.controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import com.gestorcoi.entities.Funcionarios;
import com.gestorcoi.implementations.FuncionarioImpl;
import com.gestorcoi.utils.MensagensJSF;

@ManagedBean(name = "gestorTurnoFuncionarios")
@ViewScoped
public class GestorTurnoFuncionariosController implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String filtro;
	private String filtroTurno;

	private FuncionarioImpl funcionarioImpl = new FuncionarioImpl();

	private List<Funcionarios> funcionarios = new ArrayList<>();
	private List<Funcionarios> funcionariosFiltrados = new ArrayList<>();

	private UploadedFile uploadedFile;

	private Map<String, Map<LocalDate, String>> tabelaDados = new HashMap<String, Map<LocalDate, String>>();
	private Map<LocalDate, String> mapaDias = new TreeMap<>();
	private List<LocalDate> dias;

	public GestorTurnoFuncionariosController() throws Exception {

		carregarFuncionariosTabelaPrincipal();
	}

	public void carregarFuncionariosTabelaPrincipal() throws Exception {

		funcionarios = funcionarioImpl.findAll(Funcionarios.class);

		funcionarios.sort(Comparator
				.comparing(Funcionarios::getAtividadeSuperintendencia,
						Comparator.nullsLast(String::compareToIgnoreCase))
				.thenComparing(Funcionarios::getSecao, Comparator.nullsLast(String::compareToIgnoreCase))
				.thenComparing(Funcionarios::getEscala, Comparator.nullsLast(String::compareToIgnoreCase).reversed())
				.thenComparing(Funcionarios::getNome, Comparator.nullsLast(String::compareToIgnoreCase)));

		setFuncionariosFiltrados(funcionarios);
	}

	public String formatarDataCabecalho(LocalDate dia) {

		return dia.format(DateTimeFormatter.ofPattern("dd/MM"));
	}

	public String getValor(String nome, LocalDate dia) {
		return tabelaDados.get(nome).get(dia);
	}

	public void gerarEscalaFiltrada() {

		funcionariosFiltrados = funcionarios;
		
		try {
			
			if(filtro != null && !filtro.trim().isEmpty()) {
				funcionariosFiltrados =  funcionariosFiltrados.stream().filter(s -> s.getAtividadeSuperintendencia().equalsIgnoreCase(filtro)).collect(Collectors.toList());
			}
		}catch(Exception e) {
			
			MensagensJSF.msgSeverityInfo("Não foi possível realizar o filtro", "Um erro inesperado");
		}
		
		gerarEscala();
	}

	public void gerarEscala() {

		String caminhoPadraoPlanilha = "C:/Users/E21057649/eclipse-workspace/com.gestorcoi/src/main/webapp/resources/uploads/escala.csv";

		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(caminhoPadraoPlanilha))) {
			
			// Utilitaria para erros como o .csv vir como 11/out
			Map<String, String> mesMap = new HashMap<>();
		    mesMap.put("jan", "01"); mesMap.put("fev", "02"); mesMap.put("mar", "03");
		    mesMap.put("abr", "04"); mesMap.put("mai", "05"); mesMap.put("jun", "06");
		    mesMap.put("jul", "07"); mesMap.put("ago", "08"); mesMap.put("set", "09");
		    mesMap.put("out", "10"); mesMap.put("nov", "11"); mesMap.put("dez", "12");

			String linha;

			dias = new ArrayList<>(); // retorno para a tela do usuário
			List<String> datas = new ArrayList<>();
			boolean datasSalvas = false;

			while ((linha = bufferedReader.readLine()) != null) {

				mapaDias = new TreeMap<>();

				String[] colunas = linha.split(";");

				Funcionarios funcionario = new Funcionarios();

				// Pegar os cabeçalhos de datas
				for (String buscaDatas : colunas) {

					if (buscaDatas != null && !buscaDatas.trim().isEmpty() && buscaDatas.contains("/")) {
						// System.out.println(linha);
						try {
							
							/**
							 * Erros de .csv vir como 11/out
							 */
							for (Map.Entry<String, String> entry : mesMap.entrySet()) {
							    String chave = entry.getKey();
							    String valor = entry.getValue();
							    
							    if(buscaDatas.split("/")[1].toLowerCase().equalsIgnoreCase(chave)) {
							    	buscaDatas = buscaDatas.split("/")[0] + "/" + valor;
							    }
							}
							
							datas.add(buscaDatas.trim());
							datasSalvas = true;

							// salvando a data para o usuário interface
							String primeiroDigito = buscaDatas.split("/")[0];
							int anoAtual = LocalDate.now().getYear();
							String dataAtualColuna;

							// Verificação de valor 1/10 que nao pode ser convertido

							if (Integer.parseInt(primeiroDigito) < 10) {
								dataAtualColuna = "0" + buscaDatas + "/" + anoAtual;
							} else {
								dataAtualColuna = buscaDatas + "/" + anoAtual;
							}

							DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
							LocalDate dataColunaFormatada = LocalDate.parse(dataAtualColuna, formatter);

							dias.add(dataColunaFormatada);
						} catch (Exception e) {
							continue;
						}
					}
				}
				if (datasSalvas == true) {

					int contadorColuna = 0;

					for (String texto : colunas) {

						contadorColuna++;

						// Funcionarios e mapa de dias
						if (texto != null && !colunas[4].toLowerCase().contains("controladores")
								&& !colunas[4].toLowerCase().contains("avaliador")
								&& !colunas[4].toLowerCase().contains("férias")
								&& !colunas[4].toLowerCase().contains("licença")
								&& !colunas[4].toLowerCase().contains("falta")
								&& !colunas[4].toLowerCase().contains("compensação")) {

							if (contadorColuna == 4) {
								funcionario.setNome(texto);
							} else if (contadorColuna == 5) {
								funcionario.setTipo(texto);
							} else if (contadorColuna == 6) {
								funcionario.setAtividadeSuperintendencia(texto);
								
								if(filtro != null && !filtro.trim().isEmpty()) {
						            if(!funcionario.getAtividadeSuperintendencia().equalsIgnoreCase(filtro)) {
						                funcionario = null; 
						                break; 
						            }
						        }
							} else if (contadorColuna == 7) {
								funcionario.setRegional(texto);
							} else if (contadorColuna > 7) {

								// Índice relativo à lista de datas
								int indiceData = contadorColuna - 7; // ajusta o deslocamento

								if (indiceData >= 0 && indiceData < datas.size()) {
									try {
										String dataTexto = datas.get(indiceData);

										String primeiroDigito = dataTexto.split("/")[0];
										int anoAtual = LocalDate.now().getYear();
										String dataAtualColuna;

										if (Integer.parseInt(primeiroDigito) < 10) {
											dataAtualColuna = "0" + dataTexto + "/" + anoAtual;
										} else {
											dataAtualColuna = dataTexto + "/" + anoAtual;
										}

										DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
										LocalDate dataColunaFormatada = LocalDate.parse(dataAtualColuna, formatter);

										mapaDias.put(dataColunaFormatada, texto);
									} catch (Exception e) {
										continue;
									}
								}
							}

						} else {
							continue;
						}
					}
					
					if(funcionario != null) {
						if(filtroTurno != null && !filtroTurno.trim().equals("")) {
							
							
						}else {
							tabelaDados.put(funcionario.getNome(), mapaDias);
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * Salvamento do arquivo planilha das escalas
	 * 
	 * @param event
	 */
	public void handleFileUpload(FileUploadEvent event) {

		uploadedFile = event.getFile();
		salvarEscalaExcel();
	}

	public void salvarEscalaExcel() {

		try {

			if (uploadedFile != null && uploadedFile.getFileName().endsWith(".csv")) {
				try (InputStream input = uploadedFile.getInputstream()) {

					String uploadedDir = "C:/Users/E21057649/eclipse-workspace/com.gestorcoi/src/main/webapp/resources/uploads/";

					File targetFolder = new File(uploadedDir);

					if (!targetFolder.exists()) {
						targetFolder.mkdirs();
					}

					File targetFile = new File(uploadedDir + "escala.csv");

					try (FileOutputStream outputStream = new FileOutputStream(targetFile)) {
						byte[] buffer = new byte[1024];
						int bytesRead;
						while ((bytesRead = input.read(buffer)) != -1) {
							outputStream.write(buffer, 0, bytesRead);
						}
					}

					System.out.println("Arquivo salvo em: " + targetFile.getAbsolutePath());

					MensagensJSF.msgSeverityInfo("Escala importada com sucesso!", "Sucesso");

				} catch (Exception e) {
					MensagensJSF.msgSeverityError("Não foi possível salvar esse arquivo");
				}
			} else {
				MensagensJSF.msgSeverityInfo("Arquivo não econtrado", "Um erro inesperado");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<Funcionarios> getFuncionariosFiltrados() {
		return funcionariosFiltrados;
	}

	public void setFuncionariosFiltrados(List<Funcionarios> funcionariosFiltrados) {
		this.funcionariosFiltrados = funcionariosFiltrados;
	}

	public Map<String, Map<LocalDate, String>> getTabelaDados() {
		return tabelaDados;
	}

	public List<LocalDate> getDias() {
		return dias;
	}

	public void setDias(List<LocalDate> dias) {
		this.dias = dias;
	}

	public void setTabelaDados(Map<String, Map<LocalDate, String>> tabelaDados) {
		this.tabelaDados = tabelaDados;
	}

	public String getClasseCelula(String nome, LocalDate dia) {

		String valor = getValor(nome, dia);

		if (valor != null && valor.contains("-")) {
			String[] corte = valor.trim().split("-");
			if (corte.length > 0) {
				return "fundo-amarelo";
			}
		}

		switch (valor != null ? valor.toUpperCase() : "") {

		case "X":
			return "fundo-verde";
		case "PTP":
			return "fundo-azul";
		case "FÉRIAS":
			return "fundo-laranja";
		case "R":
			return "fundo-azul-claro";
		case "S":
			return "fundo-azul-claro";
		case "A":
			return "fundo-azul-claro";
		case "T":
			return "fundo-azul-claro";
		case "B":
			return "fundo-azul-claro";
		case "LICENÇA":
			return "fundo-cinza";
		default:
			return "";
		}
	}
	
	public List<String> filtrosAtividadesAutoComplete(String query){
		
		List<String> retorno = new ArrayList<>();
		
		retorno.add("Emergencial");
		retorno.add("Comercial");
		retorno.add("Supervisão");
		retorno.add("Impacto");
		retorno.add("Manobra");
		retorno.add("Avaliação");
		retorno.add("Ilha de Risco");
		retorno.add("PTP");
		retorno.add("TRIAGEM");
		
		return retorno.stream().filter(s -> s.toUpperCase().contains(query.toUpperCase())).collect(Collectors.toList());
	}
	
	public List<String> filtrosTurno(String query){
		
		List<String> retorno = new ArrayList<>();
		
		retorno.add("1");
		retorno.add("2");
		retorno.add("3");
		retorno.add("4");
		
		return retorno.stream().filter(s -> s.toUpperCase().contains(query.toUpperCase())).collect(Collectors.toList());
	}
	
	public UploadedFile getUploadedFile() {
		return uploadedFile;
	}
	
	public void setUploadedFile(UploadedFile uploadedFile) {
		this.uploadedFile = uploadedFile;
	}

	public String getFiltro() {
		return filtro;
	}

	public void setFiltro(String filtro) {
		this.filtro = filtro;
	}

	public String getFiltroTurno() {
		return filtroTurno;
	}

	public void setFiltroTurno(String filtroTurno) {
		this.filtroTurno = filtroTurno;
	}
}
