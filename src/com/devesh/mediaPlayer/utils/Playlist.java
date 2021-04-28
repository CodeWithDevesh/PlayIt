package com.devesh.mediaPlayer.utils;

import com.devesh.mediaPlayer.listeners.PlayListListener;
import com.mpatric.mp3agic.InvalidDataException;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.DefaultListModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Playlist implements Serializable {
	
	private final ArrayList<Song> list;
	private final DefaultListModel<String> songs;
	public int currentSong;
	private transient ArrayList<PlayListListener> listeners = new ArrayList<>();
	private final Logger logger = LoggerFactory.getLogger(Playlist.class);
	
	public Playlist() {
		list = new ArrayList<>();
		this.songs = new DefaultListModel<>();
	}


	public Playlist(ArrayList<File> files) {
		ArrayList<Song> songList = new ArrayList<>();

		files.forEach((File file) -> {
			try
			{
				songList.add(new Song(file));
			} catch (InvalidDataException | IOException ex)
			{
			}
		});

		list = new ArrayList<>(songList);
		this.songs = new DefaultListModel<>();
		songList.forEach(song -> {
			this.songs.addElement(song.getTitle());
		});

	}


	public Playlist(Song song) {
		list = new ArrayList<>();
		list.add(song);
		this.songs = new DefaultListModel<>();
		this.songs.addElement(song.getTitle());
	}


	public Playlist(Playlist playlist) {
		this.list = playlist.list;
		this.songs = playlist.songs;
	}


	public void addSong(Song song)
	{
		list.add(song);
		songs.addElement(song.getTitle());
		listeners.forEach(listener -> {
			listener.sngAdded(size()-1);
		});
	}


	public void addSong(Song song, int index)
	{
		if (currentSong >= index)
			currentSong++;
		list.add(index, song);
		songs.add(index, song.getTitle());
		listeners.forEach(listener -> {
			listener.sngAdded(index);
		});
	}


	public void addSongs(ArrayList<File> files)
	{
		ArrayList<Song> songList = new ArrayList<>();

		files.forEach(file -> {
			try
			{
				songList.add(new Song(file));
			} catch (InvalidDataException | IOException ex)
			{
				logger.error("exception while opening " + file.getName(), ex);
			}
		});

		list.addAll(songList);

		songList.forEach(song -> {
			this.songs.addElement(song.getTitle());
		});
		listeners.forEach(listener -> {
			listener.sngAdded(size()-1);
		});
	}
	

	public Song getSong(int index)
	{
		return list.get(index);
	}


	public void removeSong(int index)
	{
		if (currentSong > index)
			currentSong--;
		list.remove(index);
		songs.remove(index);
	}


	public ArrayList<Song> getPlayList()
	{
		return list;
	}


	public void shuffel()
	{
		ArrayList<Song> que = new ArrayList<>(list);
		ArrayList<Integer> intsAdded = new ArrayList<>();
		Song songPlaying = list.get(currentSong);

		list.clear();

		while (list.size() < que.size())
		{
			int i = new Random().nextInt(que.size());
			if (!intsAdded.contains(i))
			{
				intsAdded.add(i);
				list.add(que.get(i));
			}
		}

		songs.clear();
		for(int i = 0 ; i < list.size() ; i++)
		{
			songs.addElement(list.get(i).getTitle());
		}

		currentSong = list.indexOf(songPlaying);
	}


	public Song getNextSong()
	{

		if (currentSong == list.size() - 1)
			currentSong = -1;

		if (list.isEmpty())
			return null;

		currentSong++;
		return list.get(currentSong);
	}


	public Song getPreSong()
	{
		if (currentSong == 0)
			currentSong = list.size();

		if (list.isEmpty())
			return null;

		currentSong--;
		return list.get(currentSong);
	}


	public Song getCurrentSong()
	{
		if (list.isEmpty())
			return null;
		return list.get(currentSong);
	}


	public DefaultListModel<String> getListModel()
	{
		return songs;
	}


	public boolean setCurrentSong(int index)
	{
		if (index > -1 && index < list.size())
		{
			currentSong = index;
			return true;
		}
		return false;
	}


	public int size()
	{
		return list.size();
	}
	
	public void addListener(PlayListListener listener){
		if(listeners == null)
			listeners = new ArrayList<>();
		listeners.add(listener);
	}
}
