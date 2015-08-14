/**
 * 
 */
package ch.inagua.watchlion;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.json.simple.parser.ParseException;

import ch.inagua.watchlion.model.Application;
import ch.inagua.watchlion.model.Watchlion;
import ch.inagua.watchlion.service.WatchlionLoader;

/**
 */
public class WatchlionMain {

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
	 */
	public static void main(String[] args) throws IOException, ParseException {

		if (args.length > 1) {
			String refJSON = args[0];
			Watchlion refWatchlion = new WatchlionLoader().loadFromJSONFile(refJSON);

			String localJSON = args[1];
			Watchlion localWatchlion = new WatchlionLoader().loadFromJSONFile(localJSON);
			
			List<Application> apps = refWatchlion.diffWithLocal(localWatchlion);

			System.out.println(refWatchlion.getReport(apps));
			
		} else {
			System.err.println("USAGE: missing 2 filepath as paremeters, watchlion-reference.json and watchlion-local.json");
		}
		
//		URL url = WatchlionMain.class.getProtectionDomain().getCodeSource().getLocation();
//		File file = new File(url.toURI());
//		String path = file.getParent() + "\\properties.json";
//		System.out.println(path);
//		final SVNProperties properties = PropertiesLoader.loadJsonElsePreferences(path);

//		SwingUtilities.invokeLater(new Runnable() {
//			public void run() {
//				final JFrame frame = new SVNFrame(properties);
//				frame.setVisible(true);
//			}
//		});

	}

}
