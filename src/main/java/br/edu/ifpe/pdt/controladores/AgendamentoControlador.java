package br.edu.ifpe.pdt.controladores;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.edu.ifpe.pdt.entidades.Agendamento;
import br.edu.ifpe.pdt.entidades.Agendamento.AGENDAMENTO_STATUS;
import br.edu.ifpe.pdt.repositorios.AgendamentoRepositorio;

@Component
@ManagedBean(name = "agendamentoControlador", eager = true)
@SessionScoped
public class AgendamentoControlador implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private AgendamentoRepositorio agendamentoRepositorio;

	public String cadastrarAgendamento(String escola, String responsavel, Integer numAlunos, String email,
			String telefone, String data) {
		String ret = "";

		if (escola != null && escola.length() > 0 && responsavel != null && responsavel.length() > 0 && email != null
				&& email.length() > 0 && data != null && data.length() > 0) {
			Agendamento a = new Agendamento();
			a.setData(Date.valueOf(data));
			a.setEmail(email);
			a.setEscola(escola);
			a.setNumAlunos(numAlunos);
			a.setResponsavel(responsavel);
			a.setTelefone(telefone);
			a.setStatus(AGENDAMENTO_STATUS.CRIADO);

			agendamentoRepositorio.saveAndFlush(a);

			// enviar email para diex?

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Agendamento cadastrado com Sucesso!", ""));
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Dados inválidos!", ""));
		}

		return ret;
	}

	public String alterarAgendamento(Integer codigo, Integer status) {
		String ret = "";

		if (codigo != null) {
			Agendamento a = agendamentoRepositorio.findByCodigo(codigo);
			a.setStatus(AGENDAMENTO_STATUS.getAgendamentoStatus(status));

			agendamentoRepositorio.saveAndFlush(a);

			List<Agendamento> ags = agendamentoRepositorio.findAll();
			
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("agendamentos", ags);
			
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Agendamento alterado com Sucesso!", ""));
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Selecione um agendamento!", ""));
		}

		return ret;
	}

	public String listarAgendamentos() {
		String ret = "";

		List<Agendamento> ags = agendamentoRepositorio.findAll();
		
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("agendamentos", ags);
		return ret;
	}
}
