package com.devesh.mediaPlayer;

import static com.devesh.mediaPlayer.Application.prefs;

public class Settings {
	public static String THEME_KEY = "THEME";
	public static String CLOSE_KEY = "CLOSE_METHOD";
	public static String NOTIFICATION_KEY = "SHOW_NOTIFICATION";

	private static boolean showNotification;
	private static boolean minOnClose;
	private static String theme;

	public static void loadSettings()
	{
		theme = prefs.get(THEME_KEY, "Dark");
		minOnClose = prefs.getBoolean(CLOSE_KEY, false);
		showNotification = prefs.getBoolean(NOTIFICATION_KEY, true);
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

}
