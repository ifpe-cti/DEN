package br.edu.ifpe.pdt.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpe.pdt.entidades.Disciplina;

public interface DisciplinaRepositorio extends JpaRepository<Disciplina, Integer> {

}
