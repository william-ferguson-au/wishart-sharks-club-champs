package au.com.xandar.parsing;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Responsible for parsing a line of CSV.
 *  
 * @author william
 */
public final class CSVLineParser {

	private static final String DOUBLE_QUOTE = "\"";
	private static final String COMMA = ",";
	private static final String ALL_DELIMITERS = "\",";
	
	private static String EMPTY = "";
	
	/**
	 * Returns the parsed contents of the line of CSV.
	 */
	public List<String> getParsedContents(String line) {
		
		final List<String> tokens = new ArrayList<String>();
		final StringTokenizer tokenizer = new StringTokenizer(line, "\",", true);
		
		// The initial delimiters are either double quote or comma
		String delimiters = ALL_DELIMITERS;
		boolean withinQuoteBlock = false;
		boolean foundToken = false;
		
		while (tokenizer.hasMoreTokens()) {
			final String token = tokenizer.nextToken(delimiters);

			if (DOUBLE_QUOTE.equals(token)) {
				if (withinQuoteBlock) {
					// At end of QuoteBlock so ignore this token and start looking for all delimiters again.
					withinQuoteBlock = false;
					delimiters = ALL_DELIMITERS;
				} else {
					// At start of QuoteBlock so ignore this token and read until finalising DOUBLE_QUOTE.
					withinQuoteBlock = true;
					delimiters = DOUBLE_QUOTE;
				}
			} else if (COMMA.equals(token)) {
				// Found comma delimiter, so ignore it and check to make sure that we found a token in the last block, if not add an empty one.
				if (!foundToken) {
					tokens.add(EMPTY);
				}
				foundToken = false;
			} else {
				// Not a delimiter, so its a token to add to the list.
				tokens.add(token);
				foundToken = true;
			}
		}
		
		return tokens;
	}
}
