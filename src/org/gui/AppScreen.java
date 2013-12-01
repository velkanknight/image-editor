package org.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.WindowConstants;

import org.listeners.CropListener;
import org.listeners.MenuLoadImageListener;
import org.listeners.MenuSaveImageListener;
import org.listeners.RotClockImageListener;
import org.listeners.RotCounterImageListener;
import org.listeners.effects.BrightnessChangedListener;
import org.listeners.effects.FrameImageListener;
import org.listeners.effects.GrayImageListener;
import org.listeners.effects.NegativeImageListener;
import org.listeners.effects.OldImageListener;
import org.listeners.effects.ToggleCropListener;
import org.transform.ImageTransform;

public class AppScreen extends JFrame {

	private static final long serialVersionUID = 1L;

	private ImagePanel imagePanel;
	private LeftPanel leftPanel;

	private boolean cropping;

	public AppScreen() {
		initEverything();
		setTitle("Image Editor CCI36");

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				System.exit(0);
			}
		});
	}

	private void initEverything() {
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(800, 600));
		BufferedImage original = readImage("tnt.jpg");
		this.imagePanel = new ImagePanel(original);

		getContentPane().add(this.imagePanel);

		JMenuBar blueMenuBar = new JMenuBar();
		JMenu file = new JMenu("Arquivo");
		JMenuItem loadImage = new JMenuItem("Carregar Imagem");
		loadImage.addActionListener(new MenuLoadImageListener(this));
		JMenuItem saveImage = new JMenuItem("Salvar Imagem");
		saveImage.addActionListener(new MenuSaveImageListener(this));
		file.add(loadImage);
		file.add(saveImage);
		blueMenuBar.add(file);
		blueMenuBar.setOpaque(true);
		blueMenuBar.setBackground(new Color(43, 43, 43));
		blueMenuBar.setPreferredSize(new Dimension(200, 20));
		setJMenuBar(blueMenuBar);

		CropListener cl = new CropListener(this, this.imagePanel);

		this.imagePanel.addMouseListener(cl);
		this.imagePanel.addMouseMotionListener(cl);

		this.leftPanel = new LeftPanel();
		addActionsToButtons();
		add(this.leftPanel, BorderLayout.WEST);

		pack();
		setVisible(true);
	}

	private void addActionsToButtons() {
		this.leftPanel.getGrayButton().addActionListener(
				new GrayImageListener(this));
		this.leftPanel.getOldButton().addActionListener(
				new OldImageListener(this));
		this.leftPanel.getNegativeButton().addActionListener(
				new NegativeImageListener(this));
		this.leftPanel.getFrameButton().addActionListener(
				new FrameImageListener(this));
		this.leftPanel.getRotateClockwise().addActionListener(
				new RotClockImageListener(this));
		this.leftPanel.getRotateCounterClock().addActionListener(
				new RotCounterImageListener(this));
		this.leftPanel.getBrightnessSlider().addChangeListener(
				new BrightnessChangedListener(this));
		this.leftPanel.getToggleCrop().addActionListener(
				new ToggleCropListener(this, this.leftPanel.getToggleCrop()));
	}

	private BufferedImage readImage(String name) {
		File input = new File(name);
		try {
			return imageFromFile(input);
		} catch (IOException e) {
			System.out.println("Não foi possível ler a imagem com nome: "
					+ name);
			e.printStackTrace();
		}
		return null;
	}

	private BufferedImage imageFromFile(File f) throws IOException {
		return ImageIO.read(f);
	}

	public void setImage(File f) {
		try {
			this.imagePanel.setImage(imageFromFile(f));
			this.imagePanel.repaint();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setImage(BufferedImage img) {
		this.imagePanel.setImage(img);
		this.imagePanel.repaint();
	}

	public BufferedImage getImage() {
		return imagePanel.getImage();
	}

	public boolean isCropping() {
		return cropping;
	}

	public void crop(int x1, int y1, int width, int height) {
		System.out.println("Tentando: " + x1 + "-" + y1 + "/" + width + "-"
				+ height);
		System.out.println("Tamanho da imagem: " + getImage().getWidth() + "-"
				+ getImage().getHeight());
		BufferedImage cropped = ImageTransform.cropImage(getImage(), x1, y1,
				width, height);
		setImage(cropped);
		System.out.println("Cropping");
	}

	public void toggleCropping() {
		cropping = !cropping;
	}

}
