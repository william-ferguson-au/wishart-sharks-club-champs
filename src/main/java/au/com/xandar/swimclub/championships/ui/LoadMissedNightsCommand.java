package au.com.xandar.swimclub.championships.ui;

import au.com.xandar.swimclub.championships.MissedNight;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.basic.DateConverter;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
public final class LoadMissedNightsCommand {

	private static final Logger LOGGER = Logger.getLogger(LoadMissedNightsCommand.class);

	private final File inputFile;

	public LoadMissedNightsCommand(File inputFile) {
		this.inputFile = inputFile;
	}

	@SuppressWarnings("unchecked")
	public List<MissedNight> execute() throws IOException {

		LOGGER.debug("LoadMissedNightsCOmmand started");

		final List<MissedNight> missedNights;

		// All good so let's start parsing.
		final XStream xstream = new XStream();
		xstream.alias("MissedNight", MissedNight.class);
		xstream.registerConverter(new DateConverter("yyyy-MM-dd", new String[0]));

        final InputStream stream = new FileInputStream(inputFile);

        // Not sure why we can't pass in newMissedNights. Doco says we can.
        LOGGER.debug("before reading stream");
        missedNights = (List<MissedNight>) xstream.fromXML(stream); //, newMissedNights);
        LOGGER.debug("after reading stream");

        stream.close();

		LOGGER.info("LoadMissedNightsCommand completed");

        return missedNights;
	}
}