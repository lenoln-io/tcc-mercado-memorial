package utility;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.JPanel;


public class PanelVisualizaImagem extends JPanel {

    private Image imagem;


    public PanelVisualizaImagem() {
    }

    public PanelVisualizaImagem(Image imagem) {
        setImagem(imagem);
    }

    public PanelVisualizaImagem(String caminho) {
        setImagem(caminho);
    }

    public void setImagem(Image imagem) {
        this.imagem = imagem;
        this.repaint();
    }

    public void setImagem(String caminho) {
        setImagem(Toolkit.getDefaultToolkit().createImage(caminho));
    }

    public Image getImagem() {
        return this.imagem;
    }

    public void limparImagem(){
        setImagem("");
    }

    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        g.drawImage(this.imagem, 0, 0, this.getWidth(), this.getHeight(), this);
    }


}