package br.edu.ifpe.pdt.controladores;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.edu.ifpe.pdt.controladores.util.AppContext;
import br.edu.ifpe.pdt.entidades.Agendamento;
import br.edu.ifpe.pdt.entidades.Agendamento.AGENDAMENTO_STATUS;
import br.edu.ifpe.pdt.repositorios.AgendamentoRepositorio;
import br.edu.ifpe.pdt.util.PTDEmail;

@Component
@ManagedBean(name = "agendamentoControlador", eager = true)
@SessionScoped
public class AgendamentoControlador implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private AgendamentoRepositorio agendamentoRepositorio;

	public String cadastrarAgendamento(String escola, String responsavel, String numAlunos, String email,
			String telefone, String data, Integer horario) {
		String ret = "/agendamento/agendamento.xhtml";

		try {
			if (escola != null && escola.length() > 0 && responsavel != null && responsavel.length() > 0
					&& email != null && email.length() > 0 && data != null && data.length() > 0) {
				Agendamento a = new Agendamento();

				Timestamp visita = defineDataVisita(data, horario);

				List<Agendamento> ags = agendamentoRepositorio.findByTimestampInterval(defineDataInicio(data),
						defineDataFim(data));

				if (ags.size() <= 1) {

					Agendamento marcado = agendamentoRepositorio.findByData(visita);
					if (marcado == null) {

						a.setData(visita);
						a.setEmail(email);
						a.setEscola(escola);
						if (numAlunos != null && numAlunos.length() > 0) {
							a.setNumAlunos(Integer.valueOf(numAlunos.trim()));
						}
						a.setResponsavel(responsavel);
						a.setTelefone(telefone);
						a.setStatus(AGENDAMENTO_STATUS.CRIADO);

						agendamentoRepositorio.saveAndFlush(a);

						PTDEmail mail = new PTDEmail();
						String subject = "Nova visita Agendada.";
						String msg = "A Escola " + escola + " Registrou uma nova solicitação de visita no sistema.\n\n"
								+ "Conferir: sisdiven.garanhuns.ifpe.edu.br/DEN/agendamento/lista.xhtml";
						mail.postMail("den@garanhuns.ifpe.edu.br", subject, msg, AppContext.getEmailAuth());

						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
								"Agendamento cadastrado com Sucesso!", ""));
					} else {
						FacesContext.getCurrentInstance().addMessage(null,
								new FacesMessage(FacesMessage.SEVERITY_ERROR,
										"Já existe visita marcada para esse dia e horário! Favor escolher outro dia ou horário.",
										""));
					}
				} else {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Já temos o número máximo de visitas para esse dia!", ""));

				}
			} else {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Dados inválidos!", ""));
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Dados inválidos!", ""));
		}

		return ret;
	}

	private Timestamp defineDataFim(String data) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Integer.valueOf(data.substring(0, 4)), Integer.valueOf(data.substring(5, 7)),
				Integer.valueOf(data.substring(8, 10)), 23, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 0);
		Timestamp inicio = new Timestamp(calendar.getTimeInMillis());

		return inicio;
	}

	private Timestamp defineDataInicio(String data) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Integer.valueOf(data.substring(0, 4)), Integer.valueOf(data.substring(5, 7)),
				Integer.valueOf(data.substring(8, 10)), 0, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Timestamp fim = new Timestamp(calendar.getTimeInMillis());

		return fim;
	}

	private Timestamp defineDataVisita(String data, Integer horario) {

		Calendar calendar = Calendar.getInstance();

		Integer hora = 0;
		Integer minuto = 0;

		switch (horario) {
		case 1:
			hora = 8;
			minuto = 30;
			break;
		case 2:
			hora = 9;
			minuto = 30;
			break;
		case 3:
			hora = 14;
			break;
		case 4:
			hora = 15;
			break;
		}
		
		calendar.set(Calendar.DAY_OF_MONTH, Integer.valueOf(data.substring(8, 10)));
		calendar.set(Calendar.MONTH, Integer.valueOf(data.substring(5, 7)) - 1);
		calendar.set(Calendar.YEAR, Integer.valueOf(data.substring(0, 4)));
		calendar.set(Calendar.HOUR_OF_DAY, hora);
		calendar.set(Calendar.MINUTE, minuto);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Timestamp visita = new Timestamp(calendar.getTimeInMillis());

		return visita;
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
