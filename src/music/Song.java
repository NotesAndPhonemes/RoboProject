package music;

import java.io.File;

/**
 * @author Harmen Song files zijn 8bit, 8KHz, mono .wav files. Deze moet je
 *         uploaden naar de EV3
 */
public class Song {

	private String title;
	private File file;

	public Song(String title, File file) {
		super();
		this.title = title;
		this.file = file;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getTitle() {
		return title;
	}

	public File getFile() {
		return file;
	}

	@Override
	public String toString() {
		return title;
	}

}
