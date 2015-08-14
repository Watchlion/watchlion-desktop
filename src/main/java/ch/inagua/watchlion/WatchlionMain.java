/**
 * 
 */
package ch.inagua.watchlion;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.json.simple.parser.ParseException;

import ch.inagua.watchlion.model.Environment;
import ch.inagua.watchlion.model.Watchlion;
import ch.inagua.watchlion.service.WatchlionLoader;
import ch.inagua.watchlion.ui.Frame;

/**
 */
public class WatchlionMain {

	private static final String OPTION_REPORT = "report";
	private static final String OPTION_LOCAL = "local";
	private static final String OPTION_REFERENCE = "reference";

	/**
	 * <pre>
	 * @see http://confluence/pages/viewpage.action?pageId=16193345#Facturation-D%C3%A9veloppement-ToolSVN
	 * </pre>
	 * 
	 * {@link http://wiki.svnkit.com/Printing_Out_Repository_History}
	 * 
	 * @throws SVNException
	 * @throws URISyntaxException
	 * @throws ParseException
	 * @throws IOException
	 * @throws java.text.ParseException 
	 * @throws org.apache.commons.cli.ParseException 
	 */
	public static void main(String[] args) throws IOException, ParseException, java.text.ParseException, org.apache.commons.cli.ParseException {

		CommandLineParser parser = new DefaultParser();
		Options options = new Options();
		options.addOption(OPTION_REFERENCE, true, "File path to the REFERENCE Wtchlion JSON. REQUIRED.");
		options.addOption(OPTION_LOCAL, true, "File path to the LOCAL Wtchlion JSON. Optional, will be created in the HOME directory if missing.");
		options.addOption(OPTION_REPORT, false, "Generate command line report (instead of GUI application). Optional.");
		CommandLine cmd = parser.parse( options, args);
		
		if (! cmd.hasOption(OPTION_REFERENCE)) {
			HelpFormatter formatter = new HelpFormatter(); 
			formatter.printHelp( "Watchlion", options);
		} else {
			String refJSON = cmd.getOptionValue(OPTION_REFERENCE);
			createEmptyJSONIfMissing(refJSON);

			String localJSON = null;
			if (cmd.hasOption(OPTION_LOCAL)) {
				localJSON = cmd.getOptionValue(OPTION_LOCAL);
			} else {
				String home = System.getProperty("user.home");
				{
					localJSON = home + "/Watchlion/watchlion.local.json";
					createEmptyJSONIfMissing(localJSON);
				}
			}

			System.out.println("REFERENCE: " + refJSON);
			System.out.println("LOCAL....: " + localJSON);
			Environment reference = new WatchlionLoader().loadFromJSONFile(refJSON);
			Environment local = new WatchlionLoader().loadFromJSONFile(localJSON);

			final Watchlion watchlion = new Watchlion(reference, local);

			if (cmd.hasOption(OPTION_REPORT)) {
				System.out.println(watchlion.getReport());
				
			} else {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						final JFrame frame = new Frame(watchlion);
						frame.setVisible(true);
					}
				});
			}
		}
	}

	private static void createEmptyJSONIfMissing(String localJSON)
			throws IOException {
		File f = new File(localJSON);
		File p = f.getParentFile();
		if (!p.exists() && !p.mkdirs()) {
			throw new IllegalStateException("Impossible to create folders " + p);
		}
		if (!f.exists()) {
			FileWriter fileWriter = new FileWriter(localJSON);
			fileWriter.write("{}");
			fileWriter.flush();
			fileWriter.close();
		}
	}

}
