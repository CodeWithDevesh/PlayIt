package com.devesh.mediaPlayer.swing;

import com.formdev.flatlaf.FlatDarkLaf;
import dorkbox.systemTray.MenuItem;
import dorkbox.systemTray.SystemTray;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JSeparator;
import javax.swing.UIManager;

public class Tray {

	private final SystemTray tray;
	private MainFrame frame;

	public static void main(String[] args)
	{
		try
		{
			UIManager.setLookAndFeel(new FlatDarkLaf());
		} catch (Exception ex)
		{
			java.util.logging.Logger.getLogger(MainFrame.class.getName())
					.log(java.util.logging.Level.SEVERE, null, ex);
		}

		SystemTray.AUTO_SIZE = true;

		new Tray();
	}


	public Tray() {
		java.awt.EventQueue.invokeLater(() -> {
			frame = new MainFrame(this);
			try
			{
				BufferedImage img = ImageIO
						.read(getClass().getResource("/icon.png"));
				frame.setIconImage(img);
			} catch (IOException ex)
			{
				Logger.getLogger(Tray.class.getName()).log(Level.SEVERE, null,
						ex);
			}
			frame.setVisible(true);
			frame.requestFocus();
			MainFrame.player.addSongChangeListener(frame);
		});

		tray = SystemTray.get();

		if (tray == null)
		{
			System.exit(-1);
		}
		tray.setImage(getClass().getResource("/icon.png"));

		tray.getMenu()
				.add(new MenuItem("Show/Hide window", (ActionEvent e) -> {
					if (frame.isVisible())
						frame.setVisible(false);
					else
					{
						frame.setVisible(true);
						frame.requestFocus();
					}
				}));

		tray.getMenu().add(new JSeparator());

		tray.getMenu().add(new MenuItem("Play/Pause", (ActionEvent e) -> {
			frame.play();
		}));

		tray.getMenu().add(new MenuItem("Next", (ActionEvent e) -> {
			MainFrame.player.next();
		}));

		tray.getMenu().add(new MenuItem("Previous", (ActionEvent e) -> {
			MainFrame.player.previous();
		}));

		tray.getMenu().add(new JSeparator());

		tray.getMenu().add(new MenuItem("Open", (ActionEvent e) -> {
			frame.openMedia();
		}));

		tray.getMenu().add(new MenuItem("Shuffel", (ActionEvent e) -> {
			frame.shuffel();
		}));

		tray.getMenu().add(new MenuItem("Save", (ActionEvent e) -> {
			frame.save();
		}));

		tray.getMenu().add(new JSeparator());

		tray.getMenu().add(new MenuItem("Quit", (ActionEvent e) -> {
			quit();
		}));
	}


	public void quit()
	{
		tray.shutdown();
		System.exit(0);
	}
}
