package au.com.xandar.swimclub.championships.ui;

import au.com.xandar.swimclub.championships.MissedNight;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * Loads the file containing the missed nights for the season.
 * <p>
 * The file needs to an XML file with a format of:
 * <code>
 * <list>
 * 		<MissedNight>
 * 			<date>2008-11-17</date>
 * 			<openHundred>Free</openHundred>
 * 			<qualifiedHundred>Back</qualifiedHundred>
 * 		</MissedNight>
 * </list>
 * </code>
 * </p> 
 *  
 * @author william
 */
public final class LoadMissedNightsAction implements ActionListener {
	
	private static final Logger LOGGER = Logger.getLogger(LoadMissedNightsAction.class);

    private final Component owner;
	private final Collection<MissedNight> missedNights;
    private final JFileChooser chooser;
	
	public LoadMissedNightsAction(Component owner, Collection<MissedNight> missedNights, JFileChooser fileChooser) {
        this.owner = owner;
		this.missedNights = missedNights;
		this.chooser = fileChooser;
	}
	
	@SuppressWarnings("unchecked")
	public void actionPerformed(ActionEvent event) {

		LOGGER.debug("Starting LoadMissedNights");
		
		this.chooser.setDialogType(JFileChooser.OPEN_DIALOG);
		this.chooser.setFileFilter(new FileNameExtensionFilter("XML Files", "xml"));
	    final int returnVal = this.chooser.showOpenDialog((Component) event.getSource());
	    if (returnVal != JFileChooser.APPROVE_OPTION) {
			LOGGER.debug("Cancelled load MissedNights");
	    	return;
	    }
		
		final File loadFile = this.chooser.getSelectedFile();
		if (!loadFile.exists()) {
			JOptionPane.showMessageDialog(
					(Component) event.getSource(), 
					new String[] {"MissedNights file does not exist : " + loadFile}, 
					"Validation Failure",
					JOptionPane.ERROR_MESSAGE);
			LOGGER.info("Load MissedNights aborted. File does not exist : " + loadFile);
			return;
		}

        final List<MissedNight> newMissedNights;
        final LoadMissedNightsCommand command = new LoadMissedNightsCommand(loadFile);
        owner.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		try {
            newMissedNights = command.execute();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(
					(Component) event.getSource(), 
					new String[] {"Could not parse MissedNights file", e.getMessage()}, 
					"Failure",
					JOptionPane.ERROR_MESSAGE);
			LOGGER.error("Could not parse MissedNights file : " + loadFile, e);
			return;
		} finally {
            owner.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
		
		// Add the loaded athletes.
		LOGGER.debug("NewMissedNights:" + newMissedNights);
		this.missedNights.clear();
		this.missedNights.addAll(newMissedNights);
		LOGGER.debug("MissedNights:" + this.missedNights);

		LOGGER.info("LoadMissedNights complete");
	}
}
