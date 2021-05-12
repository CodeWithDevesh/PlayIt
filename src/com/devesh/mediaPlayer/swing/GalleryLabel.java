package com.devesh.mediaPlayer.swing;

import com.devesh.mediaPlayer.Settings;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;

public class GalleryLabel extends JComponent implements MouseListener {

	private boolean isSelected = false;
	private boolean isPlaying = false;

	private final JLabel txtLabel, imgLabel;
	private final ImageIcon icon, icon2;

	public GalleryLabel(String text, ImageIcon icon, Dimension size) {
		super();
		setPreferredSize(size);
		setToolTipText(text);
		setLayout(new BorderLayout());

		this.icon2 = new ImageIcon(
				icon.getImage().getScaledInstance(size.width - 20,
						size.height - 20, Image.SCALE_FAST));
		this.icon = new ImageIcon(
				icon.getImage().getScaledInstance(size.width - 60,
						size.height - 60, Image.SCALE_FAST));

		txtLabel = new JLabel(text);
		txtLabel.setHorizontalAlignment(JLabel.CENTER);

		imgLabel = new JLabel(this.icon);

		add(imgLabel, BorderLayout.CENTER);
		add(txtLabel, BorderLayout.SOUTH);
	}


	@Override
	public void paint(Graphics g)
	{
		super.paint(g);
	}


	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;
		if (isPlaying)
		{
			if ("Light".equals(Settings.getTheme()))
				g2d.setColor(new Color(153, 196, 106));
			else
				g2d.setColor(new Color(99, 125, 71));
			g2d.fillRect(0, 0, getWidth(), getHeight());
		}
		if (isSelected)
		{
			if ("Light".equals(Settings.getTheme()))
				g2d.setColor(Color.LIGHT_GRAY);
			else
				g2d.setColor(Color.GRAY);
			g2d.fillRect(0, 0, getWidth(), getHeight());
		}
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
		imgLabel.setIcon(icon2);
		repaint();
	}


	@Override
	public void mouseExited(MouseEvent e)
	{
		imgLabel.setIcon(icon);
		repaint();
	}


	public void setSelected(boolean b)
	{
		isSelected = b;
		repaint();
	}


	public void setPlaying(boolean isPlaying)
	{
		this.isPlaying = isPlaying;
		repaint();
	}


	public boolean isSelected()
	{
		return isSelected;
	}


	public boolean isPlaying()
	{
		return isPlaying;
	}
}
