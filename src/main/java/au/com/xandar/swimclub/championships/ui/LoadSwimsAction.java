package au.com.xandar.swimclub.championships.ui;

import au.com.xandar.io.FileHolder;
import au.com.xandar.swimclub.championships.Athlete;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Loads the file containing the swims for all athletes and collates the results. 
 *  
 * @author william
 */
public final class LoadSwimsAction implements ActionListener {
	
	private static final Logger LOGGER = Logger.getLogger(LoadSwimsAction.class);

    private final Component owner;
	private final List<Athlete> athletes;
    private final JFileChooser chooser;
    private final JButton[] buttonsToEnable;
    
    private FileHolder loadFileHolder;
	
	public LoadSwimsAction(Component owner, List<Athlete> athletes, JButton[] buttonsToEnable, JFileChooser fileChooser, FileHolder fileHolder) {
        this.owner = owner;
		this.athletes = athletes;
		this.buttonsToEnable = buttonsToEnable;
		this.chooser = fileChooser;
		this.loadFileHolder = fileHolder;
	}
	
	public void actionPerformed(final ActionEvent event) {

		this.chooser.setDialogType(JFileChooser.OPEN_DIALOG);
		this.chooser.setFileFilter(new FileNameExtensionFilter("Comma Separated Files", "csv"));
	    final int returnVal = this.chooser.showOpenDialog((Component) event.getSource());
	    if (returnVal != JFileChooser.APPROVE_OPTION) {
	    	return;
	    }
		
		final File loadFile = this.chooser.getSelectedFile();
		if (!loadFile.exists()) {
			JOptionPane.showMessageDialog(
					(Component) event.getSource(), 
					new String[] {"SeasonalSpreadsheet file does not exist : " + loadFile}, 
					"Validation Failure",
					JOptionPane.ERROR_MESSAGE);
			return;
		}


        owner.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)); // TODO Determine how to get the wait cursor to work
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final LoadSwimsCommand command = new LoadSwimsCommand(loadFile);
                final List<Athlete> parsedAthletes;
                try {
                    parsedAthletes = command.execute();
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(
                            (Component) event.getSource(),
                            new String[] {"Could not parse Top Times CSV report", e.getMessage()},
                            "Failure",
                            JOptionPane.ERROR_MESSAGE);
                    LOGGER.error("Could not parse Top Times CSV : " + loadFile, e);
                    return;
                } catch (RuntimeException e) {
                    JOptionPane.showMessageDialog(
                            (Component) event.getSource(),
                            new String[] {"Could not parse Top Times CSV report", e.getMessage()},
                            "Failure",
                            JOptionPane.ERROR_MESSAGE);
                    LOGGER.error("Could not parse Top Times CSV : " + loadFile, e);
                    return;
                } finally {
                    owner.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }
                loadFileHolder.setFile(loadFile);

                // Add the loaded athletes.
                athletes.clear();
                athletes.addAll(parsedAthletes);

                // Enable the process button.
                for (final JButton button : buttonsToEnable) {
                    button.setEnabled(true);
                }
            }
        };
        SwingUtilities.invokeLater(runnable);
	}
}
