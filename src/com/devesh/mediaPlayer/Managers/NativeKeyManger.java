package com.devesh.mediaPlayer.Managers;

import com.devesh.mediaPlayer.Settings;
import com.devesh.mediaPlayer.swing.MainFrame;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class NativeKeyManger implements NativeKeyListener {

	private boolean shiftDwn = false;
	private boolean toPlayPause = true;

	@Override
	public void nativeKeyTyped(NativeKeyEvent nke)
	{
	}


	@Override
	public void nativeKeyPressed(NativeKeyEvent nke)
	{
		if (nke.getKeyCode() == NativeKeyEvent.VC_SHIFT
				|| nke.getKeyCode() == 3638)
		{
			shiftDwn = true;
		}
		if (nke.getKeyCode() == NativeKeyEvent.VC_PAUSE && toPlayPause)
		{
			if (Settings.isPauseBtn())
			{
				MainFrame.manager.play();
			}
			toPlayPause = false;
		}
	}


	@Override
	public void nativeKeyReleased(NativeKeyEvent nke)
	{
		if (nke.getKeyCode() == NativeKeyEvent.VC_SHIFT
				|| nke.getKeyCode() == 3638)
		{
			shiftDwn = false;
		}

		if (nke.getKeyCode() == NativeKeyEvent.VC_PAUSE)
		{
			toPlayPause = true;
		}
	}
}
