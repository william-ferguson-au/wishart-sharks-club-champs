package au.com.xandar.swimclub.championships.ui;

import au.com.xandar.io.FileHolder;
import au.com.xandar.swimclub.championships.Athlete;
import au.com.xandar.swimclub.championships.MissedNight;
import au.com.xandar.swimclub.championships.calculator.EligibilityProcessorFactory;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * Captures the location of the Top Times CSV report, where to generate the output 
 * and allows generation of the output.
 * 
 * @author william
 */
public final class EligibilityUI extends JFrame implements FileHolder {

	private final JButton loadSwimsButton = new JButton("Load Swims");
	private final JButton loadMissedNightsButton = new JButton("Missed Nights");
	private final JButton processEligibilityButton = new JButton("Eligibility Report");
	private final JButton produceProgramEventReport = new JButton("Program Events Report");
	
    private final JTextField seasonStartDate = new JTextField();
    private final JTextField seasonEndDate = new JTextField();
    
	private final List<Athlete> athletes = new ArrayList<Athlete>();
	private final Collection<MissedNight> missedNights = new ArrayList<MissedNight>();
	
	private File loadFile;

	/**
	 * Opens the LoadSwimAction at the current working directory.
	 */
	public EligibilityUI() {
		this(new File(System.getProperty("user.dir")));
	}

	public File getFile() {
		return this.loadFile;
	}
	
	public void setFile(File file) {
		this.loadFile = file;
	}
	
	
	/**
	 * @param loadFolder	Folder at which the LoadSwimsAction initially opens.
	 */
	public EligibilityUI(File loadFolder) {
        this.setTitle("SwimClub Championship Eligibility Processor");
        this.setForeground(Color.BLUE);
/*        
		final JPanel labelPane = new JPanel();
		labelPane.setLayout(new BoxLayout(labelPane, BoxLayout.Y_AXIS));
		labelPane.add(new JLabel("")); // Empty for spacing
		labelPane.add(new JLabel("Sprint"));
		labelPane.add(new JLabel("Distance"));
		*/
		final JPanel fullSeasonPane = new JPanel();
		fullSeasonPane.setLayout(new BoxLayout(fullSeasonPane, BoxLayout.Y_AXIS));
		fullSeasonPane.add(new JLabel("Season Start"));
		fullSeasonPane.add(this.seasonStartDate);
		
		// Determine reasonable season start & end dates and set them.
		final DateFormat dateFormatter = new SimpleDateFormat();
		final Date startDate = getDefaultStartDate();
		final Date endDate = getDefaultEndDate();
		this.seasonStartDate.setText(dateFormatter.format(startDate));
		this.seasonEndDate.setText(dateFormatter.format(endDate));
		
		final JPanel halfSeasonPane = new JPanel();
		halfSeasonPane.setLayout(new BoxLayout(halfSeasonPane, BoxLayout.Y_AXIS));
		halfSeasonPane.add(new JLabel("Season End"));
		halfSeasonPane.add(this.seasonEndDate);
		
		final JPanel centerPane = new JPanel();
		centerPane.setLayout(new BoxLayout(centerPane, BoxLayout.X_AXIS));
//		centerPane.add(labelPane);
		centerPane.add(fullSeasonPane);
		centerPane.add(halfSeasonPane);
		
		final JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.LEFT));
		buttonPane.add(this.loadSwimsButton);
		buttonPane.add(this.loadMissedNightsButton);
		buttonPane.add(this.processEligibilityButton);
		buttonPane.add(this.produceProgramEventReport);
		
        this.setLayout(new BorderLayout());
        this.add(centerPane, BorderLayout.CENTER);
        this.add(buttonPane, BorderLayout.SOUTH);

        // Disable those buttons that can only be invoked after loading the swims.
        this.processEligibilityButton.setEnabled(false);
        this.produceProgramEventReport.setEnabled(false);
        
        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(loadFolder);
        
        final EligibilityProcessorFactory processorFactory = new SimpleEligibilityProcessorFactory(this.seasonStartDate, this.seasonEndDate, this.missedNights);
		this.loadSwimsButton.addActionListener(
			new ActionLogger(new LoadSwimsAction(this.getRootPane(), this.athletes, new JButton[] {this.processEligibilityButton, this.produceProgramEventReport}, fileChooser, this))
		);
		this.loadMissedNightsButton.addActionListener(
				new ActionLogger(new LoadMissedNightsAction(this, this.missedNights, fileChooser))
			);
		this.processEligibilityButton.addActionListener(
			new ActionLogger(new ProduceEligibilityReportAction(this, this.athletes, this, fileChooser, processorFactory))
		);
		this.produceProgramEventReport.addActionListener(
			new ActionLogger(new ProduceProgramAction(this, this.athletes, this, fileChooser, processorFactory))
		);
        this.addWindowListener(new WindowCloser());
        
        this.pack();
	}
	
	public static void main(final String[] args) {
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				final EligibilityUI ui;
				if (args.length == 0) {
					ui = new EligibilityUI();
				} else {
					final File loadFolder = new File(args[0]);
					ui = new EligibilityUI(loadFolder);
				}
				
				ui.setVisible(true);
		    }
		});
	}
	
	private static Date getDefaultStartDate() {
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		final int month = calendar.get(Calendar.MONTH);
		
		final int year;
		if (month < 6) {
			// In second half of season, so use start date of 1-JUL-PreviousYear
			year = calendar.get(Calendar.YEAR) - 1;
		} else {
			// In first half of season, so use start date of 1-JUL-CurrentYear
			year = calendar.get(Calendar.YEAR);
		}
		calendar.set(year, 6, 1, 0, 0, 0);
		return calendar.getTime();
	}
	
	private static Date getDefaultEndDate() {
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		final int month = calendar.get(Calendar.MONTH);
		
		final int year;
		if (month < 6) {
			// In second half of season, so use end date of 30-JUN-CurrentYear
			year = calendar.get(Calendar.YEAR);
		} else {
			// In first half of season, so use end date of 30-JUN-NextYear
			year = calendar.get(Calendar.YEAR) + 1;
		}
		calendar.set(year, 5, 30, 0, 0, 0);
		return calendar.getTime();
	}
}
