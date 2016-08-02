package br.edu.ifpe.pdt.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.edu.ifpe.pdt.entidades.Disciplina;

public interface DisciplinaRepositorio extends JpaRepository<Disciplina, Integer> {

	@Query("from Disciplina d where d.codigo = ?1")
	Disciplina findByCodigo(Integer codigo);

}
