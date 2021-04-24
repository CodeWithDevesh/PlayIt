package com.devesh.mediaPlayer.listHelpers;

import com.devesh.mediaPlayer.utils.Playlist;
import com.devesh.mediaPlayer.utils.Song;
import com.devesh.mediaPlayer.utils.SongPlayer;
import com.mpatric.mp3agic.InvalidDataException;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
			JList<String> jList = (JList<String>) source;
			int a = jList.getSelectedIndex();
			if (player != null)
			{
				if (playlist.currentSong == a)
					player.next();
				if (playlist.currentSong == a)
					player.stop();
			}
			playlist.removeSong(a);
		}
	}


	@Override
	protected Transferable createTransferable(JComponent c)
	{
		JList<String> jList = (JList<String>) c;
		int index = jList.getSelectedIndex();
		return new StringSelection(playlist.getSong(index).getFile().getPath());
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
		if (!canImport(support))
			return false;

		try
		{
			Transferable t = support.getTransferable();
			List<File> files;

			if (t.isDataFlavorSupported(DataFlavor.javaFileListFlavor))
				files = ((List<File>) t
						.getTransferData(DataFlavor.javaFileListFlavor));
			else
			{
				files = new ArrayList<>();
				files.add(new File(
						(String) t.getTransferData(DataFlavor.stringFlavor)));
			}

			javax.swing.JList.DropLocation loc = (javax.swing.JList.DropLocation) support
					.getDropLocation();

			Song song;
			try
			{
				for(int i = 0 ; i < files.size() ; i++)
				{
					song = new Song(files.get(i));
					playlist.addSong(song, loc.getIndex() + i);
				}
			} catch (InvalidDataException ex)
			{
				return false;
			}

		} catch (UnsupportedFlavorException | IOException ex)
		{
			Logger.getLogger(ListItemTransferHandler.class.getName())
					.log(Level.SEVERE, null, ex);
			return false;
		}
		return true;
	}
}
