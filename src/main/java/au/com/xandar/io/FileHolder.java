package au.com.xandar.io;

import java.io.File;

/**
 * Responsible for holding a reference to a File.
 * 
 * @author william
 */
public interface FileHolder {
	public File getFile();
	public void setFile(File file);
}
