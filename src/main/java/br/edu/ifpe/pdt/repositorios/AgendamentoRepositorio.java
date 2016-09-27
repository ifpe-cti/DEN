package br.edu.ifpe.pdt.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpe.pdt.entidades.Agendamento;

public interface AgendamentoRepositorio extends JpaRepository<Agendamento, Integer> {

	
	public Agendamento findByCodigo(Integer codigo);
}
