package br.edu.ifpe.pdt.controladores.relatorio.beans;

import br.edu.ifpe.pdt.entidades.Professor;

public class ProfessorCargaHoraria {

	private Professor professor;
	private Integer cargaHoraria;
	
	public ProfessorCargaHoraria() {
		
	}
	
	public ProfessorCargaHoraria(Professor professor) {
		this.professor = professor;
	}
	
	public Professor getProfessor() {
		return professor;
	}
	
	public void setProfessor(Professor professor) {
		this.professor = professor;
	}
	
	public Integer getCargaHoraria() {
		return cargaHoraria;
	}
	
	public void setCargaHoraria(Integer cargaHoraria) {
		this.cargaHoraria = cargaHoraria;
	}
}
