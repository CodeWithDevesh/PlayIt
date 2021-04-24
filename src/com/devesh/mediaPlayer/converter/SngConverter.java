package com.devesh.mediaPlayer.converter;

import com.devesh.mediaPlayer.Application;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFmpegUtils;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.job.FFmpegJob;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.progress.Progress;
import net.bramp.ffmpeg.progress.ProgressListener;
import org.slf4j.Logger;

public class SngConverter {

	private static FFmpeg ffmpeg = Application.ffmpeg;
	private static FFprobe ffprobe = Application.ffprobe;
	private static final Logger logger = Application.logger;

	public static boolean autoConvert(FFmpegProbeResult in, String out)
	{
		try
		{
			if (ffmpeg == null || ffprobe == null)
			{
				ffmpeg = new FFmpeg("ffmpeg.exe");
				ffprobe = new FFprobe("ffprobe.exe");
			}

			FFmpegBuilder builder = new FFmpegBuilder().addInput(in).addOutput(
					out).done();
			FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
			FFmpegJob job = executor.createJob(builder, new ProgressListener() {

				// Using the FFmpegProbeResult determine the duration of the
				// input
				final double duration_ns = in.getFormat().duration
						* TimeUnit.SECONDS.toNanos(1);

				@Override
				public void progress(Progress progress)
				{
					double percentage = progress.out_time_ns / duration_ns;

					// Print out interesting information about the progress
					System.out.println(String.format(
							"[%.0f%%] status:%s frame:%d time:%s ms fps:%.0f speed:%.2fx",
							percentage * 100,
							progress.status,
							progress.frame,
							FFmpegUtils.toTimecode(progress.out_time_ns,
									TimeUnit.NANOSECONDS),
							progress.fps.doubleValue(),
							progress.speed));
				}
			});
			job.run();
			return true;
		} catch (IOException ex)
		{
			logger.error("Exception while converting song", ex);
			return false;
		}
	}
}
