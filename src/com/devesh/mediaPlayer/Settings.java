package com.devesh.mediaPlayer;

import static com.devesh.mediaPlayer.Application.prefs;
import java.util.prefs.PreferenceChangeEvent;

public class Settings {
	public static String THEME_KEY = "THEME";
	public static String CLOSE_KEY = "CLOSE_METHOD";
	public static String NOTIFICATION_KEY = "SHOW_NOTIFICATION";
	public static String PAUSEBTN_ENABLED = "PAUSEBTN_ENABLSED";

	private static boolean showNotification;
	private static boolean minOnClose;
	private static String theme;
	private static boolean pauseEnbl;

	public static void init()
	{
		prefs.addPreferenceChangeListener((PreferenceChangeEvent evt) -> {
			loadSettings();
		});
		theme = prefs.get(THEME_KEY, "Dark");
		minOnClose = prefs.getBoolean(CLOSE_KEY, false);
		showNotification = prefs.getBoolean(NOTIFICATION_KEY, true);
		pauseEnbl = prefs.getBoolean(PAUSEBTN_ENABLED, true);
		loadSettings();
	}


	public static void loadSettings()
	{
		minOnClose = prefs.getBoolean(CLOSE_KEY, false);
		showNotification = prefs.getBoolean(NOTIFICATION_KEY, true);
		pauseEnbl = prefs.getBoolean(PAUSEBTN_ENABLED, true);
	}


	public static boolean isShowNotification()
	{
		return showNotification;
	}


	public static boolean isMinOnClose()
	{
		return minOnClose;
	}


	public static String getTheme()
	{
		return theme;
	}
	
	public static boolean isPauseBtn(){
		return pauseEnbl;
	}
}
