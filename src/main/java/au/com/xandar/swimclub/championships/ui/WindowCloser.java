package au.com.xandar.swimclub.championships.ui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public final class WindowCloser extends WindowAdapter {
	
	/**
	 * Hides the Window that is closing and disposes of its resources.
	 */
	@Override
	public void windowClosing(WindowEvent event) {
		System.exit(0);
	}
}
