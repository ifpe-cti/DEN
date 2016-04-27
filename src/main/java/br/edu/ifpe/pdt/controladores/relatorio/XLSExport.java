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
			f = new File(AppContext.getRelatorioPath());
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
