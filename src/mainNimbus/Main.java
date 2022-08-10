package mainNimbus;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import view.LoginVIEW;

public class Main {

    public static void main(String[] args){
        
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
            UIManager.put("OptionPane.cancelButtonText", "Cancelar");  
            UIManager.put("OptionPane.noButtonText", "Não");  
            UIManager.put("OptionPane.yesButtonText", "Sim");
            new LoginVIEW().setVisible(true);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
