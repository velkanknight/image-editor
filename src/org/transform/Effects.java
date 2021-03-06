package org.transform;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

public class Effects {

	private BufferedImage original;

	public Effects(BufferedImage original) {
		super();
		this.original = original;
	}

	public BufferedImage grayImage() {
		int w1 = this.original.getWidth();
		int h1 = this.original.getHeight();

		BufferedImage gray = new BufferedImage(w1, h1, 1);
		int value, alpha, r, g, b;
		for (int i = 0; i < w1; i++) {
			for (int j = 0; j < h1; j++) {
				value = this.original.getRGB(i, j);
				alpha = getAlpha(value);
				r = getRed(value);
				g = getGreen(value);
				b = getBlue(value);

				value = (r + g + b) / 3;
				r = g = b = value;
				value = createRgb(alpha, r, g, b);
				gray.setRGB(i, j, value);
			}
		}
		return gray;
	}

	private int createRgb(int alpha, int r, int g, int b) {
		int rgb = (alpha << 24) + (r << 16) + (g << 8) + b;
		return rgb;
	}

	private int getAlpha(int rgb) {
		return (rgb >> 24) & 0xFF;
	}

	private int getRed(int rgb) {
		return (rgb >> 16) & 0xFF;
	}

	private int getGreen(int rgb) {
		return (rgb >> 8) & 0xFF;
	}

	private int getBlue(int rgb) {
		return rgb & 0xFF;
	}

	private static BufferedImage deepCopy(BufferedImage bi) {
		ColorModel cm = bi.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = bi.copyData(null);
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}

	public BufferedImage oldImage(int sepiaIntensity) {
		int sepiaDepth = 20;

		BufferedImage old = deepCopy(this.original);
		int w = old.getWidth();
		int h = old.getHeight();

		WritableRaster raster = old.getRaster();

		int[] pixels = new int[w * h * 3];
		raster.getPixels(0, 0, w, h, pixels);

		for (int i = 0; i < pixels.length; i += 3) {
			int r = pixels[i];
			int g = pixels[i + 1];
			int b = pixels[i + 2];

			int gry = (r + g + b) / 3;
			r = g = b = gry;
			r = r + (sepiaDepth * 2);
			g = g + sepiaDepth;

			if (r > 255)
				r = 255;
			if (g > 255)
				g = 255;
			if (b > 255)
				b = 255;

			// cor azul escuro para aumentar o efeito sepia
			b -= sepiaIntensity;

			// deixando as cores normalizadas
			if (b < 0)
				b = 0;
			if (b > 255)
				b = 255;

			pixels[i] = r;
			pixels[i + 1] = g;
			pixels[i + 2] = b;
		}
		raster.setPixels(0, 0, w, h, pixels);
		return old;
	}

	public BufferedImage negativeImage() {

		int w1 = this.original.getWidth();
		int h1 = this.original.getHeight();

		BufferedImage negative = new BufferedImage(w1, h1, 1);
		int value, alpha, r, g, b;
		for (int i = 0; i < w1; i++) {
			for (int j = 0; j < h1; j++) {
				value = this.original.getRGB(i, j);
				alpha = getAlpha(value);
				r = 255 - getRed(value);
				g = 255 - getGreen(value);
				b = 255 - getBlue(value);

				value = createRgb(alpha, r, g, b);
				negative.setRGB(i, j, value);
			}
		}
		return negative;
	}

}
