package com.devesh.mediaPlayer.swing;

import com.devesh.mediaPlayer.Application;
import com.devesh.mediaPlayer.utils.Playlist;
import com.devesh.mediaPlayer.utils.SongPlayer;
import dorkbox.systemTray.MenuItem;
import dorkbox.systemTray.SystemTray;
import java.awt.event.ActionEvent;
import javax.swing.JSeparator;

public class Tray {

	private final SystemTray tray;

	public Tray(SongPlayer player, Playlist playlist, MainFrame frame) {
		SystemTray.AUTO_SIZE = true;

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
			player.next();
		}));

		tray.getMenu().add(new MenuItem("Previous", (ActionEvent e) -> {
			player.previous();
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
			Application.quit();
		}));
	}


	public void shutdown()
	{
		tray.shutdown();
	}
}
