package com.devesh.mediaPlayer.Managers;

import com.devesh.mediaPlayer.Application;
import com.devesh.mediaPlayer.listHelpers.ListItemTransferHandler;
import com.devesh.mediaPlayer.listHelpers.SngListCellRenderer;
import com.devesh.mediaPlayer.swing.GalleryPanel;
import com.devesh.mediaPlayer.swing.MainFrame;
import com.devesh.mediaPlayer.utils.Playlist;
import com.devesh.mediaPlayer.utils.SongPlayer;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

public class SngListManager {

	private final JList<String> sngList;
	private final Playlist playlist = Application.getPlaylist();
	private final SongPlayer player = Application.getPlayer();
	private MainFrame frame = Application.getFrame();
	private int cursor = -1;
	private final GalleryPanel galleryPanel;

	public SngListManager() {
		sngList = new JList<>();
		sngList.setBorder(null);
		sngList.setFont(new java.awt.Font("Calibri", 0, 18));
		sngList.setModel(playlist.getListModel());
		sngList.setCellRenderer(new SngListCellRenderer(playlist));
		sngList.setDoubleBuffered(true);
		sngList.setDragEnabled(true);
		sngList.setDropMode(javax.swing.DropMode.INSERT);
		sngList.setFocusable(false);
		sngList.setTransferHandler(
				new ListItemTransferHandler(playlist, player));

		sngList.addMouseListener(new SngMouseListener());

		galleryPanel = new GalleryPanel(playlist, player);
		galleryPanel.setTransferHandler(new ListItemTransferHandler(playlist,
				player));
		galleryPanel.addListener(new SngMouseListener());
		playlist.addListener(galleryPanel);
	}


	public SngListManager(MainFrame frame) {
		sngList = new JList<>();
		sngList.setBorder(null);
		sngList.setFont(new java.awt.Font("Calibri", 0, 18));
		sngList.setModel(playlist.getListModel());
		sngList.setCellRenderer(new SngListCellRenderer(playlist));
		sngList.setDoubleBuffered(true);
		sngList.setDragEnabled(true);
		sngList.setDropMode(javax.swing.DropMode.INSERT);
		sngList.setFocusable(false);
		sngList.setTransferHandler(
				new ListItemTransferHandler(playlist, player));

		sngList.addMouseListener(new SngMouseListener());

		galleryPanel = new GalleryPanel(playlist, player);
		galleryPanel.setTransferHandler(new ListItemTransferHandler(playlist,
				player));
		galleryPanel.addListener(new SngMouseListener());
		playlist.addListener(galleryPanel);

		this.frame = frame;
	}


	public JList<String> GetList()
	{
		return sngList;
	}


	public GalleryPanel getGalleryPanel()
	{
		return galleryPanel;
	}


	private void showSngListPopup(MouseEvent evt)
	{
		int row = sngList.locationToIndex(evt.getPoint());
		if (!sngList.isSelectedIndex(row))
			sngList.setSelectedIndex(row);

		JPopupMenu popupMenu = new JPopupMenu();

		JMenuItem miRemove = new JMenuItem("Remove");
		miRemove.addActionListener((ActionEvent ev) -> {
			removeSelected();
		});

		JMenuItem miPlay = new JMenuItem("Play");
		miPlay.addActionListener((ActionEvent e) -> {
			player.play(sngList.getSelectedIndex());
			frame.updatePlayIcon();
		});
		
		JMenuItem miShuffel = new JMenuItem("Shuffel");
		miShuffel.addActionListener((ActionEvent e) -> {
			ArrayList<Integer> selectedIndices = new ArrayList<>();
			for(int i : sngList.getSelectedIndices()){
				selectedIndices.add(i);
			}
			playlist.shuffel(selectedIndices);
		});

		popupMenu.add(miPlay);
		popupMenu.add(miRemove);
		popupMenu.add(miShuffel);
		popupMenu.show(sngList, evt.getX(), evt.getY());
	}


	private void showGalleryPannelPopup(MouseEvent evt, int index)
	{
		JPopupMenu popupMenu = new JPopupMenu();

		JMenuItem miRemove = new JMenuItem("Remove");
		miRemove.addActionListener((ActionEvent ev) -> {
			removeSelected();
		});

		JMenuItem miPlay = new JMenuItem("Play");
		miPlay.addActionListener((ActionEvent e) -> {
			player.play(index);
			frame.updatePlayIcon();
		});
		
		JMenuItem miShuffel = new JMenuItem("Shuffel");
		miShuffel.addActionListener((ActionEvent e) -> {
			ArrayList<Integer> selectedIndices = new ArrayList<>();
			for(int i : galleryPanel.getSelectedIndices()){
				selectedIndices.add(i);
			}
			playlist.shuffel(selectedIndices);
		});

		popupMenu.add(miPlay);
		popupMenu.add(miRemove);
		popupMenu.add(miShuffel);
		popupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
	}


