package br.edu.ifpe.pdt.entidades;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table( name = "agendamento" )
public class Agendamento implements Serializable{
	
	private static final long serialVersionUID = 1L;

	public static enum AGENDAMENTO_STATUS{
		CRIADO,
		INVALIDO,
		MARCADO,
		REALIZADO;
		
		public int getOrdinal() {
			return this.ordinal();
		}
		
		public String getNome() {
			return this.name();
		}
		
		public static AGENDAMENTO_STATUS getAgendamentoStatus(Integer auto) {
			AGENDAMENTO_STATUS a = null;
			
			switch (auto) {
			case 0:
				a = CRIADO;
				break;
			case 1:
				a = INVALIDO;
				break;
			case 2:
				a = MARCADO;
				break;
			case 3:
				a = REALIZADO;
				break;			
			default:
				break;
			}
			
			return a;
		}
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id", updatable=false)
	private Integer codigo;
	
	@Column
	private String escola;
	
	@Column
	private String responsavel;
	
	@Column
	private Integer numAlunos;
	
	@Column
	private String email;
	
	@Column
	private String telefone;
	
	@Column
	private Timestamp data;
	
	@Column
	private AGENDAMENTO_STATUS status;

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getEscola() {
		return escola;
	}

	public void setEscola(String escola) {
		this.escola = escola;
	}

	public String getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(String responsavel) {
		this.responsavel = responsavel;
	}

	public Integer getNumAlunos() {
		return numAlunos;
	}

	public void setNumAlunos(Integer numAlunos) {
		this.numAlunos = numAlunos;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public Timestamp getData() {
		return data;
	}

	public void setData(Timestamp data) {
		this.data = data;
	}

	public AGENDAMENTO_STATUS getStatus() {
		return status;
	}

	public void setStatus(AGENDAMENTO_STATUS status) {
		this.status = status;
	}
}
