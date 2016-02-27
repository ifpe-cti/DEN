package br.edu.ifpe.pdt.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpe.pdt.entidades.Professor;

public interface ProfessorRepositorio extends JpaRepository<Professor, Integer> {

	Professor findBySiape(String siape);
	List<Professor> findByCoordenacao(String coordenacao);
}
