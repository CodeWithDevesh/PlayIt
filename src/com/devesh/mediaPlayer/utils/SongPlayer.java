package com.devesh.mediaPlayer.utils;

import static com.devesh.mediaPlayer.Application.logger;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

/**
 * A class that plays songs from a playlist. It can play, pause, resume and stop
 * the song. You can control the volume also.
 * 
 * @author Devesh
 */
public final class SongPlayer {

	public static final int PLAYING = 1;
	public static final int PAUSED = 2;
	public static final int STOPED = 3;

	public int status = STOPED;
	private Playlist playlist;
	private Player player;
	private Thread playbackThread;
	private final Object lock;
	private boolean songChanging = false;
	private float currentVol = 0f;
	private int framesSkipped = 0;
	private boolean updatingProgress = false;

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
		setVoulume(1f);
	}


	/**
	 * Play the current song from the <code>playlist</code>
	 * 
	 * @see com.devesh.mediaPlayer.SongPlayer#play(int index)
	 * 
	 * @throws FileNotFoundException
	 * @throws JavaLayerException
	 */
	public void play() throws FileNotFoundException, JavaLayerException
	{
		if (status == STOPED && playlist.getCurrentSong() != null)
		{
			FileInputStream is = new FileInputStream(
					playlist.getCurrentSong().getFile());
			player = new Player(is);
			status = PLAYING;
			playInternal();
		}
	}


	/**
	 * Plays the specified song from the playlist
	 * 
	 * @param index
	 *                  the song number to play. index cannot be negative.
	 * 
	 */
	public void play(int index) throws IndexOutOfBoundsException
	{
		songChanging = true;
		if (!playlist.setCurrentSong(index))
			throw new IndexOutOfBoundsException();
		stop();
		try
		{
			play();
		} catch (FileNotFoundException ex)
		{
			logger.error(null, ex);
			handleFileNotFound();
		} catch (JavaLayerException ex)
		{
			logger.error(null, ex);
		}
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
		framesSkipped = 0;
		playbackThread = new Thread(() -> {
			while (updatingProgress)
			{
				System.out.print("");
			}
			while (status != STOPED)
			{
				try
				{
					player.setVolume(currentVol);
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
			}
			player.close();
			status = STOPED;
			if (!songChanging)
				java.awt.EventQueue.invokeLater(() -> {
					next();
				});
		});
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
		if (playlist.size() == 0)
			return;
		songChanging = true;
		stop();
		playlist.getNextSong();
		try
		{
			play();
		} catch (FileNotFoundException ex)
		{
			logger.error(null, ex);
			handleFileNotFound();
		} catch (JavaLayerException ex)
		{
			logger.error(null, ex);
		}
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
		if (playlist.size() == 0)
			return;
		songChanging = true;
		stop();
		playlist.getPreSong();
		try
		{
			play();
		} catch (FileNotFoundException ex)
		{
			logger.error(null, ex);
			handleFileNotFound();
		} catch (JavaLayerException ex)
		{
			logger.error(null, ex);
		}
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
		currentVol = (float) ((volume * 30) - 30);
		if (volume == 0)
			currentVol = -80f;
	}


	/**
	 * get song completion in percentage
	 * 
	 * @return a int between 0 and 100
	 */
	public int getProgressPercentage()
	{
		if (player != null && playlist.getCurrentSong() != null)
		{
			double a = player.getPosition() + framesSkipped;
			double b = playlist.getCurrentSong().getLength() * 1000;
			double i = (a / b) * 100;
			return (int) i;
		}
		return 0;
	}


	public int getProgressMillis()
	{
		if (player != null && playlist.getCurrentSong() != null)
			return player.getPosition() + framesSkipped;
		return 0;
	}


	/**
	 * set the progress in percentage
	 * 
	 * @param progress
	 *                     it should be between 0 and 100
	 */
	public void setProgress(int progress)
	{
		boolean toPause = false;
		if (progress < 0 || progress > 100)
			return;

		if (status == PAUSED)
			toPause = true;

		if (status != STOPED)
		{
			stop();
			updatingProgress = true;
			try
			{
				play();
				float percentage = ((float) progress) / 100f;
				int toSkip = (int) (percentage
						* ((float) playlist.getCurrentSong().getLength() * 1000));
				framesSkipped = toSkip;
				player.skipMilliSeconds(toSkip);
			} catch (JavaLayerException | FileNotFoundException ex)
			{
				logger.error(null, ex);
				next();
			}
			updatingProgress = false;
			if (toPause)
				pause();
		}
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

	public void changePlaylist(Playlist playlist)
	{
		this.playlist = playlist;
	}


	private void handleFileNotFound()
	{
		JOptionPane.showMessageDialog(null,
				"Could not find "
						+ playlist.getCurrentSong().getFile().getName(),
				"Error", JOptionPane.ERROR_MESSAGE);
		if (playlist.size() == 1)
			playlist.removeSong(playlist.currentSong);
		else
		{
			int x = playlist.currentSong;
			next();
			playlist.removeSong(x);
		}
	}
}