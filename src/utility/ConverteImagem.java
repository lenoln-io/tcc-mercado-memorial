package utility;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class ConverteImagem {

	public static Image byteToImage(byte[] binario) {	

		BufferedImage imagem = null;

		if(binario != null){
			try {
				imagem = ImageIO.read(new ByteArrayInputStream(binario));
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
		return imagem;		

	}

	public static byte[] imageToByte(Image imagem) {	

		ByteArrayOutputStream buff = null;

		if(imagem != null){
			BufferedImage bi = new BufferedImage(imagem.getWidth(null),imagem.getHeight(null),BufferedImage.TYPE_INT_RGB);
			Graphics bg = bi.getGraphics();
			bg.drawImage(imagem, 0, 0, null);
			bg.dispose();

			buff = new ByteArrayOutputStream();		

			try {  
				ImageIO.write(bi, "JPG", buff);  
			} catch (IOException e) {  
				e.printStackTrace();  
			}  
		}
		return buff.toByteArray();		

	}

	public static Image inputStreamToImagem(InputStream is) {
		Image imagem = null;

		if(is != null){
			try {
				imagem = ImageIO.read(is);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return imagem;
	}

	public static InputStream imageToInputStream(Image image) {	

		BufferedImage bi = new BufferedImage(image.getWidth(null),image.getHeight(null),BufferedImage.TYPE_INT_RGB);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			ImageIO.write(bi, "JPG", os);
		} catch (IOException e) {
			e.printStackTrace();
		}
		InputStream is = new ByteArrayInputStream(os.toByteArray());

		return is;
	}

}
