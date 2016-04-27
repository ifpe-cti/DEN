package br.edu.ifpe.pdt.entidades;

import java.io.Serializable;
import java.sql.Date;
import java.util.LinkedHashSet;
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
import javax.persistence.Transient;

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
		
		public static STATUS getStatus(Integer auto) {
			STATUS a = null;
			
			switch (auto) {
			case 0:
				a = CRIADO;
				break;
			case 1:
				a = AGUARDO;
				break;
			case 2:
				a = HOMOLOGADO;
				break;
			case 3:
				a = APROVADO;
				break;
			case 4:
				a = CORRECAO;
				break;
			case 5:
				a = FECHADO;
				break;
			default:
				break;
			}
			
			return a;
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
	
	@Column(name="ano")
	private Integer ano;
	
	@Column(name="semestre")
	private Integer semestre;
	
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

	@Transient
	private Integer cargaHoraria;
	
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

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public Integer getSemestre() {
		return semestre;
	}

	public void setSemestre(Integer semestre) {
		this.semestre = semestre;
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
	
	public Integer getCargaHoraria() {
		return cargaHoraria;
	}

	public void setCargaHoraria(Integer cargaHoraria) {
		this.cargaHoraria = cargaHoraria;
	}

	public PTD clone() {
		PTD ptd = new PTD();
		
		ptd.setDisciplinas(new LinkedHashSet<Disciplina>());
		for (Disciplina d: this.disciplinas) {
			Disciplina clone = new Disciplina();
			clone.setNome(d.getNome());
			clone.setCurso(d.getCurso());
			clone.setCargaHoraria(d.getCargaHoraria());
			clone.setPTD(ptd);
			ptd.getDisciplinas().add(clone);
		}
		
		ptd.setAaes(new LinkedHashSet<AAE>());
		for (AAE aae: this.aaes) {
			AAE clone = new AAE();
			clone.setAtividade(aae.getAtividade());
			clone.setCargaHoraria(aae.getCargaHoraria());
			clone.setPTD(ptd);
			ptd.getAaes().add(clone);
		}
		
		ptd.setAaps(new LinkedHashSet<AAP>());
		for (AAP aap: this.aaps) {
			AAP clone = new AAP();
			clone.setAtividade(aap.getAtividade());
			clone.setPortaria(aap.getPortaria());
			clone.setPTD(ptd);
			ptd.getAaps().add(clone);
		}
		
		ptd.setExtensoes(new LinkedHashSet<Extensao>());
		for (Extensao e: this.extensoes) {
			Extensao clone = new Extensao();
			clone.setAtividade(e.getAtividade());
			clone.setPTD(ptd);
			ptd.getExtensoes().add(clone);
		}
		
		ptd.setPesquisas(new LinkedHashSet<Pesquisa>());
		for (Pesquisa p: this.pesquisas) {
			Pesquisa clone = new Pesquisa();
			clone.setAtividade(p.getAtividade());
			clone.setPTD(ptd);
			ptd.getPesquisas().add(clone);
		}
		
		return ptd;		
	}
}
