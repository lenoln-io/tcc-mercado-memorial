package utility;

import java.text.ParseException;
import javax.swing.text.MaskFormatter;

public class GeradorDeMascaras {

    public static MaskFormatter createMaskByIgor(String mascara, char prenchimento){
        MaskFormatter mskMoney = null;
        try {
            mskMoney = new MaskFormatter(mascara);
            mskMoney.setPlaceholderCharacter(prenchimento);
        } catch (ParseException ex) {  
            System.out.println(ex.getMessage());  
        }
        return mskMoney;
    }
    
}
