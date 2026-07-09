package filtros;

import java.awt.Color;
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

				r = clamp(r + 40);

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

				int luminancia = (int) (0.299 * r + 0.587 * g + 0.114 * b);
				r = g = b = luminancia;

				pixelNuevo = (a << 24) | (r << 16) | (g << 8) | (b << 0);
				resultado.setRGB(x, y, pixelNuevo);
			}
		}

		return resultado;
	}

	public static BufferedImage umbralizacion(BufferedImage original, BufferedImage resultado, int mascara,
			int umbral) {
		int pixel, pixelNuevo, a, r, g, b;

		for (int x = 0; x < original.getWidth(); x++) {
			for (int y = 0; y < original.getHeight(); y++) {
				pixel = original.getRGB(x, y);

				a = (pixel >> 24) & mascara;
				r = (pixel >> 16) & mascara;
				g = (pixel >> 8) & mascara;
				b = (pixel >> 0) & mascara;

				int luminancia = (int) (0.299 * r + 0.587 * g + 0.114 * b);
				int color = luminancia >= umbral ? 255 : 0;
				r = g = b = color;

				pixelNuevo = (a << 24) | (r << 16) | (g << 8) | (b << 0);
				resultado.setRGB(x, y, pixelNuevo);
			}
		}

		return resultado;
	}

	public static BufferedImage modificarSaturacion(BufferedImage original, BufferedImage resultado,
			float factorSaturacion, int mascara) {

		for (int x = 0; x < original.getWidth(); x++) {
			for (int y = 0; y < original.getHeight(); y++) {
				int pixel = original.getRGB(x, y);

				int a = (pixel >> 24) & mascara;
				int r = (pixel >> 16) & mascara;
				int g = (pixel >> 8) & mascara;
				int b = (pixel >> 0) & mascara;

				int gris = (int) (0.299 * r + 0.587 * g + 0.114 * b);

				int r1 = clamp((int) (gris + factorSaturacion * (r - gris)));
				int g1 = clamp((int) (gris + factorSaturacion * (g - gris)));
				int b1 = clamp((int) (gris + factorSaturacion * (b - gris)));

				int pixelNuevo = (a << 24) | (r1 << 16) | (g1 << 8) | b1;
				resultado.setRGB(x, y, pixelNuevo);
			}
		}
		return resultado;
	}

	public static BufferedImage rotarHue(BufferedImage original, BufferedImage resultado, float grados, int mascara) {

		for (int x = 0; x < original.getWidth(); x++) {
			for (int y = 0; y < original.getHeight(); y++) {
				int pixel = original.getRGB(x, y);

				int a = (pixel >> 24) & mascara;
				int r = (pixel >> 16) & mascara;
				int g = (pixel >> 8) & mascara;
				int b = (pixel >> 0) & mascara;

				float[] hsb = Color.RGBtoHSB(r, g, b, null);

				hsb[0] = (hsb[0] + (grados / 360f)) % 1.0f;

				int nuevoRgb = Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);

				int r1 = (nuevoRgb >> 16) & mascara;
				int g1 = (nuevoRgb >> 8) & mascara;
				int b1 = (nuevoRgb >> 0) & mascara;

				int pixelNuevo = (a << 24) | (r1 << 16) | (g1 << 8) | b1;
				resultado.setRGB(x, y, pixelNuevo);
			}
		}
		return resultado;
	}

	public static BufferedImage modificarBrillo(BufferedImage original, BufferedImage resultado, float factorBrillo,
			int mascara) {

		for (int x = 0; x < original.getWidth(); x++) {
			for (int y = 0; y < original.getHeight(); y++) {
				int pixel = original.getRGB(x, y);

				int a = (pixel >> 24) & mascara;
				int r = (pixel >> 16) & mascara;
				int g = (pixel >> 8) & mascara;
				int b = (pixel >> 0) & mascara;

				int r1 = clamp((int) (r * 1.0 + factorBrillo));
				int g1 = clamp((int) (g * 1.0 + factorBrillo));
				int b1 = clamp((int) (b * 1.0 + factorBrillo));

				int pixelNuevo = (a << 24) | (r1 << 16) | (g1 << 8) | b1;
				resultado.setRGB(x, y, pixelNuevo);
			}
		}
		return resultado;
	}

	public static BufferedImage interpolacion(BufferedImage original, BufferedImage resultado, int color, float t,
			int mascara) {

		t = Math.max(0f, Math.min(1f, t));
		for (int x = 0; x < original.getWidth(); x++) {
			for (int y = 0; y < original.getHeight(); y++) {
				int pixel = original.getRGB(x, y);

				int a = (pixel >> 24) & mascara;
				int r = (pixel >> 16) & mascara;
				int g = (pixel >> 8) & mascara;
				int b = (pixel >> 0) & mascara;

				color = clamp(color);

				int r1 = clamp((int) ((1 - t) * r + t * color));
				int g1 = clamp((int) ((1 - t) * g + t * color));
				int b1 = clamp((int) ((1 - t) * b + t * color));

				int pixelNuevo = (a << 24) | (r1 << 16) | (g1 << 8) | b1;
				resultado.setRGB(x, y, pixelNuevo);
			}
		}
		return resultado;
	}

	public static BufferedImage altoContraste(BufferedImage original, BufferedImage resultado, float escala,
			int mascara) {

		for (int x = 0; x < original.getWidth(); x++) {
			for (int y = 0; y < original.getHeight(); y++) {
				int pixel = original.getRGB(x, y);

				int a = (pixel >> 24) & mascara;
				int r = (pixel >> 16) & mascara;
				int g = (pixel >> 8) & mascara;
				int b = (pixel >> 0) & mascara;

				int r1 = clamp((int) (r * escala));
				int g1 = clamp((int) (g * escala));
				int b1 = clamp((int) (b * escala));

				int pixelNuevo = (a << 24) | (r1 << 16) | (g1 << 8) | b1;
				resultado.setRGB(x, y, pixelNuevo);
			}
		}
		return resultado;
	}

	public static void RGBtoCMYK(BufferedImage original, int mascara) {

		for (int x = 0; x < original.getWidth(); x++) {
			for (int y = 0; y < original.getHeight(); y++) {

				int pixel = original.getRGB(x, y);

				int r = (pixel >> 16) & mascara;
				int g = (pixel >> 8) & mascara;
				int b = pixel & mascara;

				float rf = r / 255.0f;
				float gf = g / 255.0f;
				float bf = b / 255.0f;

				float k = 1 - Math.max(rf, Math.max(gf, bf));

				float c = 0, m = 0, yellow = 0;

				if (k < 1) {
					c = (1 - rf - k) / (1 - k);
					m = (1 - gf - k) / (1 - k);
					yellow = (1 - bf - k) / (1 - k);
				}

				System.out.printf(
						"Pixel (%d,%d): C=%.2f M=%.2f Y=%.2f K=%.2f%n",
						x, y, c, m, yellow, k);
			}
		}
	}

	public static int clamp(int n) {
		return Math.max(0, Math.min(255, n));
	}

	public static void main(String[] args) {
		int mascara = 0xFF;

		try {
			// Ejercicio 1, 2 y 3
			BufferedImage buffer = ImageIO.read(new File("src/imagenes/perro.png"));

			BufferedImage buffer2 = new BufferedImage(buffer.getWidth(), buffer.getHeight(),
					BufferedImage.TYPE_INT_ARGB);

			ImageIO.write(aumentoR(buffer, buffer2, mascara), "png", new File("src/imagenes/ejercicio1.png"));
			ImageIO.write(escalaGrisesL(buffer, buffer2, mascara), "png", new File("src/imagenes/ejercicio2.png"));
			ImageIO.write(umbralizacion(buffer, buffer2, mascara, 100), "png",
					new File("src/imagenes/ejercicio3_100.png"));
			ImageIO.write(umbralizacion(buffer, buffer2, mascara, 50), "png",
					new File("src/imagenes/ejercicio3_50.png"));
			ImageIO.write(umbralizacion(buffer, buffer2, mascara, 200), "png",
					new File("src/imagenes/ejercicio3_200.png"));

			// Ejercicio 4
			float[] listaSaturaciones = { 1.2f, 0.6f, 1.0f, 0.0f };
			for (int i = 0; i < listaSaturaciones.length; i++) {
				BufferedImage res = modificarSaturacion(buffer, buffer2, listaSaturaciones[i], mascara);
				ImageIO.write(res, "png", new File("src/imagenes/ejercicio4_sat_" + listaSaturaciones[i] + ".png"));
			}
			// Ejercicio 5
			float[] listaGrados = { 60f, 150f, 360f, 0.0f };
			for (int i = 0; i < listaGrados.length; i++) {
				BufferedImage res = rotarHue(buffer, buffer2, listaGrados[i], mascara);
				ImageIO.write(res, "png",
						new File("src/imagenes/ejercicio5_RotacionHue_" + (int) listaGrados[i] + ".png"));
			}
			// Ejercicio 6
			int[] listaBrillos = { 40, -40 };
			for (int i = 0; i < listaBrillos.length; i++) {
				BufferedImage res = modificarBrillo(buffer, buffer2, listaBrillos[i], mascara);
				ImageIO.write(res, "png", new File("src/imagenes/ejercicio6_brillo_" + listaBrillos[i] + ".png"));
			}
			// Ejercicio 7
			BufferedImage res = interpolacion(buffer, buffer2, 255, 0.5f, mascara);
			ImageIO.write(res, "png", new File("src/imagenes/ejercicio7_interpolacion.png"));

			// Ejercicio 8
			BufferedImage res2 = interpolacion(buffer, buffer2, 0, 0.5f, mascara);
			ImageIO.write(res2, "png", new File("src/imagenes/ejercicio8_interpolacion.png"));

			// Ejercicio 9
			BufferedImage res3 = altoContraste(buffer, buffer2, 1.8f, mascara);
			ImageIO.write(res3, "png", new File("src/imagenes/ejercicio9_alto_contraste.png"));

			// Ejercicio 10
			RGBtoCMYK(buffer, mascara);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("error: " + e.getMessage());
		}
	}

}
