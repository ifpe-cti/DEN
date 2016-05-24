package br.edu.ifpe.pdt.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpe.pdt.entidades.PTD;

public interface PTDRepositorio extends JpaRepository<PTD, Integer> {

	
	List<PTD> findByProfessorSiape(String siape);
	List<PTD> findByProfessorCoordenacao(String coordenacao);
	List<PTD> findByAnoAndSemestre(Integer ano, Integer semestre);
	PTD findByAnoAndSemestreAndProfessorSiape(Integer ano, Integer semestre, String siape);
	PTD findByCodigo(Integer codigo);
}
