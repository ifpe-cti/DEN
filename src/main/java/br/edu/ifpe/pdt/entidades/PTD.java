package br.edu.ifpe.pdt.entidades;

import java.io.Serializable;
import java.sql.Date;
import java.util.Set;

import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/*
 * Plano de Trabalho Docente
 */

@RequestScoped
@Entity
@Table( name = "ptd" )
public class PTD implements Serializable {

	public static enum STATUS{
		CRIADO,
		AGUARDO,
		HOMOLOGADO,
		APROVADO,
		CORRECAO,
		FECHADO;
		
		public int getOrdinal() {
			return this.ordinal();
		}
	};
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id", updatable=false)
	private Integer codigo;
	
	@Column(name="status")
	private STATUS status;
	
	@Column(name="last_update")
	private Date lastUpdate;
	
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="professor_id")
	private Professor professor;
	
	@Column(name="resultado", length=2000)
	private String resultado;	

	@ManagedProperty(value="#{disciplinas}")
	@OneToMany(mappedBy="ptd", fetch=FetchType.EAGER, orphanRemoval=true, cascade=CascadeType.ALL)
	private Set<Disciplina> disciplinas;
	
	@ManagedProperty(value="#{aaes}")
	@OneToMany(mappedBy="ptd", fetch=FetchType.EAGER, orphanRemoval=true, cascade=CascadeType.ALL)
	private Set<AAE> aaes;
	
	@ManagedProperty(value="#{pesquisas}")
	@OneToMany(mappedBy="ptd", fetch=FetchType.EAGER, orphanRemoval=true, cascade=CascadeType.ALL)
	private Set<Pesquisa> pesquisas;
	
	@ManagedProperty(value="#{extensoes}")
	@OneToMany(mappedBy="ptd", fetch=FetchType.EAGER, orphanRemoval=true, cascade=CascadeType.ALL)
	private Set<Extensao> extensoes;
	
	@ManagedProperty(value="#{aaps}")
	@OneToMany(mappedBy="ptd", fetch=FetchType.EAGER, orphanRemoval=true, cascade=CascadeType.ALL)
	private Set<AAP> aaps;

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public STATUS getStatus() {
		return status;
	}

	public void setStatus(STATUS status) {
		this.status = status;
	}

	public Professor getProfessor() {
		return professor;
	}

	public void setProfessor(Professor professor) {
		this.professor = professor;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public String getResultado() {
		return resultado;
	}

	public void setResultado(String resultado) {
		this.resultado = resultado;
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
	
	public PTD clone() {
		PTD ptd = new PTD();
		
		return ptd;		
	}
}
