package au.com.xandar.swimclub.championships.ui;

import au.com.xandar.io.FileHolder;
import au.com.xandar.swimclub.championships.Athlete;
import au.com.xandar.swimclub.championships.calculator.EligibilityProcessor;
import au.com.xandar.swimclub.championships.calculator.EligibilityProcessorFactory;
import au.com.xandar.swimclub.championships.calculator.ProcessorConstructionException;
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
 * Produces the EligibilityReport for a List of Athletes.
 * 
 * @author william
 */
public final class ProduceEligibilityReportAction implements ActionListener {
	
	private static final Logger LOGGER = Logger.getLogger(ProduceEligibilityReportAction.class);

    private final Component owner;
	private final List<Athlete> athletes;
    private final JFileChooser chooser;
    private final FileHolder inputFileHolder;
    private final EligibilityProcessorFactory processorFactory;
    
	public ProduceEligibilityReportAction(Component owner, List<Athlete> athletes, FileHolder inputFileHolder, JFileChooser fileChooser, EligibilityProcessorFactory processorFactory) {
        this.owner = owner;
		this.athletes = athletes;
		this.inputFileHolder = inputFileHolder;
		this.chooser = fileChooser;
		this.processorFactory = processorFactory;
	}
	
	public void actionPerformed(ActionEvent event) {

		final EligibilityProcessor processor;
		try {
			processor = this.processorFactory.create();
		} catch (ProcessorConstructionException e) {
			JOptionPane.showMessageDialog(
					(Component) event.getSource(), 
					new String[] {"Could not create the EligibilityProcessor", e.getMessage()}, 
					"Failure",
					JOptionPane.ERROR_MESSAGE);
			LOGGER.error("Could not create EligibilityProcessor", e);
			return;
		}
		
		// Select outputFile
		this.chooser.setDialogType(JFileChooser.SAVE_DIALOG);
		this.chooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
		this.chooser.setSelectedFile(this.getDefaultOutputFile(this.inputFileHolder.getFile()));
	    final int returnVal = this.chooser.showSaveDialog((Component) event.getSource());
	    if (returnVal != JFileChooser.APPROVE_OPTION) {
	    	return;
	    }
		
	    // Confirm its ok to overwrite outputFile (if it already exists)
		final File outputFile = this.chooser.getSelectedFile();
		if (outputFile.exists()) {
			final int confirmationResult = JOptionPane.showConfirmDialog(
					(Component) event.getSource(), 
					new String[] {"Results file already exists : " + outputFile, "Overwrite?"}, 
					"Output file exists",
					JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.WARNING_MESSAGE);
			if (confirmationResult == JOptionPane.CANCEL_OPTION) {
				return;
			}
		}

        final ProduceEligibilityReportCommand command = new ProduceEligibilityReportCommand(athletes, processor, this.inputFileHolder.getFile(), outputFile);
        owner.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		try {
			command.execute();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(
					(Component) event.getSource(), 
					new String[] {"Could not write Eligibility results file", e.getMessage()}, 
					"Failure",
					JOptionPane.ERROR_MESSAGE);
			LOGGER.error("Could not write Eligibility results file : " + outputFile, e);
			return;
		} finally {
            owner.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
		
		// Notify user of completion
		JOptionPane.showMessageDialog(
				(Component) event.getSource(), 
				new String[] {"Processing is complete"}, 
				"Processing complete",
				JOptionPane.OK_OPTION);
	}
	
	private File getDefaultOutputFile(File inputFile) {
		final String inputFileName = inputFile.getName();
		final int lastSeparator = inputFileName.lastIndexOf(".");
		return new File(inputFileName.substring(0, lastSeparator) + "-results.txt");		
	}
}
