package com.devesh.mediaPlayer.listHelpers;

import com.devesh.mediaPlayer.Settings;
import com.devesh.mediaPlayer.utils.Playlist;
import java.awt.Color;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

public class SngListCellRenderer extends DefaultListCellRenderer {
	
	private final Playlist playlist;
	
	public SngListCellRenderer(Playlist playlist){
		this.playlist = playlist;
	}
	
	@Override
	public Component getListCellRendererComponent(JList<?> list, Object value,
			int index, boolean isSelected, boolean cellHasFocus)
	{
		Component c = super.getListCellRendererComponent(list, value, index,
				isSelected, cellHasFocus);
		if(list.getSelectedIndex() != index && index == playlist.currentSong){
			if("Dark".equals(Settings.getTheme()))
			c.setBackground(Color.BLACK);
			else
				c.setBackground(Color.green);
		}
		return c;
	}

}
