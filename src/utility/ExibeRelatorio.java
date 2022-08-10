package utility;

import java.awt.BorderLayout;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Map;
import javax.swing.JFrame;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.swing.JRViewer;

public class ExibeRelatorio {

    public static void exibirRelatorio(String titulo, InputStream inputStream, Map parametros, Connection conexao) throws JRException{
        JasperPrint  print = JasperFillManager.fillReport(inputStream, parametros, conexao);
        criarJanela(titulo, print);
    }

    private static void criarJanela(String titulo, JasperPrint  print){
        JRViewer jabelaRelatorio = new JRViewer(print);
        JFrame frameRelatorio = new JFrame(titulo);
        frameRelatorio.add(jabelaRelatorio, BorderLayout.CENTER);
        frameRelatorio.setSize(500, 500);
        frameRelatorio.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frameRelatorio.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frameRelatorio.setVisible(true);		
    }
    
}
