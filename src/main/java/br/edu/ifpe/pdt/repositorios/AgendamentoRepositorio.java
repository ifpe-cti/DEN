package br.edu.ifpe.pdt.repositorios;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.edu.ifpe.pdt.entidades.Agendamento;

public interface AgendamentoRepositorio extends JpaRepository<Agendamento, Integer> {

	
	public Agendamento findByCodigo(Integer codigo);
	public Agendamento findByData(Timestamp data);
	
	@Query("from Agendamento a  where a.data between ?1 and ?2")
	public List<Agendamento> findByTimestampInterval(Timestamp data, Timestamp data2);
	
}
