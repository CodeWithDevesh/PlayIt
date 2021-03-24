package com.devesh.mediaPlayer.utils;

import com.devesh.mediaPlayer.swing.MainFrame;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Line.Info;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Port;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

/**
 * A class that plays songs from a playlist. It can play, pause, resume and stop
 * the song. You can control the volume also.
 * 
 * @author Devesh
 */
public class SongPlayer {

	public static final int PLAYING = 1;
	public static final int PAUSED = 2;
	public static final int STOPED = 3;

	public int status = STOPED;
	private Playlist playlist;
	private Player player;
	private Thread playbackThread;
	private final Object lock;
	private boolean songChanging = false;

	ArrayList<SongChangeListener> songChangeListeners;

	/**
	 * Creates a new song player.
	 * <p>
	 * Note you must call <code>play()</code> in order to start playing.
	 * 
	 * @param playlist
	 *                     the playlist to be played by the player
	 * 
	 * @see com.devesh.mediaPlayer.SongPlayer#play()
	 */
	public SongPlayer(Playlist playlist) {
		this.playlist = playlist;
		songChangeListeners = new ArrayList<>();
		lock = new Object();
	}


	/**
	 * Play the current song from the <code>playlist</code>
	 * 
	 * @see com.devesh.mediaPlayer.SongPlayer#play(int index)
	 */
	public void play()
	{
		if (status == STOPED && playlist.getCurrentSong() != null)
		{
			try
			{
				FileInputStream is = new FileInputStream(
						playlist.getCurrentSong().getFile());
				player = new Player(is);
				status = PLAYING;
				playInternal();
			} catch (FileNotFoundException | JavaLayerException e)
			{

			}
		}
	}


	/**
	 * Plays the specified song from the playlist
	 * 
	 * @param index
	 *                  the song number to play. index cannot be negative.
	 */
	public void play(int index) throws IndexOutOfBoundsException
	{
		songChanging = true;
		if (!playlist.setCurrentSong(index))
			throw new IndexOutOfBoundsException();
		stop();
		play();
		songChanging = false;

		songChangeListeners.forEach(listener -> {
			listener.songChanged();
		});
	}


	/**
	 * used by play() to play the song on a new thread
	 */
	private void playInternal()
	{
		playbackThread = new Thread(() -> {
			while (status != STOPED)
			{
				try
				{
					if (!player.play(1))
					{
						break;
					}
				} catch (JavaLayerException e)
				{
					break;
				}
				synchronized (lock)
				{
					while (status == PAUSED)
					{
						try
						{
							lock.wait();
						} catch (InterruptedException ex)
						{
							break;
						}
					}
				}
				MainFrame.progressBar.setValue(getProgress());
			}
			player.close();
			status = STOPED;
			if (!songChanging)
				java.awt.EventQueue.invokeLater(() -> {
					next();
				});
		});
		playbackThread.setDaemon(true);
		playbackThread.setPriority(Thread.MAX_PRIORITY);
		playbackThread.start();
	}


	/**
	 * stops the currently playing song.
	 * <p>
	 * Note you cannot resume the song after stopping it.
	 * 
	 * @see com.devesh.mediaPlayer.SongPlayer#pause()
	 */
	public void stop()
	{
		songChanging = true;
		if (status != STOPED)
		{
			if (status == PAUSED)
				resume();
			status = STOPED;
			try
			{
				playbackThread.join();
			} catch (InterruptedException ex)
			{
			}
		}
		songChanging = false;
	}


	/**
	 * plays the next song in the playlist
	 */
	public void next()
	{
		songChanging = true;
		stop();
		playlist.getNextSong();
		play();
		songChanging = false;

		songChangeListeners.forEach(listener -> {
			listener.songChanged();
		});
	}


	/**
	 * plays the previous song in the playlist
	 */
	public void previous()
	{
		songChanging = true;
		stop();
		playlist.getPreSong();
		play();
		songChanging = false;

		songChangeListeners.forEach(listener -> {
			listener.songChanged();
		});
	}


	/**
	 * pauses the current song
	 * 
	 * @see com.devesh.mediaPlayer.SongPlayer#resume()
	 */
	public void pause()
	{
		if (status == PLAYING)
			status = PAUSED;
	}


	/**
	 * resumes the song if and only if it was paused.
	 * 
	 * @see com.devesh.mediaPlayer.SongPlayer#pause()
	 */
	public void resume()
	{
		if (status == PAUSED)
		{
			synchronized (lock)
			{
				status = PLAYING;
				lock.notifyAll();
			}
		}
	}


	/**
	 * Sets the output volume
	 * 
	 * @param volume
	 *                   the new volume. Volume should be greater than 0.0.
	 */
	public void setVoulume(float volume)
	{
		Info source = Port.Info.SPEAKER;
		Info source2 = Port.Info.HEADPHONE;
		Info source3 = Port.Info.LINE_OUT;

		if (AudioSystem.isLineSupported(source))
		{
			try
			{
				Port outline = (Port) AudioSystem.getLine(source);
				outline.open();
				FloatControl volumeControl = (FloatControl) outline
						.getControl(FloatControl.Type.VOLUME);
				volumeControl.setValue(volume);
			} catch (LineUnavailableException ex)
			{
				Logger.getLogger(SongPlayer.class.getName()).log(Level.SEVERE,
						null, ex);
			}
		}
		if (AudioSystem.isLineSupported(source2))
		{
			try
			{
				Port outline = (Port) AudioSystem.getLine(source2);
				outline.open();
				FloatControl volumeControl = (FloatControl) outline
						.getControl(FloatControl.Type.VOLUME);
				volumeControl.setValue(volume);
			} catch (LineUnavailableException ex)
			{
				Logger.getLogger(SongPlayer.class.getName()).log(Level.SEVERE,
						null, ex);
			}
		}
		if (AudioSystem.isLineSupported(source3))
		{
			try
			{
				Port outline = (Port) AudioSystem.getLine(source3);
				outline.open();
				FloatControl volumeControl = (FloatControl) outline
						.getControl(FloatControl.Type.VOLUME);
				volumeControl.setValue(volume);
			} catch (LineUnavailableException ex)
			{
				Logger.getLogger(SongPlayer.class.getName()).log(Level.SEVERE,
						null, ex);
			}
		}
	}


	/**
	 * get song completion in percentage
	 * 
	 * @return a int between 0 and 100
	 */
	public int getProgress()
	{
		float a = player.getPosition();
		float b = playlist.getCurrentSong().getLength();
		float i = (a / b) * 100;
		return (int) i;
	}


	/**
	 * Add a new <code>SongChangeListener</code>.
	 * 
	 * @param listener
	 *                     the SongChangeListener to be added.
	 * 
	 * @see com.devesh.mediaPlayer.SongPlayer.SongChangeListener
	 */
	public void addSongChangeListener(SongChangeListener listener)
	{
		songChangeListeners.add(listener);
	}

	/**
	 * An interface for listening to song change events
	 */
	public interface SongChangeListener {
		/**
		 * called when the song is changed
		 */
		public void songChanged();
	}
	
	public void changePlaylist(Playlist playlist){
		this.playlist = playlist;
	}
}
