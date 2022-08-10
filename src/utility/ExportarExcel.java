package utility;

import java.io.File;

import javax.swing.JTable;
import javax.swing.table.TableModel;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class ExportarExcel {
	
	  public void GerarArquivo(JTable tabela, File arquivo) {

	        try {

	            WritableWorkbook workbook1 = Workbook.createWorkbook(arquivo);
	            WritableSheet sheet1 = workbook1.createSheet("Planilha", 0); 
	            TableModel model = tabela.getModel();

	            for (int i = 0; i < model.getColumnCount(); i++) {
	                Label column = new Label(i, 0, model.getColumnName(i));
	                sheet1.addCell(column);
	            }
	            int j = 0;
	            for (int i = 0; i < model.getRowCount(); i++) {
	                for (j = 0; j < model.getColumnCount(); j++) {
	                    Label row = new Label(j, i + 1, 
	                            model.getValueAt(i, j).toString());
	                    sheet1.addCell(row);
	                }
	            }
	            workbook1.write();
	            workbook1.close();
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
	    }


}
