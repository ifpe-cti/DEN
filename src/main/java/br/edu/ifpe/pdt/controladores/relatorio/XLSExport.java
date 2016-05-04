package br.edu.ifpe.pdt.controladores.relatorio;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import br.edu.ifpe.pdt.controladores.util.AppContext;
import br.edu.ifpe.pdt.entidades.Falta;
import br.edu.ifpe.pdt.entidades.PTD;

public class XLSExport {

	public static File exportarCargaHorariaSemestre(List<PTD> ptds) {

		int rowid = 0;
		int cellid = 0;
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet spreadsheet = workbook.createSheet("Carga Horaria");
		XSSFRow row;

		row = spreadsheet.createRow(rowid++);
		Cell cell = row.createCell(cellid++);
		cell.setCellValue("SIAPE");
		cell = row.createCell(cellid++);
		cell.setCellValue("Nome");
		cell = row.createCell(cellid++);
		cell.setCellValue("Status");
		cell = row.createCell(cellid++);
		cell.setCellValue("Atualizado em");
		cell = row.createCell(cellid++);
		cell.setCellValue("Carga Horária");

		for (PTD ptd : ptds) {
			cellid = 0;
			row = spreadsheet.createRow(rowid++);
			cell = row.createCell(cellid++);
			cell.setCellValue(ptd.getProfessor().getSiape());
			cell = row.createCell(cellid++);
			cell.setCellValue(ptd.getProfessor().getNome());
			cell = row.createCell(cellid++);
			cell.setCellValue(ptd.getStatus().name());
			cell = row.createCell(cellid++);
			cell.setCellValue(ptd.getLastUpdate().toString());
			cell = row.createCell(cellid++);
			cell.setCellValue(ptd.getCargaHoraria());
		}
		// Write the workbook in file system
		FileOutputStream out;
		File f = null;
		try {
			f = new File(AppContext.getRelatorioPath() + "cargaHoraria.xlsx");
			f.createNewFile();
			out = new FileOutputStream(f);
			workbook.write(out);
			out.close();
		} catch (FileNotFoundException e) {
			f = null;
			e.printStackTrace();
		} catch (IOException e) {
			f = null;
			e.printStackTrace();
		}

		return f;
	}

	public static File exportarFaltaProfessores(List<Falta> faltas) {
		
		int rowid = 0;
		int cellid = 0;
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet spreadsheet = workbook.createSheet("Faltas Professores");
		XSSFRow row;

		row = spreadsheet.createRow(rowid++);
		Cell cell = row.createCell(cellid++);
		cell.setCellValue("SIAPE");
		cell = row.createCell(cellid++);
		cell.setCellValue("Nome");
		cell = row.createCell(cellid++);
		cell.setCellValue("Disciplina");
		cell = row.createCell(cellid++);
		cell.setCellValue("Turma");
		cell = row.createCell(cellid++);
		cell.setCellValue("Data Falta");
		cell = row.createCell(cellid++);
		cell.setCellValue("Número de Faltas");
		cell = row.createCell(cellid++);
		cell.setCellValue("Data Reposição");
		cell = row.createCell(cellid++);
		cell.setCellValue("Número de Aulas Repostas");
		cell = row.createCell(cellid++);
		cell.setCellValue("Observação");
		
		for (Falta falta : faltas) {
			cellid = 0;
			row = spreadsheet.createRow(rowid++);
			cell = row.createCell(cellid++);
			cell.setCellValue(falta.getProfessor().getSiape());
			cell = row.createCell(cellid++);
			cell.setCellValue(falta.getProfessor().getNome());
			cell = row.createCell(cellid++);
			cell.setCellValue(falta.getDisciplina().getNome());
			cell = row.createCell(cellid++);
			cell.setCellValue(falta.getTurma());
			cell = row.createCell(cellid++);
			cell.setCellValue(falta.getData().toString());
			cell = row.createCell(cellid++);
			cell.setCellValue(falta.getNumeroFaltas());
			cell = row.createCell(cellid++);
			if (falta.getReposicao() != null) {
				cell.setCellValue(falta.getReposicao().toString());
			}
			cell = row.createCell(cellid++);
			if (falta.getNumeroAulasRepostas() != null) {
				cell.setCellValue(falta.getNumeroAulasRepostas());
			}
			cell = row.createCell(cellid++);
			if (falta.getObservacao() != null) {
				cell.setCellValue(falta.getObservacao());
			}
		}
		
		// Write the workbook in file system
		FileOutputStream out;
		File f = null;
		try {
			f = new File(AppContext.getRelatorioPath() + "faltaProfessores.xlsx");
			f.createNewFile();
			out = new FileOutputStream(f);
			workbook.write(out);
			out.close();
		} catch (FileNotFoundException e) {
			f = null;
			e.printStackTrace();
		} catch (IOException e) {
			f = null;
			e.printStackTrace();
		}

		return f;
	}
}
