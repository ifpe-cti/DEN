package br.edu.ifpe.pdt.repositorios;

import java.util.List;

import br.edu.ifpe.pdt.entidades.PTD;
import br.edu.ifpe.pdt.entidades.Professor;
import br.edu.ifpe.pdt.repositorios.util.DaoManagerHiber;

public class PTDRepository {

	private static final String HQL_SELECT_PTD_BY_PROFESSOR = 
			"from PTD where professor.siape = ':siape' order by lastUpdate desc";
		
	private static final String HQL_SELECT_PROFESSOR_BY_COORDENACAO = 
			"from Professor where coordenacao = ':coordenacao'";
	
	private static PTDRepository instance;
	
	private PTDRepository() {
		DaoManagerHiber.getInstance();
	}
	
	public static PTDRepository getInstance() {
		if (instance == null) {
			instance = new PTDRepository();
		}
		
		return instance;
	}
	
	public void savePTD(PTD ptd) {
		DaoManagerHiber.getInstance().persist(ptd);
	}
	
	public void updatePTD(PTD ptd) {
		DaoManagerHiber.getInstance().update(ptd);
	}
	
	public List<PTD> findByProfessorCodigo(Integer codigo){
		return null;
	}
	
	public List<PTD> findByProfessorSiape(String siape) {
		List<PTD> ptds = null;
		
		ptds = DaoManagerHiber.getInstance().recover(
				HQL_SELECT_PTD_BY_PROFESSOR.replace(":siape", siape));
		
		return ptds;
	}

	public List<Professor> findByProfessorCoordenacao(String coordenacao) {
		List<Professor> professores = null;
		
		professores = DaoManagerHiber.getInstance().recover(
				HQL_SELECT_PROFESSOR_BY_COORDENACAO.replace(":coordenacao", coordenacao));
		
		return professores;
	}
}
