package com.devesh.mediaPlayer.listHelpers;

import com.devesh.mediaPlayer.Application;
import static com.devesh.mediaPlayer.Application.logger;
import com.devesh.mediaPlayer.utils.Playlist;
import com.devesh.mediaPlayer.utils.Song;
import com.devesh.mediaPlayer.utils.SongPlayer;
import com.mpatric.mp3agic.InvalidDataException;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.TransferHandler;

public class ListItemTransferHandler extends TransferHandler {
	private final Playlist playlist;
	private final SongPlayer player;

	public ListItemTransferHandler(Playlist playlist, SongPlayer player) {
		this.playlist = playlist;
		this.player = player;
	}


	@Override
	protected void exportDone(JComponent source, Transferable data, int action)
	{
		if (action == MOVE)
		{
			if (source instanceof JList)
			{
				JList<String> jList = (JList<String>) source;
				// int a = jList.getSelectedIndex();
				// if (player != null)
				// {
				// if (playlist.currentSong == a)
				// player.next();
				// if (playlist.currentSong == a)
				// player.stop();
				// }
				// playlist.removeSong(a);
				if (player != null)
				{
					int index = jList.getSelectedIndex();
					while(index != -1)
					{
						if (playlist.currentSong == index)
							player.next();
						if (playlist.currentSong == index)
							player.stop();
						System.out.println(index);
						playlist.removeSong(index);
						index = jList.getSelectedIndex();
					}
				}
			}
		}
	}


	@Override
	protected Transferable createTransferable(JComponent c)
	{
		if (c instanceof JList)
		{
			File file = new File(
					Application.metaDir.getPath() + "\\tempPlay.ppl");
			if (file.exists())
				file.delete();
			try
			{
				JList<String> jList = (JList<String>) c;
				try ( // int index = jList.getSelectedIndex();
						// return new StringSelection(
						// playlist.getSong(index).getFile().getPath());
						BufferedWriter writer = new BufferedWriter(
								new FileWriter(file)))
				{
					for(int index : jList.getSelectedIndices())
					{
						writer.append(Integer.toString(index));
						writer.newLine();
					}
				}
				return new StringSelection(file.getPath());
			} catch (IOException ex)
			{
				logger.error(null, ex);
			}
		}
		return null;
	}


	@Override
	public int getSourceActions(JComponent c)
	{
		return MOVE;
	}


	@Override
	public boolean canImport(TransferSupport info)
	{
		return info.isDataFlavorSupported(DataFlavor.stringFlavor)
				|| info.isDataFlavorSupported(
						DataFlavor.javaFileListFlavor);
	}


	@Override
	public boolean importData(TransferSupport support)
	{
		try
		{
			if (!canImport(support))
				return false;

			Transferable t = support.getTransferable();
			List<File> files;
			javax.swing.JList.DropLocation loc = (javax.swing.JList.DropLocation) support
					.getDropLocation();

			if (t.isDataFlavorSupported(DataFlavor.javaFileListFlavor))
			{
				files = ((List<File>) t
						.getTransferData(DataFlavor.javaFileListFlavor));

				Thread thread = new Thread(() -> {
					int i = 0;
					for(File file : files)
					{
						String filename = file.getPath();
						if (filename.endsWith(".ppl"))
						{
							try
							{
								Scanner scanner = new Scanner(file);
								while (scanner.hasNext())
								{
									playlist.addSong(
											new Song(
													new File(scanner.nextLine()
															.replace(
																	"\n", ""))),
											loc.getIndex() + i);
									i++;
								}
							} catch (InvalidDataException | IOException ex)
							{
								logger.error("Error while opening playlist",
										ex);
							}
						} else
						{
							try
							{
								playlist.addSong(new Song(file),
										loc.getIndex() + i);
								i++;
							} catch (InvalidDataException | IOException ex)
							{
								logger.error(
										"Error while opening file: "
												+ file.getPath(),
										ex);
							}
						}
					}
					Application.opened();
				});
				thread.start();
			} else
			{
				File file = new File(
						(String) t.getTransferData(DataFlavor.stringFlavor));
				if (!file.exists())
					return false;
				try (Scanner scanner = new Scanner(file))
				{
					LinkedList<Integer> indices = new LinkedList<>();
					while (scanner.hasNextInt())
					{
						indices.add(scanner.nextInt());
					}
					LinkedList<Song> songs = new LinkedList<>();
					indices.forEach(index -> {
						songs.add(playlist.getSong(index));
					});
					Song song;
					for(int i = 0 ; i < indices.size() ; i++)
					{
						song = songs.get(i);
						System.out.println(song.getTitle());
						playlist.addSong(song, i + loc.getIndex());
					}
				}
			}
			return true;
		} catch (UnsupportedFlavorException | IOException ex)
		{
			Logger.getLogger(ListItemTransferHandler.class.getName())
					.log(Level.SEVERE, null, ex);
		}
		return true;
	}
}