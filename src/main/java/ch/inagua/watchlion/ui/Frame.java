/**
 * 
 */
package ch.inagua.watchlion.ui;

import java.io.InputStream;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JPanel;

import ch.inagua.watchlion.model.Watchlion;

/**
 */
public class Frame extends JFrame {

	private static final long serialVersionUID = 1L;

	public Frame(Watchlion watchlion) {
		setTitle("Watchlion {v-v})" + getAppVersion());
		setSize(1000, 700);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		// final URL url = ClassLoader.getSystemResource("icon-svn.jpg");
		//final URL url = ClassLoader.getSystemResource("icon-svntoo-transparency.png");
		//setIconImage(Toolkit.getDefaultToolkit().getImage(url));

		final JPanel panel = new Panel(watchlion);
		getContentPane().add(panel);
	}

	private String getAppVersion() {
		try {
			final InputStream input = getClass().getClassLoader().getResourceAsStream("maven-version.properties");
			final Properties prop = new Properties();
			prop.load(input);
			return "   [" + prop.getProperty("maven.version") + "]";
		} catch (Exception e) {
		}
		return " [v]";
	}
}
