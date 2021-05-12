package com.devesh.mediaPlayer.Managers;

import com.devesh.mediaPlayer.Application;
import com.devesh.mediaPlayer.swing.MainFrame;
import com.devesh.mediaPlayer.utils.Playlist;
import com.devesh.mediaPlayer.utils.SongPlayer;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MainFrameManager implements KeyListener, MouseListener {

	private final VolumeManager volumeManager = MainFrame.volManager;
	private final SngListManager sngListManager = MainFrame.sngListManager;
	private final SongPlayer player = Application.getPlayer();
	private final Playlist playlist = Application.getPlaylist();
	private MainFrame frame = Application.getFrame();

	public MainFrameManager() {
	}


	public MainFrameManager(MainFrame frame) {
		this.frame = frame;
	}


	@Override
	public void keyTyped(KeyEvent e)
	{
	}


	@Override
	public void keyPressed(KeyEvent evt)
	{
		int index;

		switch (evt.getKeyCode()) {

		case KeyEvent.VK_UP -> {
			if (evt.isControlDown())
				volumeManager.increaseVol();
			else if (evt.isShiftDown())
			{
				sngListManager.moveUp(true);
			} else
				sngListManager.moveUp(false);
		}

		case KeyEvent.VK_DOWN -> {
			if (evt.isControlDown())
				volumeManager.decreaseVol();
			else if (evt.isShiftDown())
				sngListManager.moveDown(true);
			else
				sngListManager.moveDown(false);
		}

		case KeyEvent.VK_SPACE -> play();

		case KeyEvent.VK_LEFT -> {
			if (evt.isControlDown())
			{
				sngListManager.previousSong(true);
			}else if(evt.isShiftDown()){
				sngListManager.moveLeft(true);
			}else{
				sngListManager.moveLeft(false);
			}
		}

		case KeyEvent.VK_RIGHT -> {
			if (evt.isControlDown())
			{
				sngListManager.nextSong(true);
			}else if(evt.isShiftDown()){
				sngListManager.moveRight(true);
			}else{
				sngListManager.moveRight(false);
			}
		}

		case KeyEvent.VK_ENTER -> {
			sngListManager.playSelected();
		}

		case KeyEvent.VK_NUMPAD5 -> {
			sngListManager.playSelected();
		}

		case KeyEvent.VK_DELETE -> {
			sngListManager.removeSelected();
		}

		case KeyEvent.VK_W -> {
			if (evt.isControlDown())
				frame.setVisible(false);
		}

		default -> {
		}
		}
		frame.updatePlayIcon();
	}


	public void play()
	{
		switch (player.status) {
		case SongPlayer.STOPED -> player.play();
		case SongPlayer.PAUSED -> player.resume();
		case SongPlayer.PLAYING -> player.pause();
		default -> {
		}
		}
		frame.updatePlayIcon();
	}


	@Override
	public void keyReleased(KeyEvent e)
	{
	}


	@Override
	public void mouseClicked(MouseEvent e)
	{
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