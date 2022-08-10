package utility;

import java.text.DateFormat;
import java.util.Date;
import java.text.SimpleDateFormat;

public class DataUtility {

    public static String formatarDataTexto(Date data){
        String data_br;
        SimpleDateFormat formato_br = new SimpleDateFormat("dd/MM/yyyy");
        data_br = formato_br.format(data);
        return data_br;
    }
    
    public static String formataStrignDateTimeEnToBr(String datetime){
        String databr;
        datetime = datetime.replace(".0", "");
        String ano = datetime.substring(0, 4);
        String mes = datetime.substring(5,7);
        String dia = datetime.substring(8,10);
        String resto = datetime.substring(11);
        databr = dia+"/"+mes+"/"+ano+" "+resto;
        return databr;
    }
    
    public static String getDateTime(String lg) {
        if(lg.equals("PT")){
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
            java.util.Date date = new java.util.Date();
            return dateFormat.format(date);
        }else{
            if(lg.equals("EN")){
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
                java.util.Date date = new java.util.Date();
                return dateFormat.format(date);
            }else{
                return null;
            }
        }
    }
    
}