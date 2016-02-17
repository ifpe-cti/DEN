package edu.ifpe.pdt.entidades;

import java.util.Set;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@ManagedBean(name = "professor")
@RequestScoped
@Entity
@Table( name = "professor" )
public class Professor {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id", updatable=false)
	private Integer codigo;
	
	@Column(name="siape", nullable=false, length=200)
	private String siape;
	
	@Column(name="coordenacao", nullable=false, length=200)
	private String coordenacao;
	
	@Column(name="nome", nullable=false, length=200)
	private String nome;

	@Column(name="email", nullable=false, length=200)
	private String email;
	
	@ManagedProperty(value="#{disciplinas}")
	@OneToMany(mappedBy="professor", fetch=FetchType.EAGER, orphanRemoval=true)
	private Set<Disciplina> disciplinas;
	
	@ManagedProperty(value="#{aaes}")
	@OneToMany(mappedBy="professor", fetch=FetchType.EAGER, orphanRemoval=true)
	private Set<AAE> aaes;
	
	@ManagedProperty(value="#{pesquisas}")
	@OneToMany(mappedBy="professor", fetch=FetchType.EAGER, orphanRemoval=true)
	private Set<Pesquisa> pesquisas;
	
	@ManagedProperty(value="#{extensoes}")
	@OneToMany(mappedBy="professor", fetch=FetchType.EAGER, orphanRemoval=true)
	private Set<Extensao> extensoes;
	
	@ManagedProperty(value="#{aaps}")
	@OneToMany(mappedBy="professor", fetch=FetchType.EAGER, orphanRemoval=true)
	private Set<AAP> aaps;

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getSiape() {
		return siape;
	}

	public void setSiape(String siape) {
		this.siape = siape;
	}

	public String getCoordenacao() {
		return coordenacao;
	}

	public void setCoordenacao(String coordenacao) {
		this.coordenacao = coordenacao;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Set<Disciplina> getDisciplinas() {
		return disciplinas;
	}

	public void setDisciplinas(Set<Disciplina> disciplinas) {
		this.disciplinas = disciplinas;
	}

	public Set<AAE> getAaes() {
		return aaes;
	}

	public void setAaes(Set<AAE> aaes) {
		this.aaes = aaes;
	}

	public Set<Pesquisa> getPesquisas() {
		return pesquisas;
	}

	public void setPesquisas(Set<Pesquisa> pesquisas) {
		this.pesquisas = pesquisas;
	}

	public Set<Extensao> getExtensoes() {
		return extensoes;
	}

	public void setExtensoes(Set<Extensao> extensoes) {
		this.extensoes = extensoes;
	}

	public Set<AAP> getAaps() {
		return aaps;
	}

	public void setAaps(Set<AAP> aaps) {
		this.aaps = aaps;
	}
}
