package com.devesh.mediaPlayer;

import com.devesh.mediaPlayer.swing.MainFrame;
import com.devesh.mediaPlayer.swing.Tray;
import com.devesh.mediaPlayer.utils.Playlist;
import com.devesh.mediaPlayer.utils.SongPlayer;
import com.formdev.flatlaf.FlatDarkLaf;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.UIManager;

public class Application {

	private static MainFrame frame;
	private static Tray tray;
	private static SongPlayer player;
	private static Playlist playlist;

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
		
		playlist = new Playlist();
		player = new SongPlayer(playlist);
		
		frame = new MainFrame(playlist, player);
		try
		{
			BufferedImage img = ImageIO
					.read(Application.class.getResource("/icon.png"));
			frame.setIconImage(img);
		} catch (IOException ex)
		{
			Logger.getLogger(Tray.class.getName()).log(Level.SEVERE, null,
					ex);
		}
		frame.setVisible(true);
		frame.requestFocus();
		
		player.addSongChangeListener(frame);
		
		tray = new Tray(player, playlist, frame);
	}


	public static void quit()
	{
		tray.shutdown();
		System.exit(0);
	}
}
