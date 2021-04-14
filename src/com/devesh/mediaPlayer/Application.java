package com.devesh.mediaPlayer;

import com.devesh.mediaPlayer.RMI.OpenRMI;
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
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Application implements OpenRMI {

	private static MainFrame frame;
	private static Tray tray;
	private static SongPlayer player;
	private static Playlist playlist;
	private static final String RMI_ENTRY = "PlayItApplication";
	private static final Application app = new Application();
	public static final File metaDir = new File(
			System.getProperty("user.home") + "\\appdata\\local\\PlayIt");

	public static void main(String[] args)
	{
		try {
			initRMI(args);
		} catch (RemoteException | NotBoundException ex) {
			ex.printStackTrace();
			System.exit(-1);
		}
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

		player.addSongChangeListener(frame);

		tray = new Tray(player, playlist, frame);

		if (args.length > 0)
		{
			File[] files = new File[args.length];
			for(int i = 0 ; i < files.length ; i++)
			{
				System.out.println(args[i]);
				files[i] = new File(args[i]);
			}
			frame.openMedia(files);
		} else
			frame.setVisible(true);
	}


	private static void initRMI(String[] args)
			throws RemoteException, NotBoundException
	{
		Registry registry;
		try
		{
			registry = LocateRegistry.createRegistry(2020);
		} catch (RemoteException ex)
		{
			registry = LocateRegistry.getRegistry(2020);
		}
		
		try
		{
			OpenRMI openRMI = (OpenRMI) UnicastRemoteObject.exportObject(app,
					0);
			registry.bind(RMI_ENTRY, openRMI);
		} catch (AlreadyBoundException ex)
		{
			OpenRMI openRMI1 = (OpenRMI) registry.lookup(RMI_ENTRY);
			openRMI1.open(args);
			System.exit(0);
		}
	}


	public static void quit()
	{
		tray.shutdown();
		System.exit(0);
	}


	public static File[] showOpenDialog()
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
		if (i == JFileChooser.APPROVE_OPTION)
			return fileChooser.getSelectedFiles();
		return null;
	}


	public static Playlist getPlaylist()
	{
		return playlist;
	}


	@Override
	public void open(String[] args) throws RemoteException
	{
		if (args.length > 0)
		{
			File[] files = new File[args.length];
			for(int i = 0 ; i < files.length ; i++)
			{
				System.out.println(args[i]);
				files[i] = new File(args[i]);
			}
			frame.openMedia(files);
			player.play(playlist.size()-1);
		} else
		{
			frame.setVisible(true);
			frame.toFront();
		}
	}
	
	public static void setPlaylist(Playlist playlist){
		playlist.currentSong = 0;
		player.stop();
		Application.playlist = playlist;
		player.changePlaylist(playlist);
		frame.setPlaylist(playlist);
		player.play();
	}
}
