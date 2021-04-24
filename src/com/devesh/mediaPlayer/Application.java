package com.devesh.mediaPlayer;

import com.devesh.mediaPlayer.RMI.OpenRMI;
import com.devesh.mediaPlayer.httpServer.Server;
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
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFprobe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application implements OpenRMI {

	private static MainFrame frame;
	private static Tray tray;
	private static SongPlayer player;
	private static Playlist playlist;
	private static final String RMI_ENTRY = "PlayItApplication";
	private static final Application app = new Application();
	public static final File metaDir = new File(
			System.getProperty("user.home") + "\\appdata\\local\\PlayIt");
	public static File currentDir;
	public static FFmpeg ffmpeg;
	public static FFprobe ffprobe;

	private static final int RMIPort = 2023, httpPort = 2021;
	public static Logger logger;

	public static void main(String[] args)
	{
		try
		{
			initRMI(args);
		} catch (RemoteException | NotBoundException ex)
		{
			ex.printStackTrace();
			System.exit(-1);
		}

		new Server(httpPort);

		logger = LoggerFactory.getLogger(Application.class);
		initCurrnetDir();

		try
		{
			ffmpeg = new FFmpeg("ffmpeg.exe");
			ffprobe = new FFprobe("ffprobe.exe");
		} catch (IOException ex)
		{
			java.util.logging.Logger.getLogger(Application.class.getName())
					.log(Level.SEVERE, null, ex);
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
			logger.error(null, ex);
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
			registry = LocateRegistry.createRegistry(RMIPort);
		} catch (RemoteException ex)
		{
			registry = LocateRegistry.getRegistry(RMIPort);
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

		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setMultiSelectionEnabled(true);
		fileChooser.setFileFilter(
				new FileNameExtensionFilter("Music Files", "mp3", "ppl"));
		// fileChooser.removeChoosableFileFilter(
		// fileChooser.getAcceptAllFileFilter());
		fileChooser.setCurrentDirectory(currentDir);
		int i = fileChooser.showOpenDialog(null);
		
		updateCurrentDir(fileChooser.getCurrentDirectory());
		
		if (i == JFileChooser.APPROVE_OPTION)
			return fileChooser.getSelectedFiles();
		return null;
	}


	private static void initCurrnetDir()
	{
		File lastLoc = new File(metaDir.getPath() + "\\lastLoc.dat");
		if (lastLoc.exists())
		{
			try
			{
				try (Scanner scanner = new Scanner(lastLoc))
				{
					File file = new File(scanner.nextLine());
					if (file.exists())
						currentDir = file;
				}
			} catch (FileNotFoundException ex)
			{
				logger.error(null, ex);
			}
		}
	}


	public static void updateCurrentDir(File file)
	{
		File lastLoc = new File(metaDir.getPath() + "\\lastLoc.dat");

		if (lastLoc.exists())
			lastLoc.delete();
		try
		{
			lastLoc.createNewFile();
			try (BufferedWriter bw = new BufferedWriter(
					new FileWriter(lastLoc)))
			{
				bw.write(file.getPath());
				currentDir = file;
				bw.close();
			}
		} catch (IOException ex)
		{
			logger.error(null, ex);
		}
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
				files[i] = new File(args[i]);
			}
			frame.openMedia(files);
			player.play(playlist.size() - 1);
		} else
		{
			frame.setVisible(true);
			frame.requestFocus();
			frame.toFront();
		}
	}


	public static void open(String file)
	{
		if (file != null && !file.isBlank())
		{
			File[] files = new File[1];
			files[0] = new File(file);
			frame.openMedia(files);
			player.play(playlist.size() - 1);
		} else
		{
			frame.setVisible(true);
			frame.requestFocus();
			frame.toFront();
		}
	}


	public static void setPlaylist(Playlist playlist)
	{
		playlist.currentSong = 0;
		player.stop();
		Application.playlist = playlist;
		player.changePlaylist(playlist);
		frame.setPlaylist(playlist);
		player.play();
	}
}
