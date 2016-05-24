package br.edu.ifpe.pdt.repositorios;

import java.sql.Date;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpe.pdt.entidades.Falta;

public interface FaltaRepositorio extends JpaRepository<Falta, Integer> {

	public Falta findByDisciplinaCodigoAndData(Integer disciplina, Date valueOf);

}