	public void removeSelected()
	{
		if ("list".equals(frame.getView()))
			while (sngList.getSelectedIndex() != -1)
			{
				int index = sngList.getSelectedIndex();
				if (playlist.currentSong == index)
				{
					player.next();
				}
				if (playlist.currentSong == index)
				{
					player.stop();
				}
				playlist.removeSong(index);
				frame.updatePlayIcon();
			}
		else
		{
			while (galleryPanel.getSelectedIndex() != -1)
			{
				int index = galleryPanel.getSelectedIndex();
				if (playlist.currentSong == index)
				{
					player.next();
				}
				if (playlist.currentSong == index)
				{
					player.stop();
				}
				playlist.removeSong(index);
				frame.updatePlayIcon();
			}
		}
	}


	public void moveUp(boolean keepSelected)
	{
		if ("list".equals(frame.getView()))
		{
			if (sngList.getSelectedIndex() == -1)
				cursor = -1;
			if (cursor > 0)
			{
				if (keepSelected)
				{
					int[] selectedInd = sngList.getSelectedIndices();
					int destInd = Arrays.binarySearch(selectedInd, cursor - 1);
					int[] newSelection;
					// check if the new cursor pos is selected
					if (destInd < 0)
					{
						// if not select it
						newSelection = Arrays.copyOf(selectedInd,
								selectedInd.length + 1);
						newSelection[newSelection.length - 1] = --cursor;
					} else
					{
						// deselect it
						selectedInd[destInd + 1] = -1;
						newSelection = selectedInd;
						cursor--;
					}
					sngList.setSelectedIndices(newSelection);
				} else
					sngList.setSelectedIndex(--cursor);
				sngList.ensureIndexIsVisible(cursor);
			}
		} else
		{
			galleryPanel.moveUp(keepSelected);
		}
	}


	public void moveRight(boolean keepSelected)
	{
		galleryPanel.moveRight(keepSelected);
	}


	public void moveLeft(boolean keepSelected)
	{
		galleryPanel.moveLeft(keepSelected);
	}


	public void nextSong(boolean ensureVisible)
	{
		player.next();
		if (ensureVisible)
		{
			if ("list".equals(frame.getView()))
				sngList.ensureIndexIsVisible(playlist.currentSong);
			else
				galleryPanel.ensureVisible(playlist.currentSong);
		}
	}


	public void previousSong(boolean ensureVisible)
	{
		player.previous();
		if (ensureVisible)
			sngList.ensureIndexIsVisible(playlist.currentSong);
	}


	public void playSelected()
	{
		int index;

		if ("list".equals(frame.getView()))
			index = sngList.getSelectedIndex();
		else
			index = galleryPanel.getSelectedIndex();

		if (index != -1)
			player.play(index);
	}


	public void moveDown(boolean keepSelected)
	{
		if ("list".equals(frame.getView()))
		{
			if (sngList.getSelectedIndex() == -1)
				cursor = -1;
			if (cursor != sngList.getModel().getSize() - 1)
			{
				if (keepSelected)
				{
					int[] selectedInd = sngList.getSelectedIndices();
					int destInd = Arrays.binarySearch(selectedInd, cursor + 1);
					int[] newSelection;
					// check if the new cursor pos is selected
					if (destInd < 0)
					{
						// if not select it
						newSelection = Arrays.copyOf(selectedInd,
								selectedInd.length + 1);
						newSelection[newSelection.length - 1] = ++cursor;
					} else
					{
						// deselect it
						selectedInd[destInd - 1] = -1;
						newSelection = selectedInd;
						cursor++;
					}
					sngList.setSelectedIndices(newSelection);
				} else
					sngList.setSelectedIndex(++cursor);
				sngList.ensureIndexIsVisible(cursor);
			}
		} else
			galleryPanel.moveDown(keepSelected);
	}

	private class SngMouseListener
			implements MouseListener, GalleryPanel.GalleryListener {

		@Override
		public void mouseClicked(MouseEvent evt)
		{
			if (evt.getClickCount() == 2
					&& evt.getButton() == MouseEvent.BUTTON1)
			{
				int index = sngList.getSelectedIndex();
				if (index != -1)
					player.play(index);
			} else if (SwingUtilities.isRightMouseButton(evt))
			{
				showSngListPopup(evt);
			}

			if (!SwingUtilities.isMiddleMouseButton(evt))
			{
				cursor = sngList.locationToIndex(evt.getPoint());
			}
		}


		@Override
		public void doubleClicked(MouseEvent e, int index)
		{
			if (index != -1)
				player.play(index);
		}


		@Override
		public void rightClicked(MouseEvent e, int index)
		{
			showGalleryPannelPopup(e, index);
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
	}
}
