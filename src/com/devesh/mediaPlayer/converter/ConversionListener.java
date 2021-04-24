package com.devesh.mediaPlayer.converter;

import java.io.File;


public interface ConversionListener {
	
	public void stoped(boolean completed, File file);
	
}
