package org.listeners;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import org.gui.AppScreen;

public class MenuSaveImageListener implements MenuListener {

	private AppScreen ap;

	public MenuSaveImageListener(AppScreen ap) {
		this.ap = ap;
	}

	@Override
	public void menuCanceled(MenuEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void menuDeselected(MenuEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void menuSelected(MenuEvent e) {
		final JFileChooser fc = new JFileChooser();
		int returnVal = fc.showSaveDialog(ap);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			try {
				ImageIO.write(ap.getImage(), "jpg", file);
			} catch (IOException e1) {
				System.out.println("Cannot save image");
				e1.printStackTrace();
			}
		}
	}

}
