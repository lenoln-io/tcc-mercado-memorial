package report;

import config.Conexao;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;
import utility.ExibeRelatorio;

public class ExecutarRelatorio {

    /*private String relatorio = "";*/
    
    public void abrirRelatorio(String nomeJanela, String relatorio) throws JRException{
        
        /*this.relatorio = relatorio;*/
        
        InputStream nomeRelatorio = getClass().getResourceAsStream(relatorio);
        Map parametros = new HashMap();
        
        ExibeRelatorio.exibirRelatorio( nomeJanela, nomeRelatorio, parametros, new Conexao().getConexao());
        
    }
    
}
