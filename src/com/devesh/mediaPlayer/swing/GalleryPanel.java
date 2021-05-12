package com.devesh.mediaPlayer.swing;

import com.devesh.mediaPlayer.Application;
import com.devesh.mediaPlayer.listeners.PlayListListener;
import com.devesh.mediaPlayer.utils.Playlist;
import com.devesh.mediaPlayer.utils.Song;
import com.devesh.mediaPlayer.utils.SongPlayer;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;
import javax.swing.JPanel;

public class GalleryPanel extends JPanel implements PlayListListener,
		MouseListener {

	private final Playlist playlist;
	private final ArrayList<GalleryLabel> lables;
	private final WrapLayout layout = new WrapLayout(FlowLayout.LEADING, 5, 5);
	private final TreeSet<Integer> selectedIndices;
	private boolean isActivated = false;
	private final LinkedList<GalleryListener> listeners;
	private int cursor = -1;

	public GalleryPanel(Playlist playlist, SongPlayer player) {
		this.playlist = playlist;
		selectedIndices = new TreeSet<>();
		lables = new ArrayList<>();
		listeners = new LinkedList<>();
		setLayout(layout);

		calculate();
	}


	private void calculate()
	{
		if (isActivated)
		{
			lables.clear();
			removeAll();
			setLayout(layout);
			List<Song> songs = playlist.getPlayList();
			songs.forEach(song -> {
				GalleryLabel lable = new GalleryLabel(song.getTitle(),
						song.getImage(), new Dimension(200, 200));
				lable.addMouseListener(lable);
				lable.addMouseListener(this);
				lables.add(lable);
				add(lable);
			});
			revalidate();
		}
	}


	@Override
	public void sngAdded(int index)
	{
		if (isActivated)
		{
			Song song = playlist.getSong(index);
			GalleryLabel lable = new GalleryLabel(song.getTitle(),
					song.getImage(), new Dimension(200, 200));
			lable.addMouseListener(lable);
			lable.addMouseListener(this);
			lables.add(index, lable);
			add(lable, index);
			revalidate();
		}
	}


	@Override
	public void sngRemoved(int index)
	{
		if (isActivated)
		{
			selectedIndices.clear();
			this.remove(lables.get(index));
			lables.remove(index);
			for(int i = 0 ; i < lables.size() ; i++)
			{
				GalleryLabel label = lables.get(i);
				if (label.isSelected())
				{
					selectedIndices.add(i);
				}
			}
			revalidate();
			repaint();
		}
	}


	@Override
	public void mouseClicked(MouseEvent e)
	{
		if (!isActivated)
			return;
		int index = lables.indexOf(e.getSource());

		updateSelection(e, index);

		if (e.getButton() == MouseEvent.BUTTON3)
			listeners.forEach(listener -> {
				listener.rightClicked(e, index);
			});
		else if (e.getClickCount() == 2)
			listeners.forEach(listener -> {
				listener.doubleClicked(e, index);
			});
	}


	private void updateSelection(MouseEvent e, int index)
	{
		if (index != -1)
		{
			if (e.getButton() == MouseEvent.BUTTON1)
			{
				if (!e.isShiftDown() && !e.isControlDown())
				{
					selectedIndices.forEach((Integer i) -> {
						lables.get(i).setSelected(false);
					});
					selectedIndices.clear();
					lables.get(index).setSelected(true);
					selectedIndices.add(index);
				} else if (e.isShiftDown())
				{
					int sfIndex = index;
					int slIndex = index;

					if (!selectedIndices.isEmpty())
					{
						sfIndex = selectedIndices.first();
						slIndex = selectedIndices.last();
					}

					selectedIndices.forEach((Integer i) -> {
						lables.get(i).setSelected(false);
					});

					if (index > sfIndex)
					{
						for(int i = sfIndex ; i <= index ; i++)
						{
							lables.get(i).setSelected(true);
							selectedIndices.add(i);
						}
					} else
					{
						for(int i = slIndex ; i >= index ; i--)
						{
							lables.get(i).setSelected(true);
							selectedIndices.add(i);
						}
					}
				} else if (e.isControlDown())
				{
					if (selectedIndices.contains(index))
					{
						lables.get(index).setSelected(false);
						selectedIndices.remove(index);
					} else
					{
						lables.get(index).setSelected(true);
						selectedIndices.add(index);
					}
				}
				cursor = index;
			} else
			{
				GalleryLabel lable = lables.get(index);

				if (!lable.isSelected())
				{
					if (!e.isShiftDown() && !e.isControlDown())
					{
						selectedIndices.forEach(i -> {
							lables.get(i).setSelected(false);
						});
						selectedIndices.clear();
					}

					lable.setSelected(true);
					selectedIndices.add(index);
					cursor = index;
				}
			}
		}
	}


	public boolean isActivated()
	{
		return isActivated;
	}


	public void setActivated(boolean isActivated)
	{
		this.isActivated = isActivated;
		calculate();
		if (Application.getPlayer().status != SongPlayer.STOPED)
			setCurrentSong(Application.getPlaylist().currentSong);
	}


	public void addListener(GalleryListener listener)
	{
		listeners.add(listener);
	}


	public int getSelectedIndex()
	{
		if (selectedIndices.isEmpty())
			return -1;
		return (int) selectedIndices.first();
	}


	public void setCurrentSong(int index)
	{
		if (isActivated)
		{
			lables.forEach(lable -> {
				lable.setPlaying(false);
			});
			lables.get(index).setPlaying(true);
		}
	}


	public int[] getSelectedIndices()
	{
		int[] retVal = new int[selectedIndices.size()];

		for(int i = 0 ; i < retVal.length ; i++)
		{
			retVal[i] = (int) selectedIndices.toArray()[i];
		}
		return retVal;
	}


	public void ensureVisible(int index)
	{
		if (isActivated)
		{
			if (index < 0 || index >= lables.size())
				return;
			GalleryLabel lable = lables.get(index);
			scrollRectToVisible(lable.getBounds());
		}
	}


	public void moveRight(boolean keepSelected)
	{
		if (cursor == lables.size() - 1)
			return;
		if (!keepSelected)
		{
			selectedIndices.forEach(i -> {
				lables.get(i).setSelected(false);
			});
			selectedIndices.clear();
		}
		cursor++;
		if (selectedIndices.contains(cursor))
		{
			lables.get(cursor - 1).setSelected(false);
			selectedIndices.remove(cursor - 1);
		}
		lables.get(cursor).setSelected(true);
		selectedIndices.add(cursor);
		ensureVisible(cursor);
	}


	public void moveLeft(boolean keepSelected)
	{
		if (cursor == 0)
			return;
		if (!keepSelected)
		{
			selectedIndices.forEach(i -> {
				lables.get(i).setSelected(false);
			});
			selectedIndices.clear();
		}
		cursor--;
		if (selectedIndices.contains(cursor))
		{
			lables.get(cursor + 1).setSelected(false);
			selectedIndices.remove(cursor + 1);
		}
		lables.get(cursor).setSelected(true);
		selectedIndices.add(cursor);
		ensureVisible(cursor);
	}


	public void moveDown(boolean keepSelected)
	{
		if (!keepSelected)
		{
			selectedIndices.forEach(i -> {
				lables.get(i).setSelected(false);
			});
			selectedIndices.clear();
		}
		int preCursor = cursor;
		cursor = getLableDwnInd(cursor);

		if (selectedIndices.contains(cursor))
		{
			for(int i = preCursor ; i < cursor ; i++)
			{
				lables.get(i).setSelected(false);
				selectedIndices.remove(i);
			}
		} else if (keepSelected)
		{
			for(int i = preCursor + 1 ; i < cursor ; i++)
			{
				lables.get(i).setSelected(true);
				selectedIndices.add(i);
			}
		}

		lables.get(cursor).setSelected(true);
		selectedIndices.add(cursor);
		ensureVisible(cursor);
	}


	public void moveUp(boolean keepSelected)
	{
		if (!keepSelected)
		{
			selectedIndices.forEach(i -> {
				lables.get(i).setSelected(false);
			});
			selectedIndices.clear();
		}
		int preCursor = cursor;
		cursor = getLableUpInd(cursor);

		if (selectedIndices.contains(cursor))
		{
			for(int i = preCursor ; i > cursor ; i--)
			{
				lables.get(i).setSelected(false);
				selectedIndices.remove(i);
			}
		} else if (keepSelected)
		{
			for(int i = preCursor - 1 ; i > cursor ; i--)
			{
				lables.get(i).setSelected(true);
				selectedIndices.add(i);
			}
		}

		lables.get(cursor).setSelected(true);
		selectedIndices.add(cursor);
		ensureVisible(cursor);
	}


	private int getLableDwnInd(int cursor)
	{
		if (cursor == -1)
		{
			return 0;
		}
		GalleryLabel lable = lables.get(cursor);
		Point labLocation = lable.getLocation();
		Point destLoc = new Point(
				labLocation.x + lable.getPreferredSize().width / 2,
				labLocation.y + lable.getPreferredSize().height * 2);
		int index = lables.indexOf(getComponentAt(destLoc));
		if (index == -1)
			return lables.size() - 1;
		return index;
	}


	private int getLableUpInd(int cursor)
	{
		if (cursor == -1)
		{
			return 0;
		}
		GalleryLabel lable = lables.get(cursor);
		Point labLocation = lable.getLocation();
		Point destLoc = new Point(
				labLocation.x + lable.getPreferredSize().width / 2,
				labLocation.y - lable.getPreferredSize().height / 2);
		int index = lables.indexOf(getComponentAt(destLoc));
		if (index == -1)
			return 0;
		return index;
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


	@Override
	public void shuffeled()
	{
		calculate();
		setCurrentSong(playlist.currentSong);
	}

	public interface GalleryListener {

		public void doubleClicked(MouseEvent e, int index);

		public void rightClicked(MouseEvent e, int index);

	}
}
