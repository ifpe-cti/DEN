package br.edu.ifpe.pdt.controladores.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import br.edu.ifpe.pdt.entidades.Professor;
import br.edu.ifpe.pdt.entidades.Professor.AUTORIZACAO;
import br.edu.ifpe.pdt.repositorios.ProfessorRepositorio;

@Component
public class StartupListener implements ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	private ProfessorRepositorio professorRepositorio;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {

		Professor p = professorRepositorio.findBySiape("admin");

		if (p == null) {
			p = new Professor();
			p.setSiape("admin");
			String senha = MD5Hash.md5("adminadmin");
			p.setSenha(senha);
			p.setNome("Admin");
			p.setEmail("admin");
			p.setCoordenacao("admin");
			p.setAutorizacao(AUTORIZACAO.SUPER);
			professorRepositorio.saveAndFlush(p);
		}

		p = professorRepositorio.findBySiape("crat");

		if (p == null) {
			p = new Professor();
			p.setSiape("crat");
			String senha = MD5Hash.md5("cratcrat");
			p.setSenha(senha);
			p.setNome("CRAT");
			p.setEmail("crat");
			p.setCoordenacao("crat");
			p.setAutorizacao(AUTORIZACAO.CRAT);
			professorRepositorio.saveAndFlush(p);
		}
	}
}
