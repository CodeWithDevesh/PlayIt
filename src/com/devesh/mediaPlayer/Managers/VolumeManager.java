package com.devesh.mediaPlayer.Managers;

import com.devesh.mediaPlayer.Application;
import com.devesh.mediaPlayer.utils.SongPlayer;
import javax.swing.JSlider;

public class VolumeManager {

	int currentVol = 100;
	JSlider slider;
	SongPlayer player = Application.getPlayer();

	public VolumeManager() {
		slider = new JSlider(0, 100);
		slider.setToolTipText("Volume");
		slider.setValue(100);
		slider.setDoubleBuffered(true);
		slider.setFocusable(false);
		slider.addChangeListener((e) -> {
			sldValueChanged();
		});
	}


	private void sldValueChanged()
	{
		player.setVoulume(slider.getValue() / 100f);
	}


	public void increaseVol()
	{
		slider.setValue(slider.getValue() + 5);
	}


	public void decreaseVol()
	{
		slider.setValue(slider.getValue() - 5);
	}


	public JSlider getSlider()
	{
		return slider;
	}
}
