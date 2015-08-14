/**
 * 
 */
package ch.inagua.watchlion;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.json.simple.parser.ParseException;

import ch.inagua.watchlion.model.Application;
import ch.inagua.watchlion.model.Environment;
import ch.inagua.watchlion.model.Watchlion;
import ch.inagua.watchlion.service.WatchlionLoader;
import ch.inagua.watchlion.ui.Frame;

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
	 * @throws java.text.ParseException 
	 */
	public static void main(String[] args) throws IOException, ParseException, java.text.ParseException {

		if (args.length > 1) {
			String refJSON = args[0];
			Environment reference = new WatchlionLoader().loadFromJSONFile(refJSON);
			//System.out.println(refJSON);

			String localJSON = args[1];
			Environment local = new WatchlionLoader().loadFromJSONFile(localJSON);
			// System.out.println(localJSON);
			
			final Watchlion watchlion = new Watchlion(reference, local);

			if (args.length > 2 && "report".equals(args[2])) {
				System.out.println(watchlion.getReport());
				
			} else {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						final JFrame frame = new Frame(watchlion);
						frame.setVisible(true);
					}
				});
				
			}
			
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
