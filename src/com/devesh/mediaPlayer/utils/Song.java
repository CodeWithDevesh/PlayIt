package com.devesh.mediaPlayer.utils;

import com.devesh.mediaPlayer.Application;
import static com.devesh.mediaPlayer.Application.logger;
import com.devesh.mediaPlayer.converter.SngConverter;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.probe.FFmpegFormat;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;

public class Song implements Serializable {

	private final File file;
	private String title = null;
	private final ImageIcon image;
	private long length;
	private transient FFmpeg ffmpeg = Application.ffmpeg;
	private transient FFprobe ffprobe = Application.ffprobe;
	private byte[] imageData = null;

	public Song(File file)
			throws InvalidDataException, IOException {
		this.file = file;

		Mp3File mp3File;
		try
		{
			mp3File = new Mp3File(file);
			length = mp3File.getLengthInSeconds();
			if(length < 0)
				throw new InvalidDataException();
			if (!mp3File.hasId3v1Tag() && !mp3File.hasId3v2Tag())
				throw new InvalidDataException();
			if (mp3File.hasId3v2Tag())
			{
				title = mp3File.getId3v2Tag().getTitle();
				imageData = mp3File.getId3v2Tag().getAlbumImage();
			} else if (mp3File.hasId3v1Tag())
			{
				title = mp3File.getId3v1Tag().getTitle();
			}
			if (title == null || title.isBlank())
			{
				title = file.getName();
				if (title.endsWith("mp3"))
				{
					title = title.substring(0,
							title.length() - ("mp3".length() + 1));
				}
			}
		} catch (InvalidDataException | IOException
				| UnsupportedTagException ex)
		{
			title = null;
			logger.error(null, ex);

			if (ffmpeg == null || ffprobe == null)
			{
				ffmpeg = new FFmpeg("ffmpeg.exe");
				ffprobe = new FFprobe("ffprobe.exe");
			}

			FFmpegProbeResult result = ffprobe.probe(file.getPath());
			FFmpegFormat format = result.getFormat();

			length = (long) format.duration;
			if (format.tags != null)
				title = format.tags.get("title");
			if (title == null || title.isBlank())
			{
				title = file.getName();
				String[] formatNames = format.format_name.split(",");
				for(String formatName : formatNames)
					if (title.endsWith(formatName))
						title = title.substring(0,
								title.length() - (formatName.length() + 1));
			}

			if (!"mp3".equals(format.format_name))
			{
				int x = JOptionPane.showConfirmDialog(Application.getFrame(),
						title + " has invalid format. Would you like playit to automatically convert it to mp3",
						null, JOptionPane.YES_NO_OPTION,
						JOptionPane.ERROR_MESSAGE);

				if (x == 0)
				{
					JFileChooser fileChooser = new JFileChooser();
					fileChooser.setFileSelectionMode(
							JFileChooser.DIRECTORIES_ONLY);
					fileChooser.setMultiSelectionEnabled(false);
					fileChooser.setCurrentDirectory(Application.currentDir);
					int y = fileChooser.showSaveDialog(null);
					if (y == JFileChooser.APPROVE_OPTION)
					{
						String out = fileChooser.getSelectedFile().getPath()
								+ "\\" + title + ".mp3";
						SngConverter.autoConvert(result, out,
								Application.CONVERSION_LISTENER, title);
						throw new InvalidDataException();
					} else
						throw new InvalidDataException();
				} else
				{
					throw new InvalidDataException();
				}
			}
		}

		if (imageData != null)
		{
			image = new ImageIcon(imageData);
		} else
		{
			image = new ImageIcon(getClass().getResource("/icon.png"));
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


	public long getLength()
	{
		return length;
	}


	public ImageIcon getImage()
	{
		return image;
	}
}
