package com.devesh.mediaPlayer.converter;

import com.devesh.mediaPlayer.Application;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import javax.swing.JOptionPane;
import javax.swing.ProgressMonitor;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.job.FFmpegJob;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.progress.Progress;
import net.bramp.ffmpeg.progress.ProgressListener;
import org.slf4j.Logger;

public class SngConverter {

	FFmpeg ffmpeg;
	FFprobe ffprobe;
	private static final Logger logger = Application.logger;
	private boolean canceled = false;

	public SngConverter() {
		try
		{
			ffmpeg = new FFmpeg("ffmpeg.exe");
			ffprobe = new FFprobe("ffprobe.exe");
		} catch (IOException ex)
		{
			logger.error(null, ex);
		}
	}


	public void autoConvert(FFmpegProbeResult in, String out,
			ConversionListener listener, String title)
	{
		setCanceled(false);
		try
		{
			FFmpegBuilder builder = new FFmpegBuilder()
					.addInput(in)
					.addOutput(out).addMetaTag("title", title)
					.setAudioBitRate(320000)
					.done();
			FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);

			ProgressMonitor progressMonitor = new ProgressMonitor(
					Application.getFrame(),
					"Converting your file...", null, 0, 100);
			Thread thread = new Thread(() -> {
				try
				{
					FFmpegJob job = executor.createJob(builder,
							new ProgressListener() {

								final double duration_ns = in
										.getFormat().duration
										* TimeUnit.SECONDS.toNanos(1);

								@Override
								public void progress(Progress progress)
								{
									if (progressMonitor.isCanceled())
									{
										try
										{
											setCanceled(true);
											ffmpeg.getProcess().destroy();
										} catch (Exception ex)
										{
											logger.error(null, ex);
										}
									}
									double percentage = progress.out_time_ns
											/ duration_ns;
									progressMonitor.setProgress(
											(int) Math.min(percentage * 100,
													100));
								}
							});
					job.run();
				} catch (Exception ex)
				{
					logger.error("Error while converting", ex);
					if (!canceled)
					{
						if (listener != null)
							listener.stoped(false, null);
						JOptionPane.showMessageDialog(null,
								"An error occured while converting your file",
								"Error", JOptionPane.OK_OPTION);
					}
				}
				progressMonitor.close();
				if (listener != null && !canceled)
					listener.stoped(true, new File(out));
			});

			thread.start();
		} catch (Exception ex)
		{
			logger.error("Exception while converting song", ex);
			if (listener != null)
				listener.stoped(false, null);
			JOptionPane.showMessageDialog(null,
					"An error occured while converting your file",
					"Error", JOptionPane.OK_OPTION);
		}
	}


	public void convert(File in, File out, String title)
	{
		setCanceled(false);
		try
		{
			FFmpegBuilder builder = new FFmpegBuilder()
					.addInput(in.getPath())
					.addOutput(out.getPath()).addMetaTag("title", title)
					.setAudioBitRate(320000)
					.done();
			FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
			ProgressMonitor progressMonitor = new ProgressMonitor(
					Application.getFrame(),
					"Converting your file...", null, 0, 100);

			Thread thread = new Thread(() -> {
				try
				{
					FFmpegJob job = executor.createJob(builder,
							new ProgressListener() {
								final double duration_ns = ffprobe
										.probe(in.getPath())
										.getFormat().duration
										* TimeUnit.SECONDS.toNanos(1);

								@Override
								public void progress(Progress progress)
								{
									if (progressMonitor.isCanceled())
									{
										try
										{
											setCanceled(true);
											ffmpeg.getProcess().destroy();
										} catch (Exception ex)
										{
											logger.error(null, ex);
										}
									}
									double percentage = progress.out_time_ns
											/ duration_ns;
									progressMonitor.setProgress(
											(int) Math.min(percentage * 100,
													100));
								}
							});
					job.run();
				} catch (Exception ex)
				{
					logger.error("Error while converting", ex);
					if (!canceled)
						JOptionPane.showMessageDialog(null,
								"An error occured while converting your file",
								"Error", JOptionPane.OK_OPTION);
				}
				progressMonitor.close();
			});
			thread.start();
		} catch (Exception ex)
		{
			logger.error("Error while converting", ex);
			JOptionPane.showMessageDialog(null,
					"An error occured while converting your file",
					"Error", JOptionPane.OK_OPTION);
		}
	}


	private void setCanceled(boolean canceled)
	{
		this.canceled = canceled;
	}
}
