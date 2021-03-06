package com.devesh.mediaPlayer;

import static com.devesh.mediaPlayer.Application.prefs;
import java.util.prefs.PreferenceChangeEvent;

public class Settings {
	public static String THEME_KEY = "THEME";
	public static String CLOSE_KEY = "CLOSE_METHOD";
	public static String NOTIFICATION_KEY = "SHOW_NOTIFICATION";
	public static String GLOBAL_ENABLED_KEY = "PAUSEBTN_ENABLSED";
	public static String DOWNLOAD_LINK_KEY = "DOWNLOAD_LINK";
	public static String VIEW = "VIEW";

	private static boolean showNotification;
	private static boolean minOnClose;
	private static String theme;
	private static boolean globalControl;
	private static boolean isGalleryView;
	private static String downloadLink;

	public static void init()
	{
		prefs.addPreferenceChangeListener((PreferenceChangeEvent evt) -> {
			loadSettings();
		});
		theme = prefs.get(THEME_KEY, "Light");
		loadSettings();
	}


	public static void loadSettings()
	{
		minOnClose = prefs.getBoolean(CLOSE_KEY, false);
		showNotification = prefs.getBoolean(NOTIFICATION_KEY, true);
		globalControl = prefs.getBoolean(GLOBAL_ENABLED_KEY, true);
		isGalleryView = prefs.getBoolean(VIEW, true);
		downloadLink = prefs.get(DOWNLOAD_LINK_KEY,
				"https://ytmdl.deepjyoti30.dev/search");
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


	public static boolean isGlobalCtrlEnabled()
	{
		return globalControl;
	}


	public static boolean isGalleryView()
	{
		return isGalleryView;
	}


	public static void setGalleryView(boolean isGalleryView)
	{
		Settings.isGalleryView = isGalleryView;
		prefs.putBoolean(VIEW, isGalleryView);
	}


	public static String getDownloadLink()
	{
		return downloadLink;
	}

}
