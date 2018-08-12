package de.chrissx.alts;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CxcsvParser {

	/**
	 * Loads the alts from the given CXColonSeparatedValues-file.
	 * @param cxcsv The path of the file the CXCSV is located in.
	 * @return The loaded Alts.
	 */
	public static List<Alt> loadAlts(Path cxcsv) throws IOException
	{
		List<Alt> alts = new ArrayList<Alt>();
		for(String s : Files.readAllLines(cxcsv))
		{
			String[] t = s.split(":");
			alts.add(new Alt(t[0], t[1]));
		}
		return alts;
	}
}
