package com.devesh.mediaPlayer.listHelpers;

import com.devesh.mediaPlayer.Playlist;
import com.devesh.mediaPlayer.Song;
import com.devesh.mediaPlayer.SongPlayer;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
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
			int[] x = jList.getSelectedIndices();
			for(int a : x)
			{
				if (playlist.currentSong == a)
					player.next();
				if (playlist.currentSong == a)
					player.stop();
				playlist.removeSong(a);
			}
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
			File file;
			
			if (t.isDataFlavorSupported(DataFlavor.javaFileListFlavor))
				file = ((List<File>) t
						.getTransferData(DataFlavor.javaFileListFlavor)).get(0);
			else
				file = new File(
						(String) t.getTransferData(DataFlavor.stringFlavor));
			
			javax.swing.JList.DropLocation loc = (javax.swing.JList.DropLocation) support
					.getDropLocation();
			
			Song song;
			try{
				song = new Song(file);
			} catch (InvalidDataException | UnsupportedTagException ex) {
				return false;
			}
			
			playlist.addSong(song, loc.getIndex());
		} catch (UnsupportedFlavorException | IOException ex)
		{
			Logger.getLogger(ListItemTransferHandler.class.getName())
					.log(Level.SEVERE, null, ex);
			return false;
		}
		return true;
	}
}
