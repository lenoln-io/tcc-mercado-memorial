package mainDefault;

import javax.swing.UIManager;
import view.LoginVIEW;

public class Main {

    public static void main(String[] args){
        
        UIManager.put("OptionPane.cancelButtonText", "Cancelar");  
        UIManager.put("OptionPane.noButtonText", "Não");  
        UIManager.put("OptionPane.yesButtonText", "Sim");
        new LoginVIEW().setVisible(true);
        
    }
    
}
