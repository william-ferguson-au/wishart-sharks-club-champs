package au.com.xandar.swimclub.championships.ui;

import au.com.xandar.swimclub.championships.Athlete;
import au.com.xandar.swimclub.championships.calculator.EligibilityProcessor;
import au.com.xandar.swimclub.championships.eligibility.AthleteFormatter;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

/**
 * Produces the EligibilityReport for a List of Athletes.
 *
 * @author william
 */
public final class ProduceEligibilityReportCommand {

	private static final Logger LOGGER = Logger.getLogger(ProduceEligibilityReportCommand.class);

	private final List<Athlete> athletes;
    private final EligibilityProcessor processor;
    private final File inputFile;
    private final File outputFile;

	public ProduceEligibilityReportCommand(List<Athlete> athletes, EligibilityProcessor processor, File inputFile, File outputFile) {
		this.athletes = athletes;
		this.processor = processor;
        this.inputFile = inputFile;
        this.outputFile = outputFile;
	}

	public void execute() throws IOException {

        LOGGER.info("ProduceEligibilityReportCommand started");

		// Open outputFile ready for writing.
		final PrintStream outStream = new PrintStream(new FileOutputStream(outputFile));

		LOGGER.info("Nr Athletes = " + this.athletes.size());

		// Write output
		outStream.printf("Club Championships eligibility based on %s", this.inputFile.getName());
        outStream.println();
        outStream.println();
		final AthleteFormatter formatter = new AthleteFormatter(outStream);
		for (final Athlete athlete : this.athletes) {
			formatter.render(athlete, processor);
		}
		outStream.close();

		LOGGER.info("ProduceEligibilityReportCommand completed");
	}
}