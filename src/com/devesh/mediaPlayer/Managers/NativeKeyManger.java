package com.devesh.mediaPlayer.Managers;

import com.devesh.mediaPlayer.Application;
import com.devesh.mediaPlayer.Settings;
import com.devesh.mediaPlayer.swing.MainFrame;
import com.devesh.mediaPlayer.utils.SongPlayer;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class NativeKeyManger implements NativeKeyListener {

	private boolean toPlayPause = true;
	private boolean isPauseDwn = false;
	private boolean toNext = true, toPrevious = true;
	private boolean played = false, paused = false;

	private final SongPlayer player;

	public NativeKeyManger() {
		player = Application.getPlayer();
	}


	@Override
	public void nativeKeyTyped(NativeKeyEvent nke)
	{
	}


	@Override
	public void nativeKeyPressed(NativeKeyEvent nke)
	{
		if (Settings.isGlobalCtrlEnabled())
		{
			if (nke.getKeyCode() == NativeKeyEvent.VC_PAUSE)
				isPauseDwn = true;

			if (nke.getKeyCode() == NativeKeyEvent.VC_PAUSE && toPlayPause)
			{
				if (player.status == SongPlayer.PAUSED)
				{
					player.resume();
					played = true;
				} else if (player.status == SongPlayer.PLAYING)
				{
					player.pause();
					paused = true;
				}
				toPlayPause = false;
			}

			if (nke.getKeyCode() == NativeKeyEvent.VC_LEFT && isPauseDwn
					&& toPrevious)
			{
				player.previous();
				toPrevious = false;
				played = false;
				paused = false;
			}
			if (nke.getKeyCode() == NativeKeyEvent.VC_RIGHT && isPauseDwn
					&& toNext)
			{
				player.next();
				toNext = false;
				played = false;
				paused = false;
			}

			if (nke.getKeyCode() == NativeKeyEvent.VC_UP && isPauseDwn)
			{
				if (paused)
				{
					player.resume();
					paused = false;
				} else if (played)
				{
					player.pause();
					played = false;
				}
				MainFrame.volManager.increaseVol();
			}
			if (nke.getKeyCode() == NativeKeyEvent.VC_DOWN && isPauseDwn)
			{
				if (paused)
				{
					player.resume();
					paused = false;
				} else if (played)
				{
					player.pause();
					played = false;
				}
				MainFrame.volManager.decreaseVol();
			}
		}
	}


	@Override
	public void nativeKeyReleased(NativeKeyEvent nke)
	{
		if (nke.getKeyCode() == NativeKeyEvent.VC_PAUSE)
		{
			toPlayPause = true;
			isPauseDwn = false;
			played = false;
			paused = false;
		}

		if (nke.getKeyCode() == NativeKeyEvent.VC_RIGHT)
			toNext = true;
		if (nke.getKeyCode() == NativeKeyEvent.VC_LEFT)
			toPrevious = true;
	}
}
