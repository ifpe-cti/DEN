package br.edu.ifpe.pdt.controladores;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.edu.ifpe.pdt.controladores.relatorio.XLSExport;
import br.edu.ifpe.pdt.entidades.Disciplina;
import br.edu.ifpe.pdt.entidades.PTD;
import br.edu.ifpe.pdt.repositorios.PTDRepositorio;

@Component
@ManagedBean(name = "relatorioControlador", eager = true)
@SessionScoped
public class RelatorioControlador implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final int LISTAR_CARGA_HORARIA_SEMESTRE = 1;

	@Autowired
	private PTDRepositorio ptdRepositorio;

	public String emitirRelatorio(Integer relatorioID) {
		String ret = "";

		switch (relatorioID) {
		case LISTAR_CARGA_HORARIA_SEMESTRE:
			ret = "/restrito/relatorio/professorCargaHoraria.xhtml";
			break;
		default:
			break;
		}

		return ret;
	}

	public String listarCargaHorariaSemestre(Integer ano, Integer semestre) {

		List<PTD> ptds = this.ptdRepositorio.findByAnoAndSemestre(ano, semestre);

		for (PTD ptd : ptds) {
			Integer cargaHoraria = 0;

			for (Disciplina disciplina : ptd.getDisciplinas()) {
				cargaHoraria += disciplina.getCargaHoraria();
			}

			ptd.setCargaHoraria(cargaHoraria);
		}

		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("selectedPtds", ptds);

		return "";
	}

	@SuppressWarnings("unchecked")
	public void exportarCargaHorariaSemestre() {
		List<PTD> ptds = (List<PTD>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("selectedPtds");

		File f = XLSExport.exportarCargaHorariaSemestre(ptds);

		if (f != null) { 
			FacesContext fc = FacesContext.getCurrentInstance();
			ExternalContext ec = fc.getExternalContext();
			
			ec.responseReset();
			ec.setResponseContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			ec.setResponseContentLength((int) f.length());
			ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + f.getName() + "\"");
			try {
				OutputStream output = ec.getResponseOutputStream();

				FileInputStream fis = new FileInputStream(f);
				while (fis.available() > -1) {
					output.write(fis.read());
				}
				
				output.flush();
				output.close();
				fis.close();
				fc.responseComplete();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
