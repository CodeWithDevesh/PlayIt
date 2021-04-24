package com.devesh.mediaPlayer.utils;

import com.devesh.mediaPlayer.Application;
import com.devesh.mediaPlayer.converter.SngConverter;
import com.mpatric.mp3agic.InvalidDataException;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.probe.FFmpegFormat;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;

public class Song implements Serializable {

	private File file;
	private String title;
	private final int length;
	private FFmpeg ffmpeg = Application.ffmpeg;
	private FFprobe ffprobe = Application.ffprobe;

	public Song(File file)
			throws InvalidDataException, IOException {
		this.file = file;

		if (ffmpeg == null || ffprobe == null)
		{
			ffmpeg = new FFmpeg("ffmpeg.exe");
			ffprobe = new FFprobe("ffprobe.exe");
		}

		FFmpegProbeResult result = ffprobe.probe(file.getPath());
		FFmpegFormat format = result.getFormat();
		length = (int) format.duration * 1000;
		title = format.tags.get("title");
		if (title == null)
		{
			title = file.getName();
			if (title.endsWith(".mp3"))
				title = title.substring(0, title.length() - 4);
		}

		if (!"mp3".equals(format.format_name))
		{
			int x = JOptionPane.showConfirmDialog(null,
					"Format Invalid. Would you like playit to automatically convert it to mp3",
					null, JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);

			if (x == 0)
			{
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fileChooser.setMultiSelectionEnabled(false);
				fileChooser.setCurrentDirectory(Application.currentDir);
				int y = fileChooser.showSaveDialog(null);
				if (y == JFileChooser.APPROVE_OPTION)
				{
					String out = fileChooser.getSelectedFile().getPath()
							+ "\\" + title + ".mp3";
					Application.updateCurrentDir(
							fileChooser.getCurrentDirectory());
					if (!SngConverter.autoConvert(result, out))
						throw new InvalidDataException();
					else
						this.file = new File(out);
				} else
					throw new InvalidDataException();
			} else
			{
				throw new InvalidDataException();
			}
		}
	}

	public File getFile()
	{
		return file;
	}


	public String getTitle()
	{
		return title;
	}


	public int getLength()
	{
		return length;
	}
}
