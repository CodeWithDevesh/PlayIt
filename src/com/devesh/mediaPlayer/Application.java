package com.devesh.mediaPlayer;

import com.devesh.mediaPlayer.Managers.NativeKeyManger;
import com.devesh.mediaPlayer.RMI.OpenRMI;
import com.devesh.mediaPlayer.converter.ConversionListener;
import com.devesh.mediaPlayer.httpServer.Server;
import com.devesh.mediaPlayer.swing.MainFrame;
import com.devesh.mediaPlayer.swing.Tray;
import com.devesh.mediaPlayer.utils.Playlist;
import com.devesh.mediaPlayer.utils.SongPlayer;
import com.formdev.flatlaf.FlatDarkLaf;
import java.awt.Frame;
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
import java.util.prefs.Preferences;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFprobe;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.dispatcher.SwingDispatchService;
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
	public static final Preferences prefs = Preferences
			.userNodeForPackage(Application.class);
	public static File currentDir;
	public static FFmpeg ffmpeg;
	public static FFprobe ffprobe;

	private static final int RMIPort = 2022, httpPort = 2021;
	public static Logger logger;

	private static boolean playLast = false;

	public static void main(String[] args)
	{
		Settings.init();
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
			if ("Dark".equals(Settings.getTheme()))
				UIManager.setLookAndFeel(new FlatDarkLaf());
			else
				UIManager.setLookAndFeel(new NimbusLookAndFeel());
		} catch (Exception ex)
		{
			java.util.logging.Logger.getLogger(MainFrame.class.getName())
					.log(java.util.logging.Level.SEVERE, null, ex);
		}

		playlist = new Playlist();
		player = new SongPlayer(playlist);

		SwingUtilities.invokeLater(() -> {
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

			try
			{
				java.util.logging.Logger natlogger = java.util.logging.Logger
						.getLogger(GlobalScreen.class.getPackage().getName());
				natlogger.setLevel(Level.OFF);
				natlogger.setUseParentHandlers(false);
				GlobalScreen.setEventDispatcher(new SwingDispatchService());
				GlobalScreen.registerNativeHook();
				GlobalScreen.addNativeKeyListener(new NativeKeyManger());
			} catch (NativeHookException ex)
			{
				logger.error(null, ex);
			}
		});

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
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setMultiSelectionEnabled(true);
		fileChooser.setFileFilter(
				new FileNameExtensionFilter("Music Files", "mp3", "ppl"));
		fileChooser.setCurrentDirectory(currentDir);
		int i = fileChooser.showOpenDialog(null);

		updateCurrentDir(fileChooser.getCurrentDirectory());

		if (i == JFileChooser.APPROVE_OPTION)
			return fileChooser.getSelectedFiles();
		return null;
	}


	private static void initCurrnetDir()
	{
		metaDir.mkdirs();
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
		} else
			currentDir = new File(System.getProperty("user.home"));
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
			playLast = true;
		} else
		{
			showWindow();
		}
	}


	public static void open(String file)
	{
		if (file != null && !file.isBlank())
		{
			File[] files = new File[1];
			files[0] = new File(file);
			frame.openMedia(files);
			playLast = true;
		} else
		{
			showWindow();
		}
	}


	public static void opened()
	{
		if (playLast)
		{
			if (playlist.size() != 0)
				player.play(playlist.size() - 1);
			playLast = false;
		}
	}

	public static final ConversionListener CONVERSION_LISTENER = new ConversionListener() {
		@Override
		public void stoped(boolean completed, File file)
		{
			if (completed == true && file != null)
			{
				File[] files = new File[1];
				files[0] = file;
				frame.openMedia(files);
			}
		}
	};

	public static Tray getTray()
	{
		return tray;
	}


	public static MainFrame getFrame()
	{
		return frame;
	}


	public static SongPlayer getPlayer()
	{
		return player;
	}


	public static Playlist getPlaylist()
	{
		return playlist;
	}


	public static void showWindow()
	{
		frame.setVisible(true);
		frame.requestFocus();
		frame.toFront();
		frame.setExtendedState(Frame.MAXIMIZED_BOTH);
	}
}
