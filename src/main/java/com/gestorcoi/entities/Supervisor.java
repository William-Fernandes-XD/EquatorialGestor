package com.gestorcoi.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "supervisor")
public class Supervisor {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(unique = false)
	private String name;
	private String password;
	
	private String email;
	
	@OneToMany(mappedBy = "avaliador", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Feedback> feedbacks;
	
	@OneToMany(mappedBy = "supervisorEntrandoOcorrencia", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Ocorrencia> ocorrencias;
	
	@OneToMany(mappedBy = "supervisor", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Roles> roles;
	
	@OneToMany(mappedBy = "supervisorSaindo", cascade = CascadeType.ALL)
	private List<GestorEntity> declarados;
	
	@OneToMany(mappedBy = "supervisorEntrando", cascade = CascadeType.ALL)
	private List<GestorEntity> pendenciasOcorrencias;

	public Supervisor() {

	}
	
	public Supervisor(Long id, String name, String password) {
		this.id = id;
		this.name = name;
		this.password = password;
	}

	public Long getId() {
		return id;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	@JsonIgnore
	public List<Feedback> getFeedbacks() {
		return feedbacks;
	}
	
	public void setFeedbacks(List<Feedback> feedbacks) {
		this.feedbacks = feedbacks;
	}
	
	@JsonIgnore
	public List<GestorEntity> getDeclarados() {
		return declarados;
	}
	
	@JsonIgnore
	public List<GestorEntity> getPendenciasOcorrencias() {
		return pendenciasOcorrencias;
	}
	
	public void setDeclarados(List<GestorEntity> declarados) {
		this.declarados = declarados;
	}
	
	public void setPendenciasOcorrencias(List<GestorEntity> pendenciasOcorrencias) {
		this.pendenciasOcorrencias = pendenciasOcorrencias;
	}
	
	@JsonIgnore
	public List<Ocorrencia> getOcorrencias() {
		return ocorrencias;
	}
	
	public List<Roles> getRoles() {
		return roles;
	}
	
	public void setRoles(List<Roles> roles) {
		this.roles = roles;
	}
	
	public void setOcorrencias(List<Ocorrencia> ocorrencias) {
		this.ocorrencias = ocorrencias;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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
		Supervisor other = (Supervisor) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
