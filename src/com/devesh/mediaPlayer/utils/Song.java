package com.devesh.mediaPlayer.utils;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

public class Song implements Serializable {

	private final File file;
	private String title;
	private final int length;

	public Song(File file)
			throws InvalidDataException, IOException, UnsupportedTagException {
		this.file = file;

		Mp3File mp3File = new Mp3File(file);
		
		if (mp3File.getId3v1Tag() != null)
			title = mp3File.getId3v1Tag().getTitle();
		else if (mp3File.getId3v2Tag() != null)
			title = mp3File.getId3v2Tag().getTitle();
		
		length = (int) mp3File.getLengthInMilliseconds();

		if (title == null)
		{
			title = file.getName();
			if (title.endsWith(".mp3"))
				title = title.substring(0, title.length() - 4);
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
