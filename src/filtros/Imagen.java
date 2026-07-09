package filtros;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class Imagen {
	public static BufferedImage aumentoR(BufferedImage original, BufferedImage resultado, int mascara) {
		int pixel, pixelNuevo, a, r, g, b;

		for (int x = 0; x < original.getWidth(); x++) {
			for (int y = 0; y < original.getHeight(); y++) {
				pixel = original.getRGB(x, y);

				a = (pixel >> 24) & mascara;
				r = (pixel >> 16) & mascara;
				g = (pixel >> 8) & mascara;
				b = (pixel >> 0) & mascara;

				r = clamp(r+40);

				pixelNuevo = (a << 24) | (r << 16) | (g << 8) | (b << 0);
				resultado.setRGB(x, y, pixelNuevo);
			}
		}

		return resultado;
	}
	public static BufferedImage escalaGrisesL(BufferedImage original, BufferedImage resultado, int mascara) {
		int pixel, pixelNuevo, a, r, g, b;

		for (int x = 0; x < original.getWidth(); x++) {
			for (int y = 0; y < original.getHeight(); y++) {
				pixel = original.getRGB(x, y);

				a = (pixel >> 24) & mascara;
				r = (pixel >> 16) & mascara;
				g = (pixel >> 8) & mascara;
				b = (pixel >> 0) & mascara;

				int luminancia = (int)(0.299*r+0.587*g+0.114*b);
				r=g=b=luminancia;
				

				pixelNuevo = (a << 24) | (r << 16) | (g << 8) | (b << 0);
				resultado.setRGB(x, y, pixelNuevo);
			}
		}

		return resultado;
	}
	public static BufferedImage umbralizacion(BufferedImage original, BufferedImage resultado, int mascara, int umbral) {
		int pixel, pixelNuevo, a, r, g, b;

		for (int x = 0; x < original.getWidth(); x++) {
			for (int y = 0; y < original.getHeight(); y++) {
				pixel = original.getRGB(x, y);

				a = (pixel >> 24) & mascara;
				r = (pixel >> 16) & mascara;
				g = (pixel >> 8) & mascara;
				b = (pixel >> 0) & mascara;

				int luminancia = (int)(0.299*r+0.587*g+0.114*b);
				int color = luminancia>=umbral?255:0;
				r=g=b=color;
				

				pixelNuevo = (a << 24) | (r << 16) | (g << 8) | (b << 0);
				resultado.setRGB(x, y, pixelNuevo);
			}
		}

		return resultado;
	}

	public static int clamp(int n) {
		return Math.max(0, Math.min(255, n));
	}

	public static void main(String[] args) {
		int ancho, alto;
		int mascara = 0xFF;

		try {
			BufferedImage buffer = ImageIO.read(new File("src/imagenes/perro.png"));
			

			BufferedImage buffer2 = new BufferedImage(buffer.getWidth(), buffer.getHeight(),
					BufferedImage.TYPE_INT_ARGB);

			ImageIO.write(aumentoR(buffer, buffer2, mascara), "png", new File("src/imagenes/ejericio1.png"));
			ImageIO.write(escalaGrisesL(buffer, buffer2, mascara), "png", new File("src/imagenes/ejericio2.png"));
			ImageIO.write(umbralizacion(buffer, buffer2, mascara,100), "png", new File("src/imagenes/ejericio3_100.png"));
			ImageIO.write(umbralizacion(buffer, buffer2, mascara,50), "png", new File("src/imagenes/ejericio3_50.png"));
			ImageIO.write(umbralizacion(buffer, buffer2, mascara,200), "png", new File("src/imagenes/ejericio3_200.png"));
			
			System.out.println("correcto");

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("error: " + e.getMessage());
		}
	}

}
