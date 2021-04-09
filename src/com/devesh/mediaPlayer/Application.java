package com.devesh.mediaPlayer;

import com.devesh.mediaPlayer.swing.MainFrame;
import com.devesh.mediaPlayer.swing.Tray;
import com.devesh.mediaPlayer.utils.Playlist;
import com.devesh.mediaPlayer.utils.SongPlayer;
import com.formdev.flatlaf.FlatDarkLaf;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Application {

	private static MainFrame frame;
	private static Tray tray;
	private static SongPlayer player;
	private static Playlist playlist;
	private static final File metaDir = new File(
			System.getProperty("user.home") + "\\appdata\\local\\PlayIt");

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
		frame.requestFocus();
		if (args.length > 0)
			if ("visible".equals(args[0]))
				frame.setVisible(true);
		
		player.addSongChangeListener(frame);

		tray = new Tray(player, playlist, frame);
		
		
		if(args.length > 1){
			File[] files = new File[args.length-1];
			for(int i = 1; i <= files.length; i++){
				files[i-1] = new File(args[i]);
			}
			frame.openMedia(files);
		}
	}


	public static void quit()
	{
		tray.shutdown();
		System.exit(0);
	}


	public static void showOpenDialog()
	{
		metaDir.mkdirs();

		File lastLoc = new File(metaDir.getPath() + "\\lastLoc.dat");

		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setMultiSelectionEnabled(true);
		fileChooser.setFileFilter(
				new FileNameExtensionFilter("Music Files", "mp3", "ppl"));
		fileChooser.removeChoosableFileFilter(
				fileChooser.getAcceptAllFileFilter());

		if (lastLoc.exists())
		{
			try
			{
				Scanner scanner = new Scanner(lastLoc);
				File file = new File(scanner.nextLine());
				if (file.exists())
					fileChooser.setCurrentDirectory(file);
			} catch (FileNotFoundException ex)
			{
				Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE,
						null, ex);
			}
		}

		int i = fileChooser.showOpenDialog(null);

		if (i == JFileChooser.APPROVE_OPTION)
			frame.openMedia(fileChooser.getSelectedFiles());

		if (lastLoc.exists())
			lastLoc.delete();
		try
		{
			lastLoc.createNewFile();
			try (BufferedWriter bw = new BufferedWriter(
					new FileWriter(lastLoc)))
			{
				bw.write(fileChooser.getCurrentDirectory().getPath());
				bw.close();
			}
		} catch (IOException ex)
		{
			Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null,
					ex);
		}
	}
}
