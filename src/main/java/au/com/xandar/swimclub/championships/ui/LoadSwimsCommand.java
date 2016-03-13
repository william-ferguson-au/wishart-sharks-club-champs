package au.com.xandar.swimclub.championships.ui;

import au.com.xandar.swimclub.championships.Athlete;
import au.com.xandar.swimclub.championships.load.TopTimesParser;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Loads the file containing the swims for all athletes and collates the results.
 *
 * @author william
 */
public final class LoadSwimsCommand {

	private static final Logger LOGGER = Logger.getLogger(LoadSwimsCommand.class);

    private final File loadFile;

	public LoadSwimsCommand(File loadFile) {
        this.loadFile = loadFile;
	}

	public List<Athlete> execute() throws IOException {

        LOGGER.info("LoadSwimsCommand started");

		// All good so let's start parsing.
		final TopTimesParser parser = new TopTimesParser();
		final List<Athlete> parsedAthletes = parser.parseSeasonalSpreadSheet(loadFile);

		for (final Athlete athlete : parsedAthletes) {
			LOGGER.info("Athlete : " + athlete);
		}

		LOGGER.info("LoadSwimsCommand completed");

        return parsedAthletes;
	}
}