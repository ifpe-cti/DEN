package br.edu.ifpe.pdt.entidades;

import java.util.List;

import javax.faces.bean.ManagedProperty;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table( name = "professor" )
public class Professor {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id", updatable=false)
	private Integer codigo;
	
	@Column(name="siape", nullable=false, length=12, unique=true)
	private String siape;
	
	@Column(name="senha", nullable=false, length=12)
	private String senha;
	
	@Column(name="coordenacao", nullable=false, length=10)
	private String coordenacao;
	
	@Column(name="nome", nullable=false, length=100)
	private String nome;

	@Column(name="email", nullable=false, length=50, unique=true)
	private String email;
	
	@Column(name="isGestao", nullable=false)
	private boolean isGestao;
	
	@ManagedProperty(value="#{ptds}")
	@OneToMany(mappedBy="professor", fetch=FetchType.EAGER, orphanRemoval=true, cascade=CascadeType.ALL)
	private List<PTD> ptds;	
	
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

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
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
	
	public boolean isGestao() {
		return isGestao;
	}

	public void setGestao(boolean isGestao) {
		this.isGestao = isGestao;
	}

	public List<PTD> getPtds() {
		return ptds;
	}

	public void setPtds(List<PTD> ptds) {
		this.ptds = ptds;
	}
}
