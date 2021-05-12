package com.devesh.mediaPlayer.swing;

import com.devesh.mediaPlayer.Application;
import static com.devesh.mediaPlayer.Application.logger;
import com.devesh.mediaPlayer.Settings;
import com.devesh.mediaPlayer.utils.Playlist;
import com.devesh.mediaPlayer.utils.SongPlayer;
import java.awt.AWTException;
import java.awt.Frame;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.Timer;

public final class Tray {

	private final SystemTray tray;
	private final TrayIcon icon;
	private final SongPlayer player;
	private final Playlist playlist;

	private int clicks;

	public Tray(SongPlayer player, Playlist playlist, MainFrame frame) {

		if (!SystemTray.isSupported())
		{
			JOptionPane.showMessageDialog(null,
					"System tray is not supported on your system", "Error",
					JOptionPane.ERROR_MESSAGE);
			System.exit(-1);
		}

		tray = SystemTray.getSystemTray();
		this.player = player;
		this.playlist = playlist;

		if (tray == null)
		{
			System.exit(-1);
		}
		icon = new TrayIcon(
				new ImageIcon(getClass().getResource("/icon non-trans.png"))
						.getImage());
		icon.setImageAutoSize(true);
		try
		{
			tray.add(icon);
		} catch (AWTException ex)
		{
			logger.error(null, ex);
		}

		PopupMenu menu = new PopupMenu();

		MenuItem miShowHide = new MenuItem("Show/Hide window");
		miShowHide.addActionListener((var e) -> {
			if (frame.isVisible())
				frame.setVisible(false);
			else
			{
				frame.setVisible(true);
				frame.toFront();
				frame.requestFocus();
				frame.setExtendedState(Frame.NORMAL);
			}
		});
		menu.add(miShowHide);
		menu.addSeparator();

		MenuItem miPlayPause = new MenuItem("Play/Pause");
		miPlayPause.addActionListener((ActionEvent e) -> {
			frame.play();
		});
		menu.add(miPlayPause);

		MenuItem miNext = new MenuItem("Next");
		miNext.addActionListener((ActionEvent e) -> {
			player.next();
		});
		menu.add(miNext);

		MenuItem miPrevious = new MenuItem("Previous");
		miPrevious.addActionListener((ActionEvent e) -> {
			player.previous();
		});
		menu.add(miPrevious);
		menu.addSeparator();

		MenuItem miOpen = new MenuItem("Open");
		miOpen.addActionListener((ActionEvent e) -> {
			File[] files = Application.showOpenDialog();
			frame.openMedia(files);
		});
		menu.add(miOpen);

		MenuItem miShuffel = new MenuItem("Shuffel");
		miShuffel.addActionListener((ActionEvent e) -> {
			frame.shuffel();
		});
		menu.add(miShuffel);

		MenuItem miSave = new MenuItem("Save");
		miSave.addActionListener((ActionEvent e) -> {
			frame.save();
		});
		menu.add(miSave);
		menu.addSeparator();

		MenuItem miQuit = new MenuItem("Quit");
		miQuit.addActionListener((ActionEvent e) -> {
			Application.quit();
		});
		menu.add(miQuit);

		icon.setPopupMenu(menu);

		player.addSongChangeListener(() -> {
			if (Settings.isShowNotification())
			{
				icon.displayMessage(null, playlist.getCurrentSong().getTitle(),
						TrayIcon.MessageType.NONE);
				updateTooltip();
			}
		});

		icon.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e)
			{
				if (e.getButton() == MouseEvent.BUTTON1)
				{
					if (e.getClickCount() == 2)
					{
						clicks = 2;
						frame.setVisible(!frame.isVisible());
						frame.requestFocus();
						frame.toFront();
						frame.setExtendedState(Frame.NORMAL);
					}

					if (e.getClickCount() == 1)
					{
						clicks = 1;
						Timer timer = new Timer(500, (ActionEvent evt) -> {
							if (clicks == 1)
								frame.play();
						});
						timer.setRepeats(false);
						timer.start();
					}
				}
			}


			@Override
			public void mousePressed(MouseEvent e)
			{
			}


			@Override
			public void mouseReleased(MouseEvent e)
			{
			}


			@Override
			public void mouseEntered(MouseEvent e)
			{
			}


			@Override
			public void mouseExited(MouseEvent e)
			{
			}
		});

		updateTooltip();
	}


	public void shutdown()
	{
		tray.remove(icon);
	}


	public void updateTooltip()
	{
		if (player.status != SongPlayer.STOPED
				&& playlist.getCurrentSong() != null)
			icon.setToolTip(playlist.getCurrentSong().getTitle());
		else
			icon.setToolTip("no song playing");
	}
}
