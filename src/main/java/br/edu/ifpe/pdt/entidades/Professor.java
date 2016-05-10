package br.edu.ifpe.pdt.entidades;

import java.io.Serializable;
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
public class Professor implements Serializable{
	
	private static final long serialVersionUID = 1L;

	public static enum AUTORIZACAO{
		PROFESSOR,
		CCTI,
		CCTMA,
		CCTEE,
		CCTIN,
		DIVEN,
		DEN,
		SUPER,
		CRAT;
		
		public int getOrdinal() {
			return this.ordinal();
		}
		
		public String getNome() {
			return this.name();
		}
		
		public static AUTORIZACAO getAutorizacao(Integer auto) {
			AUTORIZACAO a = null;
			
			switch (auto) {
			case 0:
				a = PROFESSOR;
				break;
			case 1:
				a = CCTI;
				break;
			case 2:
				a = CCTMA;
				break;
			case 3:
				a = CCTEE;
				break;
			case 4:
				a = CCTIN;
				break;
			case 5:
				a = DIVEN;
				break;
			case 6:
				a = DEN;
				break;
			case 7:
				a = SUPER;
				break;
			case 8:
				a = CRAT;
				break;
			default:
				break;
			}
			
			return a;
		}
	};
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id", updatable=false)
	private Integer codigo;
	
	@Column(name="siape", nullable=false, length=12, unique=true)
	private String siape;
	
	@Column(name="senha", nullable=false)
	private String senha;
	
	@Column(name="coordenacao", nullable=false, length=10)
	private String coordenacao;
	
	@Column(name="nome", nullable=false, length=100)
	private String nome;

	@Column(name="email", nullable=false, length=50, unique=true)
	private String email;
	
	@Column(name="autorizacao")
	private AUTORIZACAO autorizacao;
	
	@ManagedProperty(value="#{ptds}")
	@OneToMany(mappedBy="professor", fetch=FetchType.EAGER, orphanRemoval=true, cascade=CascadeType.ALL)
	private List<PTD> ptds;	
	
	@OneToMany(mappedBy="professor", fetch=FetchType.EAGER, orphanRemoval=true, cascade=CascadeType.ALL)
	private List<Falta> faltas;	
	
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

	public AUTORIZACAO getAutorizacao() {
		return autorizacao;
	}

	public void setAutorizacao(AUTORIZACAO autorizacao) {
		this.autorizacao = autorizacao;
	}

	public List<PTD> getPtds() {
		return ptds;
	}

	public void setPtds(List<PTD> ptds) {
		this.ptds = ptds;
	}

	public List<Falta> getFaltas() {
		return faltas;
	}

	public void setFaltas(List<Falta> faltas) {
		this.faltas = faltas;
	}
}
